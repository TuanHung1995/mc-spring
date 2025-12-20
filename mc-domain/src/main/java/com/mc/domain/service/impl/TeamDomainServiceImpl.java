package com.mc.domain.service.impl;

import com.mc.domain.exception.BusinessLogicException;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.*;
import com.mc.domain.model.enums.ApartmentMemberStatus;
import com.mc.domain.model.enums.RoleType;
import com.mc.domain.port.MailSender;
import com.mc.domain.repository.*;
import com.mc.domain.service.TeamDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamDomainServiceImpl implements TeamDomainService {

    private final TeamRepository teamRepository;
    private final ApartmentRepository apartmentRepository;
    private final ApartmentMemberRepository apartmentMemberRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final MailSender mailSender;

    private String apartmentApiUrl = "/api/v1/teams/";

    @Value("${server.port}")
    private String serverPort;

    @Value("${constants.development}")
    private boolean isDevelopment;

    @Value("${constants.frontend}")
    private String appUrl;


    @Override
    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public Apartment saveApartment(User user, Team team, String name, String description, String backgroundUrl) {

        Apartment apartment = new Apartment();
        apartment.setName(name);
        apartment.setDescription(description);
        apartment.setBackgroundUrl(backgroundUrl);
        apartment.setOwner(user);
        apartment.setTeam(team);
        apartmentRepository.save(apartment);

        Role apartmentRole = roleRepository.findByName(RoleType.ROLE_OWNER_APARTMENT.name())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.ROLE_OWNER_APARTMENT.name()));

        ApartmentMember apartmentMember = new ApartmentMember();
        apartmentMember.setApartment(apartment);
        apartmentMember.setUser(user);
        apartmentMember.setRole(apartmentRole);
        apartmentMemberRepository.save(apartmentMember);

        return apartment;
    }

    @Override
    @Transactional
    public List<User> addApartmentMember(List<User> users, Apartment apartment) {

        List<Long> userIds = users.stream().map(User::getId).toList();

        List<Long> existingMemberUserIds = apartmentMemberRepository.findAllUserIdsByApartmentIdAndUserIdsIn(apartment.getId(), userIds);

        Role memberRole = roleRepository.findByName(RoleType.ROLE_MEMBER_APARTMENT.name())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.ROLE_MEMBER_APARTMENT.name()));

        Role ownerRole = roleRepository.findByName(RoleType.ROLE_OWNER_APARTMENT.name())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.ROLE_OWNER_APARTMENT.name()));

        long apartmentMembers = apartmentMemberRepository.countByApartmentId(apartment.getId());

        List<ApartmentMember> newMembers = users.stream()
                .filter(user -> !existingMemberUserIds.contains(user.getId()))
                .map(user -> {
                    ApartmentMember member = new ApartmentMember();
                    member.setApartment(apartment);
                    member.setUser(user);
                    if (apartmentMembers == 0) {
                        member.setRole(ownerRole);
                    } else {
                        member.setRole(memberRole);
                    }
                    return member;
                })
                .toList();

        if (!newMembers.isEmpty()) {
            apartmentMemberRepository.saveAllApartmentMembers(newMembers);
        }

        return userRepository.findAllByApartmentId(apartment.getId());

    }

    @Override
    public List<User> deletedApartmentMember(ApartmentMember apartmentMember) {
        apartmentMemberRepository.deleteApartmentMember(apartmentMember);
        return userRepository.findAllByApartmentId(apartmentMember.getApartment().getId());
    }

    @Override
    public boolean assignApartmentOwner(boolean isOwner, Long userId, Long apartmentId) {

        ApartmentMember apartmentMember = apartmentMemberRepository.findApartmentMemberByUserIdAndApartmentId(userId, apartmentId)
                .orElseThrow(() -> new ResourceNotFoundException("ApartmentMember", "id", apartmentId.toString()));

        apartmentMember.setOwner(isOwner);
        apartmentMemberRepository.save(apartmentMember);

        return isOwner;

    }

    @Override
    @Transactional
    public void requestToJoinApartment(Long userId, Long apartmentId) {

        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", apartmentId.toString()));

        // Check exist member
        Optional<ApartmentMember> existingMember = apartmentMemberRepository.findApartmentMemberByUserIdAndApartmentId(userId, apartmentId);

        if (existingMember.isPresent()) {
            ApartmentMemberStatus status = existingMember.get().getStatus();
            if (status == ApartmentMemberStatus.ACTIVE) {
                throw new BusinessLogicException("You already joined this apartment.");
            } else if (status == ApartmentMemberStatus.PENDING) {
                throw new BusinessLogicException("Request is pending.");
            }
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        Role apartmentMemberRole = roleRepository.findByName(RoleType.ROLE_MEMBER_APARTMENT.name())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.ROLE_MEMBER_APARTMENT.name()));

        ApartmentMember newMember = new ApartmentMember();
        newMember.setUser(user);
        newMember.setRole(apartmentMemberRole);
        newMember.setApartment(apartment);
        newMember.setStatus(ApartmentMemberStatus.PENDING);
        newMember.setOwner(false);

        apartmentMemberRepository.save(newMember);

        sendNotificationToOwners(apartmentId, user, apartment);

    }

    private void sendNotificationToOwners(Long apartmentId, User requester, Apartment apartment) {

        List<String> ownerEmails = apartmentMemberRepository.findOwnerEmailsByApartmentId(apartmentId);
        if (ownerEmails.isEmpty()) return;

        String subject = "[Monday Clone] New apartment join request: " + apartment.getName();

        String approveLink = "http://localhost:" + serverPort + apartmentApiUrl + "accept-request?apartmentId=" + apartmentId + "/members?action=approve&userId=" + requester.getId();
        String rejectLink = "http://localhost:" + serverPort + apartmentApiUrl + "reject-request?apartmentId=" + apartmentId + "/members?action=reject&userId=" + requester.getId();

        String body = String.format("""
            <div style="font-family: Arial, sans-serif; padding: 20px;">
                <h2>New apartment join request: %s</h2>
                <p>Hi, </p>
                <p><strong>%s</strong> (%s) has requested to join your apartment <strong>%s</strong>.</p>
                <div style="margin: 20px 0;">
                    <a href="%s" style="background-color: #00ca72; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-right: 10px;">Approve</a>
                    <a href="%s" style="background-color: #fb275d; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Reject</a>
                </div>
                <p>Or you can access to admin page to approve or reject.</p>
            </div>
            """,
                apartment.getName(), requester.getFullName(), requester.getEmail(), apartment.getName(), approveLink, rejectLink
        );

        // 3. Gửi mail
        mailSender.send(ownerEmails.toArray(new String[0]), subject, body);
    }

    @Override
    @Transactional
    public void approveRequestJoinApartment(boolean isApprove, Long apartmentId, Long requesterId, Long approverId) {

        // Check owner
        ApartmentMember approver = apartmentMemberRepository.findApartmentMemberByUserIdAndApartmentId(approverId, apartmentId)
                .orElseThrow(() -> new BusinessLogicException("You have no request to approve or reject."));

        if (!RoleType.ROLE_OWNER_APARTMENT.name().equals(approver.getRole().getName())) {
            throw new BusinessLogicException("You are not apartment owner.");
        }

        // Find requester
        ApartmentMember requesterMember = apartmentMemberRepository.findApartmentMemberByUserIdAndApartmentId(requesterId, apartmentId)
                .orElseThrow(() -> new ResourceNotFoundException("ApartmentMember", "userId and apartmentId", requesterId + ", " + apartmentId));

        if (requesterMember.getStatus() == ApartmentMemberStatus.ACTIVE) {
            throw new BusinessLogicException("This user has active.");
        }
        if (requesterMember.getStatus() != ApartmentMemberStatus.PENDING) {
            throw new BusinessLogicException("Not found validated request.");
        }

        if (!isApprove) {
            requesterMember.setStatus(ApartmentMemberStatus.REJECTED);
        } else {
            requesterMember.setStatus(ApartmentMemberStatus.ACTIVE);
        }

        apartmentMemberRepository.save(requesterMember);

        sendApprovalNotification(isApprove, requesterMember.getApartment(), requesterMember.getUser());

    }

    private void sendApprovalNotification(boolean isApprove, Apartment apartment, User user) {

        if (!isApprove) {
            mailSender.send(user.getEmail(),
                    "[Monday Clone] Yêu cầu tham gia bị từ chối",
                    "Yêu cầu tham gia vào " + apartment.getName() + " của bạn đã bị từ chối.");
            return;
        }

        String subject = "[Monday Clone] Yêu cầu tham gia được chấp thuận";
        String loginLink = "http://" + appUrl + "/login";

        String body = String.format("""
        <div style="font-family: Arial, sans-serif; padding: 20px;">
            <h2 style="color: #00ca72;">Yêu cầu được chấp thuận!</h2>
            <p>Xin chào <strong>%s</strong>,</p>
            <p>Yêu cầu tham gia vào <strong>%s</strong> của bạn đã được chấp thuận.</p>
            <p>Bây giờ bạn có thể truy cập và bắt đầu làm việc.</p>
            <div style="margin: 20px 0;">
                <a href="%s" style="background-color: #5960e6; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Truy cập ngay</a>
            </div>
        </div>
        """,
                user.getFullName(), apartment.getName(), loginLink
        );

        mailSender.send(user.getEmail(), subject, body);
    }

}
