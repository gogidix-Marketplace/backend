package com.gogidix.foundation.devtools.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.foundation.devtools.domain.entity.DocumentationProject;
import com.gogidix.foundation.devtools.domain.entity.DocumentationGeneration;
import com.gogidix.foundation.devtools.domain.repository.DocumentationProjectRepository;
import com.gogidix.foundation.devtools.domain.repository.DocumentationGenerationRepository;
import com.gogidix.foundation.devtools.dto.DocumentationProjectDto;
import com.gogidix.foundation.devtools.dto.DocumentationGenerationDto;
import com.gogidix.foundation.devtools.dto.DocumentationRequest;
import com.gogidix.foundation.devtools.dto.DocumentationResult;
import com.gogidix.foundation.devtools.exception.DocumentationException;
import com.gogidix.foundation.devtools.mapper.DocumentationMapper;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service for documentation generation functionality.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentationService {

    private final DocumentationProjectRepository projectRepository;
    private final DocumentationGenerationRepository generationRepository;
    private final DocumentationMapper mapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${devtools.documentation.output-dir:./docs/generated}")
    private String outputDir;

    @Value("${devtools.documentation.formats:markdown,html}")
    private String defaultFormats;

    /**
     * Create a new documentation project.
     */
    @Transactional
    public DocumentationProjectDto createProject(DocumentationProjectDto dto) {
        log.info("Creating documentation project: {}", dto.getName());

        DocumentationProject entity = mapper.toEntity(dto);
        entity.setUuid(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        entity = projectRepository.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * Update an existing documentation project.
     */
    @Transactional
    @CacheEvict(value = "documentationProjects", key = "#uuid")
    public DocumentationProjectDto updateProject(UUID uuid, DocumentationProjectDto dto) {
        log.info("Updating documentation project: {}", uuid);

        DocumentationProject entity = projectRepository.findByUuid(uuid)
                .orElseThrow(() -> new DocumentationException("Documentation project not found: " + uuid));

        mapper.updateEntityFromDto(dto, entity);
        entity.setUpdatedAt(LocalDateTime.now());

        entity = projectRepository.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * Get a documentation project by UUID.
     */
    @Cacheable(value = "documentationProjects", key = "#uuid")
    @Transactional(readOnly = true)
    public DocumentationProjectDto getProject(UUID uuid) {
        DocumentationProject entity = projectRepository.findByUuid(uuid)
                .orElseThrow(() -> new DocumentationException("Documentation project not found: " + uuid));
        return mapper.toDto(entity);
    }

    /**
     * Get all documentation projects for a project.
     */
    @Transactional(readOnly = true)
    public Page<DocumentationProjectDto> getProjectsByProjectId(String projectId, Pageable pageable) {
        return projectRepository.findByProjectId(projectId, pageable)
                .map(mapper::toDto);
    }

    /**
     * Delete a documentation project.
     */
    @Transactional
    @CacheEvict(value = "documentationProjects", key = "#uuid")
    public void deleteProject(UUID uuid) {
        log.info("Deleting documentation project: {}", uuid);
        projectRepository.findByUuid(uuid).orElseThrow(() -> new DocumentationException("Documentation project not found: " + uuid)); projectRepository.deleteById(projectRepository.findByUuid(uuid).get().getId());
    }

    /**
     * Generate documentation for a project.
     */
    @Async
    @Transactional
    public CompletableFuture<DocumentationResult> generateDocumentation(Long projectId, DocumentationRequest request) {
        DocumentationProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new DocumentationException("Documentation project not found: " + projectId));

        return generateDocs(project, request);
    }

    /**
     * Internal method to generate documentation.
     */
    private CompletableFuture<DocumentationResult> generateDocs(DocumentationProject project,
                                                                  DocumentationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            DocumentationResult.DocumentationResultBuilder resultBuilder = DocumentationResult.builder()
                    .projectUuid(project.getUuid())
                    .projectName(project.getName());

            DocumentationGeneration.DocumentationGenerationBuilder generationBuilder =
                    DocumentationGeneration.builder()
                            .uuid(UUID.randomUUID())
                            .projectId(project.getId())
                            .generatedBy(request.getGeneratedBy())
                            .createdAt(LocalDateTime.now());

            try {
                log.info("Generating documentation for project: {}", project.getName());

                // Parse configuration
                Map<String, Object> config = parseConfiguration(project.getConfiguration());
                List<String> formats = request.getFormats() != null && !request.getFormats().isEmpty()
                        ? request.getFormats()
                        : Arrays.asList(defaultFormats.split(","));

                // Create output directory
                Path outputPath = createOutputDirectory(project.getUuid());

                // Generate documentation from source
                String markdownContent = generateFromSource(project, config);
                generationBuilder.outputContent(markdownContent);

                // Generate different formats
                Map<String, String> generatedFiles = new HashMap<>();

                for (String format : formats) {
                    String formatPath = generateFormat(markdownContent, format, outputPath, project.getName());
                    generatedFiles.put(format, formatPath);
                }

                long generationTime = System.currentTimeMillis() - startTime;

                resultBuilder.status("SUCCESS")
                        .formats(formats)
                        .outputPath(outputPath.toString())
                        .generatedFiles(generatedFiles)
                        .pageCount(countPages(markdownContent))
                        .generationTime(generationTime);

                generationBuilder.status("SUCCESS")
                        .outputPath(outputPath.toString())
                        .pageCount(countPages(markdownContent))
                        .generationTime(generationTime);

            } catch (Exception e) {
                log.error("Documentation generation failed", e);
                long generationTime = System.currentTimeMillis() - startTime;

                resultBuilder.status("FAILED")
                        .errorMessage(e.getMessage())
                        .generationTime(generationTime);

                generationBuilder.status("FAILED")
                        .errorMessage(e.getMessage())
                        .generationTime(generationTime);
            }

            DocumentationResult result = resultBuilder.build();
            DocumentationGeneration generation = generationBuilder.build();

            // Save generation
            generationRepository.save(generation);

            return result;
        });
    }

    /**
     * Get documentation generation history.
     */
    @Transactional(readOnly = true)
    public List<DocumentationGenerationDto> getGenerationHistory(Long projectId, Pageable pageable) {
        return generationRepository.findByProjectId(projectId, pageable)
                .stream()
                .map(mapper::toGenerationDto)
                .toList();
    }

    /**
     * Get generation by UUID.
     */
    @Transactional(readOnly = true)
    public DocumentationGenerationDto getGeneration(UUID uuid) {
        DocumentationGeneration generation = generationRepository.findByUuid(uuid)
                .orElseThrow(() -> new DocumentationException("Generation not found: " + uuid));
        return mapper.toGenerationDto(generation);
    }

    /**
     * Generate API documentation from code.
     */
    public String generateApiDocumentation(String projectId, String sourcePath) {
        StringBuilder markdown = new StringBuilder();
        markdown.append("# API Documentation\n\n");
        markdown.append("Generated for project: ").append(projectId).append("\n\n");
        markdown.append("Source path: ").append(sourcePath).append("\n\n");

        // Scan for API endpoints (simplified example)
        try {
            Path path = Paths.get(sourcePath);
            if (Files.exists(path)) {
                Files.walk(path)
                        .filter(p -> p.toString().endsWith(".java"))
                        .forEach(javaFile -> {
                            try {
                                String content = Files.readString(javaFile);
                                if (content.contains("@RestController") || content.contains("@RequestMapping")) {
                                    markdown.append("## ")
                                            .append(javaFile.getFileName())
                                            .append("\n\n```java\n")
                                            .append(content)
                                            .append("\n```\n\n");
                                }
                            } catch (IOException e) {
                                log.warn("Failed to read file: {}", javaFile, e);
                            }
                        });
            }
        } catch (IOException e) {
            log.error("Failed to scan source path", e);
        }

        return markdown.toString();
    }

    /**
     * Generate database documentation.
     */
    public String generateDatabaseDocumentation(String schemaName) {
        StringBuilder markdown = new StringBuilder();
        markdown.append("# Database Documentation\n\n");
        markdown.append("Schema: ").append(schemaName).append("\n\n");

        // This would typically query the database schema
        markdown.append("## Tables\n\n");
        markdown.append("* Table documentation would be generated here\n\n");

        return markdown.toString();
    }

    /**
     * Preview documentation.
     */
    public String previewDocumentation(String markdownContent) {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(markdownContent != null ? markdownContent : "");
        return renderer.render(document);
    }

    private Map<String, Object> parseConfiguration(String configurationJson) {
        try {
            if (configurationJson != null && !configurationJson.isBlank()) {
                return objectMapper.readValue(configurationJson,
                        objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
            }
        } catch (Exception e) {
            log.warn("Failed to parse configuration JSON", e);
        }
        return new HashMap<>();
    }

    private Path createOutputDirectory(UUID projectUuid) throws IOException {
        Path path = Paths.get(outputDir, projectUuid.toString());
        Files.createDirectories(path);
        return path;
    }

    private String generateFromSource(DocumentationProject project, Map<String, Object> config) throws IOException {
        StringBuilder markdown = new StringBuilder();

        // Header
        markdown.append("# ").append(project.getName()).append("\n\n");
        if (project.getDescription() != null) {
            markdown.append(project.getDescription()).append("\n\n");
        }

        // Add timestamp
        markdown.append("*Generated on: ").append(LocalDateTime.now()).append("*\n\n");

        // If source path is provided, scan it
        if (project.getSourcePath() != null && !project.getSourcePath().isBlank()) {
            Path sourcePath = Paths.get(project.getSourcePath());
            if (Files.exists(sourcePath)) {
                markdown.append("## Source Files\n\n");
                Files.walk(sourcePath, 10)
                        .filter(p -> !Files.isDirectory(p))
                        .filter(p -> p.toString().matches(".*\\.(java|js|ts|py|go|rs)$"))
                        .forEach(file -> {
                            try {
                                String relativePath = sourcePath.relativize(file).toString();
                                markdown.append("### ").append(relativePath).append("\n\n");

                                if (file.toString().endsWith(".java")) {
                                    String content = Files.readString(file);
                                    // Extract class documentation
                                    extractJavaDoc(content, markdown);
                                }
                            } catch (IOException e) {
                                log.warn("Failed to read file: {}", file, e);
                            }
                        });
            }
        }

        return markdown.toString();
    }

    private void extractJavaDoc(String content, StringBuilder markdown) {
        // Extract class-level comments
        String[] lines = content.split("\n");
        boolean inClassComment = false;
        StringBuilder classDoc = new StringBuilder();

        for (String line : lines) {
            if (line.trim().startsWith("/**")) {
                inClassComment = true;
            } else if (inClassComment) {
                if (line.trim().endsWith("*/")) {
                    inClassComment = false;
                    if (classDoc.length() > 0) {
                        markdown.append(classDoc.toString().trim())
                                .append("\n\n");
                    }
                } else {
                    String cleaned = line.trim()
                            .replace("/*", "")
                            .replace("*/", "")
                            .replace("*", "")
                            .trim();
                    if (!cleaned.isEmpty()) {
                        classDoc.append(cleaned).append("\n");
                    }
                }
            } else if (line.contains("class ") || line.contains("interface ") || line.contains("enum ")) {
                // Found class declaration
                markdown.append("```java\n").append(line.trim()).append("\n```\n\n");
                break;
            }
        }
    }

    private String generateFormat(String markdownContent, String format, Path outputPath, String projectName) throws IOException {
        String fileName = projectName.toLowerCase().replace(" ", "-");

        switch (format.toLowerCase()) {
            case "markdown":
            case "md":
                Path mdPath = outputPath.resolve(fileName + ".md");
                Files.writeString(mdPath, markdownContent);
                return mdPath.toString();

            case "html":
                String htmlContent = convertMarkdownToHtml(markdownContent);
                Path htmlPath = outputPath.resolve(fileName + ".html");
                Files.writeString(htmlPath, wrapHtml(htmlContent, projectName));
                return htmlPath.toString();

            default:
                throw new DocumentationException("Unsupported format: " + format);
        }
    }

    private String convertMarkdownToHtml(String markdown) {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    private String wrapHtml(String content, String title) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>" + title + "</title>\n" +
                "    <style>\n" +
                "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; line-height: 1.6; }\n" +
                "        table { border-collapse: collapse; width: 100%; }\n" +
                "        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n" +
                "        th { background-color: #f2f2f2; }\n" +
                "        code { background-color: #f4f4f4; padding: 2px 4px; border-radius: 3px; }\n" +
                "        pre { background-color: #f4f4f4; padding: 10px; border-radius: 5px; overflow-x: auto; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                content +
                "</body>\n" +
                "</html>";
    }

    private int countPages(String content) {
        if (content == null || content.isBlank()) {
            return 0;
        }
        // Count major headings (h1, h2) as page indicators
        long headingCount = content.lines()
                .filter(line -> line.startsWith("#"))
                .count();
        return (int) Math.max(1, headingCount / 5);
    }
}
