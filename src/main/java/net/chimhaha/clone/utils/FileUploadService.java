package net.chimhaha.clone.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.domain.images.Images;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileUploadService {

    @Value("${file.store.path}")
    private String path;

    public List<File> upload(List<MultipartFile> imageFiles) {
        List<File> uploadedFiles = new ArrayList<>();
        imageFiles.forEach(file -> uploadedFiles.add(upload(file)));

        return uploadedFiles;
    }

    public File upload(MultipartFile imageFile) {
        UUID uuid = UUID.randomUUID();
        String realFileName = imageFile.getOriginalFilename();
        String storedFileName = uuid + "_" + realFileName;

        try {
            File file = new File(path + File.separator + storedFileName);
            imageFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new FileUploadException("해당 파일을 업로드할 수 없습니다. : " + realFileName);
        }
    }
}