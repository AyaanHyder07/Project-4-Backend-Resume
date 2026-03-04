package com.resume.dashboard.controller;

import com.resume.dashboard.dto.contact.*;
import com.resume.dashboard.entity.ContactStatus;
import com.resume.dashboard.service.ContactMessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactMessageController {

    private final ContactMessageService contactService;

    public ContactMessageController(ContactMessageService contactService) {
        this.contactService = contactService;
    }

    /* =========================================================
       PUBLIC SUBMIT MESSAGE
       POST /api/contacts
    ========================================================= */
    @PostMapping
    public ResponseEntity<Void> submit(
            @Valid @RequestBody CreateContactMessageRequest request) {

        contactService.submitMessage(request);
        return ResponseEntity.ok().build();
    }

    /* =========================================================
       GET INBOX (OWNER)
       GET /api/contacts/resume/{resumeId}
    ========================================================= */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<ContactMessageResponse>> getInbox(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                contactService.getInbox(userId, resumeId)
        );
    }

    /* =========================================================
       FILTER BY STATUS (OWNER)
       GET /api/contacts/resume/{resumeId}/status/{status}
    ========================================================= */
    @GetMapping("/resume/{resumeId}/status/{status}")
    public ResponseEntity<List<ContactMessageResponse>> getByStatus(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @PathVariable ContactStatus status) {

        return ResponseEntity.ok(
                contactService.getByStatus(userId, resumeId, status)
        );
    }

    /* =========================================================
       UPDATE STATUS (OWNER)
       PUT /api/contacts/{messageId}/status
    ========================================================= */
    @PutMapping("/{messageId}/status")
    public ResponseEntity<Void> updateStatus(
            @AuthenticationPrincipal String userId,
            @PathVariable String messageId,
            @RequestParam ContactStatus status) {

        contactService.updateStatus(userId, messageId, status);
        return ResponseEntity.ok().build();
    }

    /* =========================================================
       DELETE MESSAGE (OWNER)
    ========================================================= */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String messageId) {

        contactService.delete(userId, messageId);
        return ResponseEntity.noContent().build();
    }

    /* =========================================================
       UNREAD COUNT (OWNER)
    ========================================================= */
    @GetMapping("/resume/{resumeId}/unread-count")
    public ResponseEntity<Long> unreadCount(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                contactService.getUnreadCount(userId, resumeId)
        );
    }
}