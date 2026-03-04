package com.resume.dashboard.dto.publicview;

public class LayoutDTO {

    private String layoutType;   // enum -> string for frontend
    private String layoutConfigJson;  // raw JSON string

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getLayoutConfigJson() {
        return layoutConfigJson;
    }

    public void setLayoutConfigJson(String layoutConfigJson) {
        this.layoutConfigJson = layoutConfigJson;
    }
}