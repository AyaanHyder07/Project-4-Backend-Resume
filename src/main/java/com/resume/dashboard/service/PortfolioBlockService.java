package com.resume.dashboard.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resume.dashboard.dto.portfolioBlock.CreatePortfolioBlockRequest;
import com.resume.dashboard.dto.portfolioBlock.PortfolioBlockResponse;
import com.resume.dashboard.dto.portfolioBlock.UpdatePortfolioBlockRequest;
import com.resume.dashboard.entity.PortfolioBlock;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.PortfolioBlockRepository;

@Service
public class PortfolioBlockService {

    private final PortfolioBlockRepository portfolioBlockRepository;
    private final BaseOwnershipService ownershipService;

    public PortfolioBlockService(
            PortfolioBlockRepository portfolioBlockRepository,
            BaseOwnershipService ownershipService) {
        this.portfolioBlockRepository = portfolioBlockRepository;
        this.ownershipService = ownershipService;
    }

    public PortfolioBlockResponse create(String userId, CreatePortfolioBlockRequest request) {
        ownershipService.validateResumeOwnership(request.getResumeId(), userId);

        PortfolioBlock block = new PortfolioBlock();
        block.setId(UUID.randomUUID().toString());
        block.setResumeId(request.getResumeId());
        block.setBlockType(request.getBlockType());
        block.setTitle(request.getTitle());
        block.setEnabled(request.getEnabled() == null || request.getEnabled());
        block.setDisplayOrder(request.getDisplayOrder() != null
                ? request.getDisplayOrder()
                : (int) portfolioBlockRepository.countByResumeId(request.getResumeId()));
        block.setStyleVariant(request.getStyleVariant());
        block.setParentSection(request.getParentSection());
        block.setPayload(request.getPayload());
        block.setCreatedAt(Instant.now());
        block.setUpdatedAt(Instant.now());

        return map(portfolioBlockRepository.save(block));
    }

    public List<PortfolioBlockResponse> getByResume(String userId, String resumeId) {
        ownershipService.validateResumeOwnership(resumeId, userId);
        return portfolioBlockRepository.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public PortfolioBlockResponse update(String userId, String blockId, UpdatePortfolioBlockRequest request) {
        PortfolioBlock block = portfolioBlockRepository.findById(blockId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio block not found"));
        ownershipService.validateResumeOwnership(block.getResumeId(), userId);

        if (request.getBlockType() != null) block.setBlockType(request.getBlockType());
        if (request.getTitle() != null) block.setTitle(request.getTitle());
        if (request.getEnabled() != null) block.setEnabled(request.getEnabled());
        if (request.getDisplayOrder() != null) block.setDisplayOrder(request.getDisplayOrder());
        if (request.getStyleVariant() != null) block.setStyleVariant(request.getStyleVariant());
        if (request.getParentSection() != null) block.setParentSection(request.getParentSection());
        if (request.getPayload() != null) block.setPayload(request.getPayload());
        block.setUpdatedAt(Instant.now());

        return map(portfolioBlockRepository.save(block));
    }

    public void reorder(String userId, String resumeId, List<String> orderedIds) {
        ownershipService.validateResumeOwnership(resumeId, userId);
        List<PortfolioBlock> blocks = portfolioBlockRepository.findByResumeIdOrderByDisplayOrderAsc(resumeId);
        java.util.Map<String, PortfolioBlock> byId = blocks.stream()
                .collect(Collectors.toMap(PortfolioBlock::getId, block -> block));

        for (int index = 0; index < orderedIds.size(); index++) {
            PortfolioBlock block = byId.get(orderedIds.get(index));
            if (block == null) continue;
            block.setDisplayOrder(index);
            block.setUpdatedAt(Instant.now());
            portfolioBlockRepository.save(block);
        }
    }

    public void delete(String userId, String blockId) {
        PortfolioBlock block = portfolioBlockRepository.findById(blockId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio block not found"));
        ownershipService.validateResumeOwnership(block.getResumeId(), userId);
        portfolioBlockRepository.delete(block);
    }

    private PortfolioBlockResponse map(PortfolioBlock block) {
        PortfolioBlockResponse response = new PortfolioBlockResponse();
        response.setId(block.getId());
        response.setResumeId(block.getResumeId());
        response.setBlockType(block.getBlockType());
        response.setTitle(block.getTitle());
        response.setEnabled(block.isEnabled());
        response.setDisplayOrder(block.getDisplayOrder());
        response.setStyleVariant(block.getStyleVariant());
        response.setParentSection(block.getParentSection());
        response.setPayload(block.getPayload());
        response.setCreatedAt(block.getCreatedAt());
        response.setUpdatedAt(block.getUpdatedAt());
        return response;
    }
}
