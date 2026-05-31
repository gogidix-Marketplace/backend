package com.gogidix.corporatecms.domain.service;

import com.gogidix.corporatecms.application.dto.MediaDTO;
import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.application.exception.FileStorageException;
import com.gogidix.corporatecms.application.exception.ResourceNotFoundException;
import com.gogidix.corporatecms.application.mapper.MediaMapper;
import com.gogidix.corporatecms.domain.enums.MediaType;
import com.gogidix.corporatecms.domain.model.Media;
import com.gogidix.corporatecms.domain.repository.ContentRepository;
import com.gogidix.corporatecms.domain.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing media files.
 */
@Service
@RequiredArgsConstructor
public class MediaService {
    private static final Logger log = LoggerFactory.getLogger(MediaService.class);

    private final MediaRepository mediaRepository;
    private final ContentRepository contentRepository;
    private final MediaMapper mediaMapper;

    @Value("${media.storage.location:uploads/media}")
    private String storageLocation;

    @Value("${media.storage.max-file-size:52428800}")
    private long maxFileSize;

    @Value("${media.base-url:/api/media}")
    private String baseUrl;

    @Transactional
    @CacheEvict(value = "media", allEntries = true)
    public MediaDTO uploadMedia(MultipartFile file, String uploaderId, String altText, String caption) {
        log.info("Uploading media file: {}, size: {}", file.getOriginalFilename(), file.getSize());

        validateFile(file);

        try {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = resolveStoragePath(fileName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath);

            MediaType mediaType = determineMediaType(file.getContentType());

            Media media = Media.builder()
                    .id(UUID.randomUUID().toString())
                    .originalFileName(file.getOriginalFilename())
                    .fileName(fileName)
                    .mimeType(file.getContentType())
                    .fileSize(file.getSize())
                    .mediaType(mediaType)
                    .altText(altText)
                    .caption(caption)
                    .uploadedBy(uploaderId)
                    .cdnUrl(buildFileUrl(fileName))
                    .filePath(filePath.toString())
                    .createdAt(LocalDateTime.now())
                    .build();

            Media saved = mediaRepository.save(media);
            log.info("Media uploaded successfully: {}", saved.getId());
            return mediaMapper.toDto(saved);

        } catch (IOException e) {
            log.error("Failed to upload media file: {}", file.getOriginalFilename(), e);
            throw new FileStorageException("Failed to store file: " + file.getOriginalFilename());
        }
    }

    @Cacheable(value = "media", key = "#id")
    public MediaDTO getMediaById(String id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "id", id));
        return mediaMapper.toDto(media);
    }

    public List<MediaDTO> getMediaByUploader(String uploaderId) {
        return mediaMapper.toDtoList(mediaRepository.findByUploadedByAndDeletedFalse(uploaderId));
    }

    public List<MediaDTO> getMediaByType(MediaType type) {
        return mediaMapper.toDtoList(mediaRepository.findByMediaTypeAndDeletedFalse(type));
    }

    public Page<MediaDTO> getMediaByType(MediaType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Media> mediaPage = mediaRepository.findByMediaTypeAndDeletedFalse(type, pageable);
        return mediaPage.map(mediaMapper::toDto);
    }

    public List<MediaDTO> searchMedia(String keyword) {
        return mediaMapper.toDtoList(mediaRepository.searchByFileName(keyword));
    }

    public Page<MediaDTO> searchMedia(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Media> mediaPage = mediaRepository.searchByKeyword(keyword, pageable);
        return mediaPage.map(mediaMapper::toDto);
    }

    public Page<MediaDTO> getMediaByFolder(String folder, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Media> mediaPage = mediaRepository.findByFolderAndDeletedFalse(folder, pageable);
        return mediaPage.map(mediaMapper::toDto);
    }

    @Transactional
    @CacheEvict(value = "media", key = "#id")
    public MediaDTO updateMedia(String id, MediaDTO mediaDTO) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "id", id));

        if (mediaDTO.getAltText() != null) {
            media.setAltText(mediaDTO.getAltText());
        }
        if (mediaDTO.getCaption() != null) {
            media.setCaption(mediaDTO.getCaption());
        }

        Media updated = mediaRepository.save(media);
        log.info("Media updated: {}", id);
        return mediaMapper.toDto(updated);
    }

    @Transactional
    @CacheEvict(value = "media", key = "#id")
    public void deleteMedia(String id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "id", id));

        try {
            Path filePath = Paths.get(media.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Failed to delete file from disk: {}", media.getFilePath(), e);
        }

        mediaRepository.delete(media);
        log.info("Media deleted: {}", id);
    }

    public byte[] downloadMedia(String id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "id", id));

        try {
            Path filePath = Paths.get(media.getFilePath());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("Failed to read media file: {}", media.getFilePath(), e);
            throw new FileStorageException("Failed to read file: " + media.getOriginalFileName());
        }
    }

    public List<MediaDTO> getRecentUploads(int limit) {
        return mediaMapper.toDtoList(mediaRepository.findNonOptimizedMedia().stream()
                .limit(limit).toList());
    }

    public long getTotalStorageUsed() {
        return mediaRepository.countTotalMedia();
    }

    public long getMediaCountByType(MediaType type) {
        return mediaRepository.countByMediaTypeAndDeletedFalse(type);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("Cannot upload empty file");
        }

        if (file.getSize() > maxFileSize) {
            throw new FileStorageException("File size exceeds maximum allowed size");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new FileStorageException("File type not allowed: " + contentType);
        }
    }

    private boolean isAllowedContentType(String contentType) {
        return contentType != null && (
                contentType.startsWith("image/")
                || contentType.startsWith("video/")
                || contentType.equals("application/pdf")
                || contentType.startsWith("application/msword")
                || contentType.startsWith("application/vnd"));
    }

    private MediaType determineMediaType(String contentType) {
        if (contentType == null) {
            return MediaType.DOCUMENT;
        }

        if (contentType.startsWith("image/")) {
            return MediaType.IMAGE;
        } else if (contentType.startsWith("video/")) {
            return MediaType.VIDEO;
        } else {
            return MediaType.DOCUMENT;
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName);
        String baseName = FilenameUtils.getBaseName(originalFileName);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return String.format("%s_%s_%s.%s", baseName, timestamp, uuid, extension);
    }

    private Path resolveStoragePath(String fileName) {
        YearMonth now = YearMonth.now();
        String yearMonth = String.format("%d/%02d", now.getYear(), now.getMonthValue());
        return Paths.get(storageLocation, yearMonth, fileName).toAbsolutePath().normalize();
    }

    private String buildFileUrl(String fileName) {
        YearMonth now = YearMonth.now();
        String yearMonth = String.format("%d/%02d", now.getYear(), now.getMonthValue());
        return String.format("%s/%s/%s", baseUrl, yearMonth, fileName);
    }
}
