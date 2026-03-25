package com.resume.dashboard.service;

import org.springframework.stereotype.Service;

import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.ProfessionType;

@Service
public class ProfessionTaxonomyService {

    public ProfessionType resolveProfessionType(String rawValue) {
        return ProfessionType.fromValue(rawValue);
    }

    public ProfessionCategory resolveProfessionCategory(String rawValue) {
        return resolveProfessionType(rawValue).getCategory();
    }
}
