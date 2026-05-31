package com.gogidix.corporatecms.interfaces.rest.validator;

import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class FileValidator {

    private static final Set<String> ALLOWED_IMAGE_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    ));

    private static final Set<String> ALLOWED_VIDEO_TYPES = new HashSet<>(Arrays.asList(
            "video/mp4", "video/webm", "video/quicktime"
    ));

    private static final Set<String> ALLOWED_DOCUMENT_TYPES = new HashSet<>(Arrays.asList(
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    ));

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    public static boolean isValidImageFile(MultipartFile file) {
        return file != null && file.getContentType() != null
                && ALLOWED_IMAGE_TYPES.contains(file.getContentType())
                && file.getSize() <= MAX_FILE_SIZE;
    }

    public static boolean isValidVideoFile(MultipartFile file) {
        return file != null && file.getContentType() != null
                && ALLOWED_VIDEO_TYPES.contains(file.getContentType())
                && file.getSize() <= MAX_FILE_SIZE * 2; // 100MB for videos
    }

    public static boolean isValidDocumentFile(MultipartFile file) {
        return file != null && file.getContentType() != null
                && ALLOWED_DOCUMENT_TYPES.contains(file.getContentType())
                && file.getSize() <= MAX_FILE_SIZE;
    }

    public static boolean isValidFileSize(MultipartFile file) {
        return file != null && file.getSize() <= MAX_FILE_SIZE;
    }

    public static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    public static boolean isAllowedImageExtension(String extension) {
        return Set.of("jpg", "jpeg", "png", "gif", "webp").contains(extension);
    }
}
