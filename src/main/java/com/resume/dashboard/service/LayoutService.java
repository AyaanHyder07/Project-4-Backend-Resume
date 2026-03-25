package com.resume.dashboard.service;

import com.resume.dashboard.dto.layout.CreateLayoutRequest;
import com.resume.dashboard.dto.layout.LayoutResponse;
import com.resume.dashboard.dto.layout.UpdateLayoutRequest;
import com.resume.dashboard.entity.BlockType;
import com.resume.dashboard.entity.ContentMode;
import com.resume.dashboard.entity.Layout;
import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.LayoutStructureConfig;
import com.resume.dashboard.entity.LayoutType;
import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.ProfessionType;
import com.resume.dashboard.entity.VisualMood;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.LayoutRepository;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LayoutService {

    private final LayoutRepository layoutRepository;
    private final MongoTemplate mongoTemplate;

    public LayoutService(LayoutRepository layoutRepository, MongoTemplate mongoTemplate) {
        this.layoutRepository = layoutRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public LayoutResponse create(CreateLayoutRequest request) {
        Layout layout = new Layout();
        layout.setId(UUID.randomUUID().toString());
        layout.setName(request.getName());
        layout.setDescription(request.getDescription());
        layout.setLayoutType(request.getLayoutType());
        layout.setTargetAudiences(request.getTargetAudiences());
        layout.setCompatibleMoods(request.getCompatibleMoods());
        layout.setProfessionTags(request.getProfessionTags());
        layout.setSupportedProfessionCategories(request.getSupportedProfessionCategories());
        layout.setSupportedProfessionTypes(request.getSupportedProfessionTypes());
        layout.setSupportedContentModes(request.getSupportedContentModes());
        layout.setSupportedMotionPresets(request.getSupportedMotionPresets());
        layout.setRecommendedBlockTypes(request.getRecommendedBlockTypes());
        layout.setDefaultMotionPreset(request.getDefaultMotionPreset());
        layout.setStructureConfig(request.getStructureConfig());
        layout.setRequiredPlan(request.getRequiredPlan() != null ? request.getRequiredPlan() : PlanType.FREE);
        layout.setPreviewImageUrl(request.getPreviewImageUrl());
        layout.setActive(true);
        layout.setVersion(1);
        layout.setCreatedAt(Instant.now());
        layout.setUpdatedAt(Instant.now());
        return map(layoutRepository.save(layout));
    }

    public LayoutResponse update(String id, UpdateLayoutRequest request) {
        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        boolean structuralChange = false;
        if (request.getName() != null) layout.setName(request.getName());
        if (request.getDescription() != null) layout.setDescription(request.getDescription());
        if (request.getLayoutType() != null) layout.setLayoutType(request.getLayoutType());
        if (request.getTargetAudiences() != null) layout.setTargetAudiences(request.getTargetAudiences());
        if (request.getCompatibleMoods() != null) layout.setCompatibleMoods(request.getCompatibleMoods());
        if (request.getProfessionTags() != null) layout.setProfessionTags(request.getProfessionTags());
        if (request.getSupportedProfessionCategories() != null) layout.setSupportedProfessionCategories(request.getSupportedProfessionCategories());
        if (request.getSupportedProfessionTypes() != null) layout.setSupportedProfessionTypes(request.getSupportedProfessionTypes());
        if (request.getSupportedContentModes() != null) layout.setSupportedContentModes(request.getSupportedContentModes());
        if (request.getSupportedMotionPresets() != null) layout.setSupportedMotionPresets(request.getSupportedMotionPresets());
        if (request.getRecommendedBlockTypes() != null) layout.setRecommendedBlockTypes(request.getRecommendedBlockTypes());
        if (request.getDefaultMotionPreset() != null) layout.setDefaultMotionPreset(request.getDefaultMotionPreset());
        if (request.getStructureConfig() != null) {
            layout.setStructureConfig(request.getStructureConfig());
            structuralChange = true;
        }
        if (request.getRequiredPlan() != null) layout.setRequiredPlan(request.getRequiredPlan());
        if (request.getPreviewImageUrl() != null) layout.setPreviewImageUrl(request.getPreviewImageUrl());
        if (request.getActive() != null) layout.setActive(request.getActive());
        if (structuralChange) layout.setVersion(layout.getVersion() + 1);
        layout.setUpdatedAt(Instant.now());
        return map(layoutRepository.save(layout));
    }

    public LayoutResponse getActiveById(String id) {
        return map(layoutRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active layout not found")));
    }

    public List<LayoutResponse> getAllActive() {
        try {
            return layoutRepository.findByActiveTrue().stream().map(this::map).collect(Collectors.toList());
        } catch (Exception ex) {
            return getAllActiveFromDocuments();
        }
    }

    public List<LayoutResponse> getByType(LayoutType type) {
        return getAllActive().stream().filter(layout -> layout.getLayoutType() == type).collect(Collectors.toList());
    }

    public List<LayoutResponse> getByAudience(LayoutAudience audience) {
        return getAllActive().stream()
                .filter(layout -> layout.getTargetAudiences() != null && layout.getTargetAudiences().contains(audience))
                .collect(Collectors.toList());
    }

    public List<LayoutResponse> getByMood(VisualMood mood) {
        return getAllActive().stream()
                .filter(layout -> layout.getCompatibleMoods() != null && layout.getCompatibleMoods().contains(mood))
                .collect(Collectors.toList());
    }

    public List<LayoutResponse> getByProfessionCategory(ProfessionCategory category) {
        return getAllActive().stream()
                .filter(layout -> layout.getSupportedProfessionCategories() != null
                        && layout.getSupportedProfessionCategories().contains(category))
                .collect(Collectors.toList());
    }

    public List<LayoutResponse> getByMotionPreset(MotionPreset motionPreset) {
        return getAllActive().stream()
                .filter(layout -> layout.getSupportedMotionPresets() != null
                        && layout.getSupportedMotionPresets().contains(motionPreset))
                .collect(Collectors.toList());
    }

    public List<LayoutResponse> getByContentMode(ContentMode contentMode) {
        return getAllActive().stream()
                .filter(layout -> layout.getSupportedContentModes() != null
                        && layout.getSupportedContentModes().contains(contentMode))
                .collect(Collectors.toList());
    }

    public List<LayoutResponse> getAvailableForPlan(PlanType userPlan) {
        return getAllActive().stream()
                .filter(l -> userPlan.ordinal() >= l.getRequiredPlan().ordinal())
                .collect(Collectors.toList());
    }

    public void deactivate(String id) {
        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));
        layout.setActive(false);
        layout.setUpdatedAt(Instant.now());
        layoutRepository.save(layout);
    }

    private LayoutResponse map(Layout l) {
        LayoutResponse r = new LayoutResponse();
        r.setId(l.getId());
        r.setName(l.getName());
        r.setDescription(l.getDescription());
        r.setLayoutType(l.getLayoutType());
        r.setTargetAudiences(l.getTargetAudiences());
        r.setCompatibleMoods(l.getCompatibleMoods());
        r.setProfessionTags(l.getProfessionTags());
        r.setSupportedProfessionCategories(l.getSupportedProfessionCategories());
        r.setSupportedProfessionTypes(l.getSupportedProfessionTypes());
        r.setSupportedContentModes(l.getSupportedContentModes());
        r.setSupportedMotionPresets(l.getSupportedMotionPresets());
        r.setRecommendedBlockTypes(l.getRecommendedBlockTypes());
        r.setDefaultMotionPreset(l.getDefaultMotionPreset());
        r.setStructureConfig(l.getStructureConfig());
        r.setRequiredPlan(l.getRequiredPlan());
        r.setPreviewImageUrl(l.getPreviewImageUrl());
        r.setActive(l.isActive());
        r.setVersion(l.getVersion());
        r.setCreatedAt(l.getCreatedAt());
        r.setUpdatedAt(l.getUpdatedAt());
        return r;
    }

    private List<LayoutResponse> getAllActiveFromDocuments() {
        Query query = new Query(new Criteria().orOperator(
                Criteria.where("active").is(true),
                Criteria.where("active").exists(false)
        ));

        List<Document> documents = mongoTemplate.find(query, Document.class, "layouts");
        List<LayoutResponse> responses = new ArrayList<>();
        for (Document document : documents) {
            responses.add(mapDocument(document));
        }
        return responses;
    }

    private LayoutResponse mapDocument(Document document) {
        LayoutResponse response = new LayoutResponse();
        response.setId(readString(document, "_id"));
        response.setName(readString(document, "name"));
        response.setDescription(readString(document, "description"));
        response.setLayoutType(parseEnum(readString(document, "layoutType"), LayoutType.class, LayoutType.SINGLE_COLUMN));
        response.setTargetAudiences(parseEnumList(document.get("targetAudiences"), LayoutAudience.class));
        response.setCompatibleMoods(parseEnumList(document.get("compatibleMoods"), VisualMood.class));
        response.setProfessionTags(parseStringList(document.get("professionTags")));
        response.setSupportedProfessionCategories(parseEnumList(document.get("supportedProfessionCategories"), ProfessionCategory.class));
        response.setSupportedProfessionTypes(parseEnumList(document.get("supportedProfessionTypes"), ProfessionType.class));
        response.setSupportedContentModes(parseEnumList(document.get("supportedContentModes"), ContentMode.class));
        response.setSupportedMotionPresets(parseEnumList(document.get("supportedMotionPresets"), MotionPreset.class));
        response.setRecommendedBlockTypes(parseEnumList(document.get("recommendedBlockTypes"), BlockType.class));
        response.setDefaultMotionPreset(parseEnum(readString(document, "defaultMotionPreset"), MotionPreset.class, MotionPreset.NONE));
        response.setStructureConfig(parseStructureConfig(document.get("structureConfig")));
        response.setRequiredPlan(parseEnum(readString(document, "requiredPlan"), PlanType.class, PlanType.FREE));
        response.setPreviewImageUrl(readString(document, "previewImageUrl"));
        response.setActive(readBoolean(document.get("active"), true));
        response.setVersion(readInt(document.get("version"), 1));
        response.setCreatedAt(readInstant(document.get("createdAt")));
        response.setUpdatedAt(readInstant(document.get("updatedAt")));
        return response;
    }

    private LayoutStructureConfig parseStructureConfig(Object rawValue) {
        if (!(rawValue instanceof Document configDocument)) {
            return null;
        }

        LayoutStructureConfig config = new LayoutStructureConfig();
        config.setColumnCount(readInt(configDocument.get("columnCount"), null));
        config.setSidebarPosition(readString(configDocument, "sidebarPosition"));
        config.setSidebarWidthPercent(readString(configDocument, "sidebarWidthPercent"));
        config.setHasHeroSection(readBooleanObject(configDocument.get("hasHeroSection")));
        config.setHasFloatingHeader(readBooleanObject(configDocument.get("hasFloatingHeader")));
        config.setHasStickyContact(readBooleanObject(configDocument.get("hasStickyContact")));
        config.setIsScrollBased(readBooleanObject(configDocument.get("isScrollBased")));
        config.setScrollDirection(readString(configDocument, "scrollDirection"));
        config.setZones(parseZones(configDocument.get("zones")));
        config.setGridConfig(parseGridConfig(configDocument.get("gridConfig")));
        return config;
    }

    private List<LayoutStructureConfig.SectionZone> parseZones(Object rawValue) {
        if (!(rawValue instanceof List<?> rawList)) {
            return Collections.emptyList();
        }

        List<LayoutStructureConfig.SectionZone> zones = new ArrayList<>();
        for (Object item : rawList) {
            if (!(item instanceof Document zoneDocument)) {
                continue;
            }
            LayoutStructureConfig.SectionZone zone = new LayoutStructureConfig.SectionZone();
            zone.setZoneId(readString(zoneDocument, "zoneId"));
            zone.setLabel(readString(zoneDocument, "label"));
            zone.setAllowedSections(parseStringList(zoneDocument.get("allowedSections")));
            zone.setDefaultWidth(readString(zoneDocument, "defaultWidth"));
            zone.setOptional(readBooleanObject(zoneDocument.get("optional")));
            zone.setDisplayOrder(readInt(zoneDocument.get("displayOrder"), null));
            zones.add(zone);
        }
        return zones;
    }

    private LayoutStructureConfig.GridConfig parseGridConfig(Object rawValue) {
        if (!(rawValue instanceof Document gridDocument)) {
            return null;
        }

        LayoutStructureConfig.GridConfig gridConfig = new LayoutStructureConfig.GridConfig();
        gridConfig.setColumns(readInt(gridDocument.get("columns"), null));
        gridConfig.setRows(readInt(gridDocument.get("rows"), null));
        gridConfig.setGap(readString(gridDocument, "gap"));
        gridConfig.setItemAspectRatio(readString(gridDocument, "itemAspectRatio"));
        gridConfig.setAllowVariableSize(readBooleanObject(gridDocument.get("allowVariableSize")));
        return gridConfig;
    }

    private String readString(Document document, String key) {
        Object value = document.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private boolean readBoolean(Object value, boolean defaultValue) {
        Boolean parsed = readBooleanObject(value);
        return parsed != null ? parsed : defaultValue;
    }

    private Boolean readBooleanObject(Object value) {
        if (value instanceof Boolean bool) return bool;
        if (value instanceof String text && !text.isBlank()) return Boolean.parseBoolean(text);
        return null;
    }

    private Integer readInt(Object value, Integer defaultValue) {
        if (value instanceof Number number) return number.intValue();
        if (value instanceof String text && !text.isBlank()) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private Instant readInstant(Object value) {
        if (value instanceof Instant instant) return instant;
        if (value instanceof java.util.Date date) return date.toInstant();
        if (value instanceof String text && !text.isBlank()) {
            try {
                return Instant.parse(text);
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }

    private List<String> parseStringList(Object rawValue) {
        if (!(rawValue instanceof List<?> rawList)) return Collections.emptyList();
        List<String> values = new ArrayList<>();
        for (Object item : rawList) {
            if (item != null) values.add(String.valueOf(item));
        }
        return values;
    }

    private <E extends Enum<E>> List<E> parseEnumList(Object rawValue, Class<E> enumType) {
        if (!(rawValue instanceof List<?> rawList)) return Collections.emptyList();
        List<E> values = new ArrayList<>();
        for (Object item : rawList) {
            E parsed = parseEnum(item == null ? null : String.valueOf(item), enumType, null);
            if (parsed != null) values.add(parsed);
        }
        return values;
    }

    private <E extends Enum<E>> E parseEnum(String value, Class<E> enumType, E defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        String normalized = value.trim().replace('-', '_').replace(' ', '_').toUpperCase(Locale.ROOT);
        try {
            return Enum.valueOf(enumType, normalized);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }
}
