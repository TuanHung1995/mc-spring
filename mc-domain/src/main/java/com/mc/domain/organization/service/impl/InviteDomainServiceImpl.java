package com.mc.domain.organization.service.impl;

import com.mc.domain.organization.service.InviteDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InviteDomainServiceImpl implements InviteDomainService {

//    private final BoardRepository boardRepository;
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final BoardMemberRepository boardMemberRepository;
//    private final MailSender mailSender;

    @Override
    public void sendInvitation(UUID boardId, String email, String tokenLink) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//
//        String subject = "Invitation to join board: " + board.getName();
//        String body = "You have been invited to join the board '" + board.getName() + "'.\n" +
//                "Click the link below to accept:\n" + tokenLink;
//
//        mailSender.send(email, subject, body);
    }

    @Override
    public void addUserToBoard(UUID boardId, String email, String roleName) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//
//        if (boardMemberRepository.existsByBoardIdAndUserId(boardId, user.getId())) {
//            return;
//        }
//
//        Role role = roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//
//        BoardMember member = new BoardMember();
//        member.setBoard(board);
//        member.setUser(user);
//        member.setRole(role);
//
//        log.info("Adding member to board: {}", member);
//
//        boardMemberRepository.save(member);
    }

}
