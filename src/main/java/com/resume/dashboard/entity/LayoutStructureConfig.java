package com.resume.dashboard.entity;

import java.util.List;

/**
 * Replaces the raw `layoutConfigJson` string with a typed structure.
 * Tells the frontend EXACTLY how to render sections and zones.
 */
public class LayoutStructureConfig {

    private Integer columnCount;              // 1, 2, 3 (null = freeform/grid)
    private String sidebarPosition;          // "left", "right", "none"
    private String sidebarWidthPercent;      // "30%", "35%"

    // Ordered section zones (frontend renders in this order)
    private List<SectionZone> zones;

    // Special layout flags
    private Boolean hasHeroSection;          // fullscreen hero at top?
    private Boolean hasFloatingHeader;       // sticky/floating nav?
    private Boolean hasStickyContact;        // fixed contact card?
    private Boolean isScrollBased;           // scroll-triggered reveals?
    private String scrollDirection;          // "vertical", "horizontal"

    // Grid config (for MASONRY, BENTO, MODERN_GRID)
    private GridConfig gridConfig;
    
    

    public Integer getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(Integer columnCount) {
		this.columnCount = columnCount;
	}

	public String getSidebarPosition() {
		return sidebarPosition;
	}

	public void setSidebarPosition(String sidebarPosition) {
		this.sidebarPosition = sidebarPosition;
	}

	public String getSidebarWidthPercent() {
		return sidebarWidthPercent;
	}

	public void setSidebarWidthPercent(String sidebarWidthPercent) {
		this.sidebarWidthPercent = sidebarWidthPercent;
	}

	public List<SectionZone> getZones() {
		return zones;
	}

	public void setZones(List<SectionZone> zones) {
		this.zones = zones;
	}

	public Boolean getHasHeroSection() {
		return hasHeroSection;
	}

	public void setHasHeroSection(Boolean hasHeroSection) {
		this.hasHeroSection = hasHeroSection;
	}

	public Boolean getHasFloatingHeader() {
		return hasFloatingHeader;
	}

	public void setHasFloatingHeader(Boolean hasFloatingHeader) {
		this.hasFloatingHeader = hasFloatingHeader;
	}

	public Boolean getHasStickyContact() {
		return hasStickyContact;
	}

	public void setHasStickyContact(Boolean hasStickyContact) {
		this.hasStickyContact = hasStickyContact;
	}

	public Boolean getIsScrollBased() {
		return isScrollBased;
	}

	public void setIsScrollBased(Boolean isScrollBased) {
		this.isScrollBased = isScrollBased;
	}

	public String getScrollDirection() {
		return scrollDirection;
	}

	public void setScrollDirection(String scrollDirection) {
		this.scrollDirection = scrollDirection;
	}

	public GridConfig getGridConfig() {
		return gridConfig;
	}

	public void setGridConfig(GridConfig gridConfig) {
		this.gridConfig = gridConfig;
	}

	public static class SectionZone {
        private String zoneId;              // e.g. "hero", "sidebar", "main", "footer"
        private String label;               // Human-readable
        private List<String> allowedSections; // section type names allowed here
        private String defaultWidth;         // "100%", "60%", "sidebar"
        private Boolean optional;
        private Integer displayOrder;
		public String getZoneId() {
			return zoneId;
		}
		public void setZoneId(String zoneId) {
			this.zoneId = zoneId;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public List<String> getAllowedSections() {
			return allowedSections;
		}
		public void setAllowedSections(List<String> allowedSections) {
			this.allowedSections = allowedSections;
		}
		public String getDefaultWidth() {
			return defaultWidth;
		}
		public void setDefaultWidth(String defaultWidth) {
			this.defaultWidth = defaultWidth;
		}
		public Boolean getOptional() {
			return optional;
		}
		public void setOptional(Boolean optional) {
			this.optional = optional;
		}
		public Integer getDisplayOrder() {
			return displayOrder;
		}
		public void setDisplayOrder(Integer displayOrder) {
			this.displayOrder = displayOrder;
		}
        
        
    }

    public static class GridConfig {
        private Integer columns;
        private Integer rows;              // null = auto
        private String gap;               // "16px", "24px"
        private String itemAspectRatio;   // "1:1", "4:3", "free"
        private Boolean allowVariableSize; // for masonry / bento
		public Integer getColumns() {
			return columns;
		}
		public void setColumns(Integer columns) {
			this.columns = columns;
		}
		public Integer getRows() {
			return rows;
		}
		public void setRows(Integer rows) {
			this.rows = rows;
		}
		public String getGap() {
			return gap;
		}
		public void setGap(String gap) {
			this.gap = gap;
		}
		public String getItemAspectRatio() {
			return itemAspectRatio;
		}
		public void setItemAspectRatio(String itemAspectRatio) {
			this.itemAspectRatio = itemAspectRatio;
		}
		public Boolean getAllowVariableSize() {
			return allowVariableSize;
		}
		public void setAllowVariableSize(Boolean allowVariableSize) {
			this.allowVariableSize = allowVariableSize;
		}
        
        
    }
}