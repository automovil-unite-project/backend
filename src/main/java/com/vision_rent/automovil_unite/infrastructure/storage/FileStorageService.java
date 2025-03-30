package com.vision_rent.automovil_unite.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Servicio para almacenar y recuperar archivos.
 */
@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(@Value("${app.storage.location}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new StorageException("No se pudo crear el directorio de almacenamiento", e);
        }
    }
    
    /**
     * Almacena un archivo en el sistema de archivos.
     *
     * @param file Archivo a almacenar
     * @param subDirectory Subdirectorio dentro del directorio raíz
     * @return Ruta relativa del archivo almacenado
     * @throws IOException Si ocurre un error al almacenar el archivo
     */
    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        if (file.isEmpty()) {
            throw new StorageException("No se puede almacenar un archivo vacío");
        }
        
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Verificar caracteres inválidos en el nombre del archivo
        if (originalFilename.contains("..")) {
            throw new StorageException("El nombre del archivo contiene una ruta inválida: " + originalFilename);
        }
        
        // Crear un nombre único para el archivo
        String fileExtension = "";
        if (originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFilename = UUID.randomUUID().toString() + fileExtension;
        
        // Crear el subdirectorio si no existe
        Path targetDir = this.rootLocation.resolve(subDirectory);
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        
        // Copiar el archivo
        Path targetLocation = targetDir.resolve(newFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        return subDirectory + "/" + newFilename;
    }
    
    /**
     * Carga un archivo como un recurso.
     *
     * @param filename Nombre del archivo a cargar
     * @return Recurso que representa el archivo
     */
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("No se pudo leer el archivo: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("No se pudo leer el archivo: " + filename, e);
        }
    }
    
    /**
     * Elimina un archivo.
     *
     * @param filename Nombre del archivo a eliminar
     * @throws IOException Si ocurre un error al eliminar el archivo
     */
    public void deleteFile(String filename) throws IOException {
        Path file = rootLocation.resolve(filename);
        Files.deleteIfExists(file);
    }
}