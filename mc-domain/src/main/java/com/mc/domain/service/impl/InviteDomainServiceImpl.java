package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Board;
import com.mc.domain.model.entity.BoardMember;
import com.mc.domain.model.entity.Role;
import com.mc.domain.model.entity.User;
import com.mc.domain.port.MailSender;
import com.mc.domain.repository.BoardMemberRepository;
import com.mc.domain.repository.BoardRepository;
import com.mc.domain.repository.RoleRepository;
import com.mc.domain.repository.UserRepository;
import com.mc.domain.service.InviteDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InviteDomainServiceImpl implements InviteDomainService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final MailSender mailSender;

    @Override
    public void sendInvitation(Long boardId, String email, String tokenLink) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        String subject = "Invitation to join board: " + board.getName();
        String body = "You have been invited to join the board '" + board.getName() + "'.\n" +
                "Click the link below to accept:\n" + tokenLink;

        mailSender.send(email, subject, body);
    }

    @Override
    public void addUserToBoard(Long boardId, String email, String roleName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        if (boardMemberRepository.existsByBoardIdAndUserId(boardId, user.getId())) {
            return;
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        BoardMember member = new BoardMember();
        member.setBoard(board);
        member.setUser(user);
        member.setRole(role);

        log.info("Adding member to board: {}", member);

        boardMemberRepository.save(member);
    }

}
