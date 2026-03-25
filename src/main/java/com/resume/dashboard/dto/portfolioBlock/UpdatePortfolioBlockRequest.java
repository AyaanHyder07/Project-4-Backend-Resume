package com.resume.dashboard.dto.portfolioBlock;

import java.util.Map;

import com.resume.dashboard.entity.BlockType;

public class UpdatePortfolioBlockRequest {
    private BlockType blockType;
    private String title;
    private Boolean enabled;
    private Integer displayOrder;
    private String styleVariant;
    private String parentSection;
    private Map<String, Object> payload;

    public BlockType getBlockType() { return blockType; }
    public void setBlockType(BlockType blockType) { this.blockType = blockType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public String getStyleVariant() { return styleVariant; }
    public void setStyleVariant(String styleVariant) { this.styleVariant = styleVariant; }
    public String getParentSection() { return parentSection; }
    public void setParentSection(String parentSection) { this.parentSection = parentSection; }
    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
}
