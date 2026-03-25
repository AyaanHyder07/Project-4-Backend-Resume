package com.resume.dashboard.dto.publicview;

import java.util.Map;

public class PublicPortfolioBlockDTO {
    private String id;
    private String blockType;
    private String title;
    private int displayOrder;
    private String styleVariant;
    private String parentSection;
    private Map<String, Object> payload;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBlockType() { return blockType; }
    public void setBlockType(String blockType) { this.blockType = blockType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
    public String getStyleVariant() { return styleVariant; }
    public void setStyleVariant(String styleVariant) { this.styleVariant = styleVariant; }
    public String getParentSection() { return parentSection; }
    public void setParentSection(String parentSection) { this.parentSection = parentSection; }
    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
}
