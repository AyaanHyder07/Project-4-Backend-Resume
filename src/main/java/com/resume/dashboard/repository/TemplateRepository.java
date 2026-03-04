package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.PlanType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends MongoRepository<Template, String> {

    List<Template> findByActiveTrue();

    List<Template> findByPlanLevelAndActiveTrue(PlanType planLevel);

    List<Template> findByProfessionTagsContainingAndActiveTrue(String profession);

    Optional<Template> findByIdAndActiveTrue(String id);

    boolean existsByNameIgnoreCase(String name);
}