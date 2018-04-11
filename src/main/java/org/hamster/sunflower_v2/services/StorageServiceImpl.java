package org.hamster.sunflower_v2.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@Service(value = "storageService")
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation = Paths.get("src/main/resources/static/images/client");
    private final Path productLocation = Paths.get("src/main/resources/static/images/client/product");

    @Override
    public String store(MultipartFile file) {
        String filename;

        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            filename = timestamp.getTime() + "-" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }

        return filename;
    }

    @Override
    public String storeProduct(MultipartFile file) {
        String filename;

        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            filename = timestamp.getTime() + "-" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.productLocation.resolve(filename));
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }

        return filename;
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectory(rootLocation);
            }

            if (!Files.exists(productLocation)) {
                Files.createDirectory(productLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}
