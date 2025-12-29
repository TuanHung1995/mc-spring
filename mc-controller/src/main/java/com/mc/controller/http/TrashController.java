package com.mc.controller.http;

import com.mc.application.service.TrashAppService;
import com.mc.domain.model.dto.TrashItem;
import com.mc.domain.model.entity.User;
import com.mc.infrastructure.config.security.context.SpringSecurityUserContext;
import com.mc.infrastructure.constant.SecurityConstants;
import com.mc.infrastructure.config.security.JwtTokenProvider;
import com.mc.infrastructure.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trash")
@RequiredArgsConstructor
public class TrashController {

    private final TrashAppService trashAppService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final SpringSecurityUserContext springSecurityUserContext;

    @GetMapping
    public ResponseEntity<List<TrashItem>> getTrashItems(HttpServletRequest request) {
        Long userId = springSecurityUserContext.getCurrentUserId();
        return ResponseEntity.ok(trashAppService.getTrashItems(userId));
    }

    @PutMapping("/restore/{type}/{id}")
    public ResponseEntity<?> restoreItem(@PathVariable String type, @PathVariable Long id) {
        trashAppService.restoreItem(type, id);
        return ResponseEntity.ok("Item restored successfully");
    }

    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<?> deleteItemPermanently(@PathVariable String type, @PathVariable Long id) {
        trashAppService.deleteItemPermanently(type, id);
        return ResponseEntity.ok("Item deleted permanently");
    }
}
