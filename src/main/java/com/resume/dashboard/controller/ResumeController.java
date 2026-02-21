package com.resume.dashboard.controller;

import com.resume.dashboard.dto.resume.ResumeRequest;
import com.resume.dashboard.dto.resume.ResumeResponse;
import com.resume.dashboard.dto.resume.ResumeVersionResponse;
import com.resume.dashboard.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ResumeResponse> create(@Valid @RequestBody ResumeRequest req) {
        ResumeResponse res = resumeService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResumeResponse> update(@PathVariable String id, @Valid @RequestBody ResumeRequest req) {
        ResumeResponse res = resumeService.update(id, req);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}/submit")
    public ResponseEntity<ResumeResponse> submit(@PathVariable String id) {
        ResumeResponse res = resumeService.submit(id);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ResumeResponse>> getMyResumes() {
        List<ResumeResponse> list = resumeService.getMyResumes();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getById(@PathVariable String id) {
        ResumeResponse res = resumeService.getById(id);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<ResumeVersionResponse>> getVersions(@PathVariable String id) {
        List<ResumeVersionResponse> list = resumeService.getVersions(id);
        return ResponseEntity.ok(list);
    }
}
