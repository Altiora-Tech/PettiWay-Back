package com.altioratech.pettiway.image.infrastructure.out.storage;

import com.altioratech.pettiway.image.domain.StorageServicePort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.UUID;

@Component
public class LocalStorageServiceAdapter implements StorageServicePort {

    private static final String UPLOAD_DIR = "uploads";

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {
            // Crear carpeta si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR, folder);
            Files.createDirectories(uploadPath);

            // Normalizar nombre de archivo (sin espacios ni acentos)
            String originalName = normalizeFileName(file.getOriginalFilename());
            String extension = getExtension(file);
            String uniqueName = UUID.randomUUID() + "_" + originalName + extension;

            // Guardar archivo
            Path filePath = uploadPath.resolve(uniqueName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retornar URL accesible
            return "/uploads/" + folder + "/" + uniqueName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            Path filePath = Paths.get(fileUrl.replace("/uploads/", "uploads/"));
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar archivo", e);
        }
    }

    // ======================
    // Métodos auxiliares
    // ======================

    private String normalizeFileName(String original) {
        if (original == null) return "file";
        String baseName = original.replaceAll("\\s+", "_"); // espacios → _
        baseName = Normalizer.normalize(baseName, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", ""); // quita acentos
        return baseName.replaceAll("[^a-zA-Z0-9_.-]", ""); // limpia símbolos raros
    }

    private String getExtension(MultipartFile file) {
        String type = file.getContentType();
        if (type == null) return "";
        return switch (type) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "application/pdf" -> ".pdf";
            default -> "";
        };
    }
}
