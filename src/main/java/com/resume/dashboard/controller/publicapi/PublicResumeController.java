package com.resume.dashboard.controller.publicapi;

import com.resume.dashboard.dto.resume.ResumeResponse;
import com.resume.dashboard.service.PublicResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/public")
public class PublicResumeController {

    private final PublicResumeService publicResumeService;

    @Autowired
    public PublicResumeController(PublicResumeService publicResumeService) {
        this.publicResumeService = publicResumeService;
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResumeResponse> getResume(@PathVariable String id, HttpServletRequest request) {
        String ip = getClientIp(request);
        ResumeResponse res = publicResumeService.getPublishedResume(id, ip);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/resumes/{id}/analytics")
    public ResponseEntity<Void> logDownload(@PathVariable String id, HttpServletRequest request) {
        String ip = getClientIp(request);
        publicResumeService.logDownload(id, ip);
        return ResponseEntity.ok().build();
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
