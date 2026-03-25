package com.resume.dashboard.dto.portfolioBlock;

import java.time.Instant;
import java.util.Map;

import com.resume.dashboard.entity.BlockType;

public class PortfolioBlockResponse {
    private String id;
    private String resumeId;
    private BlockType blockType;
    private String title;
    private boolean enabled;
    private int displayOrder;
    private String styleVariant;
    private String parentSection;
    private Map<String, Object> payload;
    private Instant createdAt;
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getResumeId() { return resumeId; }
    public void setResumeId(String resumeId) { this.resumeId = resumeId; }
    public BlockType getBlockType() { return blockType; }
    public void setBlockType(BlockType blockType) { this.blockType = blockType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
    public String getStyleVariant() { return styleVariant; }
    public void setStyleVariant(String styleVariant) { this.styleVariant = styleVariant; }
    public String getParentSection() { return parentSection; }
    public void setParentSection(String parentSection) { this.parentSection = parentSection; }
    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
