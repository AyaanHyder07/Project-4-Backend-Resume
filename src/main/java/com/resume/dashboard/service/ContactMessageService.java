package com.resume.dashboard.service;

import com.resume.dashboard.dto.contact.*;
import com.resume.dashboard.entity.ContactMessage;
import com.resume.dashboard.entity.ContactStatus;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ContactMessageRepository;
import com.resume.dashboard.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContactMessageService {

    private final ContactMessageRepository contactRepo;
    private final ResumeRepository resumeRepo;

    public ContactMessageService(ContactMessageRepository contactRepo,
                                 ResumeRepository resumeRepo) {
        this.contactRepo = contactRepo;
        this.resumeRepo = resumeRepo;
    }

    /*
     * PUBLIC SUBMISSION
     */
    public void submitMessage(CreateContactMessageRequest request) {

        Resume resume = resumeRepo.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.isPublished()) {
            throw new IllegalStateException("Resume not accepting messages");
        }

        ContactMessage msg = new ContactMessage();
        msg.setId(UUID.randomUUID().toString());
        msg.setResumeId(request.getResumeId());
        msg.setSenderName(request.getSenderName());
        msg.setSenderEmail(request.getSenderEmail());
        msg.setSenderPhone(request.getSenderPhone());
        msg.setSubject(request.getSubject());
        msg.setMessage(request.getMessage());
        msg.setStatus(ContactStatus.NEW);
        msg.setReceivedAt(Instant.now());
        msg.setCreatedAt(Instant.now());
        msg.setUpdatedAt(Instant.now());

        contactRepo.save(msg);

        // Future:
        // - Send email notification
        // - Push notification
        // - Analytics event
    }

    /*
     * GET INBOX
     */
    public List<ContactMessageResponse> getInbox(String userId,
                                                 String resumeId) {

        validateOwnership(userId, resumeId);

        return contactRepo.findByResumeIdOrderByReceivedAtDesc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * FILTER BY STATUS
     */
    public List<ContactMessageResponse> getByStatus(String userId,
                                                    String resumeId,
                                                    ContactStatus status) {

        validateOwnership(userId, resumeId);

        return contactRepo
                .findByResumeIdAndStatusOrderByReceivedAtDesc(resumeId, status)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * UPDATE STATUS
     */
    public void updateStatus(String userId,
                             String messageId,
                             ContactStatus status) {

        ContactMessage msg = contactRepo.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        Resume resume = resumeRepo.findById(msg.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        msg.setStatus(status);
        msg.setUpdatedAt(Instant.now());

        contactRepo.save(msg);
    }

    /*
     * DELETE MESSAGE
     */
    public void delete(String userId, String messageId) {

        ContactMessage msg = contactRepo.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        Resume resume = resumeRepo.findById(msg.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        contactRepo.delete(msg);
    }

    /*
     * UNREAD COUNT
     */
    public long getUnreadCount(String userId, String resumeId) {

        validateOwnership(userId, resumeId);

        return contactRepo.countByResumeIdAndStatus(resumeId, ContactStatus.NEW);
    }

    /*
     * OWNERSHIP VALIDATION
     */
    private void validateOwnership(String userId, String resumeId) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    private ContactMessageResponse map(ContactMessage msg) {

        ContactMessageResponse res = new ContactMessageResponse();
        res.setId(msg.getId());
        res.setResumeId(msg.getResumeId());
        res.setSenderName(msg.getSenderName());
        res.setSenderEmail(msg.getSenderEmail());
        res.setSenderPhone(msg.getSenderPhone());
        res.setSubject(msg.getSubject());
        res.setMessage(msg.getMessage());
        res.setStatus(msg.getStatus().name());
        res.setReceivedAt(msg.getReceivedAt());
        res.setCreatedAt(msg.getCreatedAt());
        res.setUpdatedAt(msg.getUpdatedAt());

        return res;
    }
}