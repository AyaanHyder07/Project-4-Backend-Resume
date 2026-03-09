package com.resume.dashboard.dto.publicview;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.LayoutStructureConfig;
import com.resume.dashboard.entity.VisualMood;

import java.util.List;

/**
 * Layout data sent to the frontend renderer.
 *
 * FIX: targetAudiences is List<LayoutAudience> and compatibleMoods is List<VisualMood>
 * to match the Layout entity exactly. The previous version used List<String> which
 * caused a type mismatch on setTargetAudiences() and setCompatibleMoods().
 *
 * structureConfig is LayoutStructureConfig (not raw Object/String) — the full
 * structured config object that the renderer reads to decide column layout,
 * section placement, sidebar config, etc.
 */
public class LayoutDTO {

    private String layoutType;                      // LayoutType enum name e.g. "TWO_COLUMN_LEFT_SIDEBAR"
    private int layoutVersion;                      // version snapshot stored on resume
    private LayoutStructureConfig structureConfig;  // full structure config for renderer
    private List<LayoutAudience> targetAudiences;   // ✅ matches Layout entity type
    private List<VisualMood> compatibleMoods;        // ✅ matches Layout entity type
    private List<String> professionTags;             // free-text tags e.g. "musician", "surgeon"

    public String getLayoutType() { return layoutType; }
    public void setLayoutType(String layoutType) { this.layoutType = layoutType; }

    public int getLayoutVersion() { return layoutVersion; }
    public void setLayoutVersion(int layoutVersion) { this.layoutVersion = layoutVersion; }

    public LayoutStructureConfig getStructureConfig() { return structureConfig; }
    public void setStructureConfig(LayoutStructureConfig structureConfig) {
        this.structureConfig = structureConfig;
    }

    public List<LayoutAudience> getTargetAudiences() { return targetAudiences; }
    public void setTargetAudiences(List<LayoutAudience> targetAudiences) {
        this.targetAudiences = targetAudiences;
    }

    public List<VisualMood> getCompatibleMoods() { return compatibleMoods; }
    public void setCompatibleMoods(List<VisualMood> compatibleMoods) {
        this.compatibleMoods = compatibleMoods;
    }

    public List<String> getProfessionTags() { return professionTags; }
    public void setProfessionTags(List<String> professionTags) {
        this.professionTags = professionTags;
    }
}