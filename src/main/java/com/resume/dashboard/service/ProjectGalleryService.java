package com.resume.dashboard.service;

import com.resume.dashboard.dto.projectgallery.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ProjectGalleryRepository;
import com.resume.dashboard.repository.ProjectRepository;
import com.resume.dashboard.repository.ResumeRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectGalleryService {

	private final ProjectGalleryRepository repo;
	private final ProjectRepository projectRepo;
	private final ResumeRepository resumeRepository;
	private final CloudinaryService cloudinaryService;

	public ProjectGalleryService(ProjectGalleryRepository repo, ProjectRepository projectRepo,
			ResumeRepository resumeRepository, CloudinaryService cloudinaryService) {
		this.repo = repo;
		this.projectRepo = projectRepo;
		this.resumeRepository = resumeRepository;
		this.cloudinaryService = cloudinaryService;
	}

	/*
	 * CREATE
	 */
	public ProjectGalleryResponse create(String userId, CreateProjectGalleryRequest request, MultipartFile mediaFile,
			MultipartFile thumbnailFile) {

		Project project = projectRepo.findById(request.getProjectId())
				.orElseThrow(() -> new ResourceNotFoundException("Project not found"));

		validateOwnership(project, userId);

		int nextOrder = repo.findTopByProjectIdOrderByDisplayOrderDesc(project.getId())
				.map(p -> p.getDisplayOrder() + 1).orElse(1);

		String mediaUrl = request.getMediaUrl();
		String thumbnailUrl = request.getThumbnailUrl();

// Upload media
		if (mediaFile != null && !mediaFile.isEmpty()) {

		    String contentType = mediaFile.getContentType();

		    if (contentType != null && contentType.startsWith("video")) {

		        mediaUrl = cloudinaryService.uploadVideo(
		                mediaFile,
		                "resume/projects/" + project.getResumeId()
		        );

		    } else {

		        mediaUrl = cloudinaryService.uploadImage(
		                mediaFile,
		                "resume/projects/" + project.getResumeId()
		        );
		    }
		}
// Upload thumbnail
		if (thumbnailFile != null && !thumbnailFile.isEmpty()) {

			thumbnailUrl = cloudinaryService.uploadImage(thumbnailFile,
					"resume/projects/thumbnails/" + project.getResumeId());
		}

		ProjectGallery entity = new ProjectGallery();
		entity.setId(UUID.randomUUID().toString());
		entity.setProjectId(project.getId());
		entity.setMediaType(request.getMediaType());
		entity.setMediaUrl(mediaUrl);
		entity.setThumbnailUrl(thumbnailUrl);
		entity.setCaption(request.getCaption());
		entity.setResolutionInfo(request.getResolutionInfo());
		entity.setDisplayOrder(nextOrder);
		entity.setCreatedAt(Instant.now());
		entity.setUpdatedAt(Instant.now());

		return map(repo.save(entity));
	}

	/*
	 * UPDATE
	 */
	public ProjectGalleryResponse update(String userId, String galleryId, UpdateProjectGalleryRequest request,
			MultipartFile mediaFile, MultipartFile thumbnailFile) {

		ProjectGallery entity = repo.findById(galleryId)
				.orElseThrow(() -> new ResourceNotFoundException("Gallery item not found"));

		Project project = projectRepo.findById(entity.getProjectId())
				.orElseThrow(() -> new ResourceNotFoundException("Project not found"));

		validateOwnership(project, userId);

		if (mediaFile != null && !mediaFile.isEmpty()) {

		    String contentType = mediaFile.getContentType();

		    if (contentType != null && contentType.startsWith("video")) {

		        entity.setMediaUrl(
		                cloudinaryService.uploadVideo(mediaFile, "resume/projects/" + project.getResumeId())
		        );

		    } else {

		        entity.setMediaUrl(
		                cloudinaryService.uploadImage(mediaFile, "resume/projects/" + project.getResumeId())
		        );
		    }
		}

		// Replace thumbnail if provided
		if (thumbnailFile != null && !thumbnailFile.isEmpty()) {

			entity.setThumbnailUrl(cloudinaryService.uploadImage(thumbnailFile,
					"resume/projects/thumbnails/" + project.getResumeId()));

		} else if (request.getThumbnailUrl() != null) {

			entity.setThumbnailUrl(request.getThumbnailUrl());
		}

		if (request.getCaption() != null)
			entity.setCaption(request.getCaption());

		if (request.getResolutionInfo() != null)
			entity.setResolutionInfo(request.getResolutionInfo());

		entity.setUpdatedAt(Instant.now());

		return map(repo.save(entity));
	}

	/*
	 * DELETE
	 */
	public void delete(String userId, String galleryId) {

		ProjectGallery entity = repo.findById(galleryId)
				.orElseThrow(() -> new ResourceNotFoundException("Gallery item not found"));

		Project project = projectRepo.findById(entity.getProjectId())
				.orElseThrow(() -> new ResourceNotFoundException("Project not found"));

		validateOwnership(project, userId);

		repo.delete(entity);
	}

	/*
	 * PRIVATE LIST
	 */
	public List<ProjectGalleryResponse> getByProject(String userId, String projectId) {

		Project project = projectRepo.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found"));

		validateOwnership(project, userId);

		return repo.findByProjectIdOrderByDisplayOrderAsc(projectId).stream().map(this::map)
				.collect(Collectors.toList());
	}

	/*
	 * PUBLIC LIST
	 */
	public List<PublicProjectGalleryResponse> getPublic(String projectId) {

		Project project = projectRepo.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found"));

		// Only allow public projects
		if (project.getVisibility() != VisibilityType.PUBLIC) {
			throw new AccessDeniedException("Project is not public");
		}

		return repo.findByProjectIdOrderByDisplayOrderAsc(projectId).stream().map(this::mapPublic)
				.collect(Collectors.toList());
	}

	/*
	 * REORDER (Transactional for consistency)
	 */
	@Transactional
	public void reorder(String userId, String projectId, List<String> orderedIds) {

		Project project = projectRepo.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found"));

		validateOwnership(project, userId);

		int order = 1;

		for (String id : orderedIds) {

			ProjectGallery entity = repo.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Gallery item not found"));

			// Ensure gallery belongs to same project
			if (!entity.getProjectId().equals(projectId)) {
				throw new AccessDeniedException("Gallery item mismatch");
			}

			entity.setDisplayOrder(order++);
			entity.setUpdatedAt(Instant.now());
			repo.save(entity);
		}
	}

	/*
	 * OWNERSHIP VALIDATION
	 */
	private void validateOwnership(Project project, String userId) {

		Resume resume = resumeRepository.findById(project.getResumeId())
				.orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

		if (!resume.getUserId().equals(userId)) {
			throw new AccessDeniedException("Unauthorized access to this project");
		}
	}

	/*
	 * MAPPERS
	 */
	private ProjectGalleryResponse map(ProjectGallery g) {

		ProjectGalleryResponse r = new ProjectGalleryResponse();
		r.setId(g.getId());
		r.setProjectId(g.getProjectId());
		r.setMediaType(g.getMediaType());
		r.setMediaUrl(g.getMediaUrl());
		r.setThumbnailUrl(g.getThumbnailUrl());
		r.setCaption(g.getCaption());
		r.setResolutionInfo(g.getResolutionInfo());
		r.setDisplayOrder(g.getDisplayOrder());
		r.setCreatedAt(g.getCreatedAt());
		r.setUpdatedAt(g.getUpdatedAt());
		return r;
	}

	private PublicProjectGalleryResponse mapPublic(ProjectGallery g) {

		PublicProjectGalleryResponse r = new PublicProjectGalleryResponse();
		r.setMediaType(g.getMediaType());
		r.setMediaUrl(g.getMediaUrl());
		r.setThumbnailUrl(g.getThumbnailUrl());
		r.setCaption(g.getCaption());
		return r;
	}
}