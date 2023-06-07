package net.chimhaha.clone.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileUploadService {

    @Value("${file.store.path}")
    private String path;

    public File save(MultipartFile imageFile) {
        UUID uuid = UUID.randomUUID();
        String realFileName = imageFile.getOriginalFilename();
        String storedFileName = uuid + "_" + realFileName;

        try {
            File file = new File(path + File.separator + storedFileName);
            imageFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_NOT_UPLOADED);
        }
    }

    public ImageFileDto getImageFile(String path) {
        File file = new File(path);
        byte[] bytes;
        MediaType mediaType;

        try {
            bytes = Files.readAllBytes(file.toPath());
            mediaType = MediaType.parseMediaType(Files.probeContentType(file.toPath()));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FILE_LOAD_FAILED);
        }

        return ImageFileDto.builder()
                .bytes(bytes)
                .mediaType(mediaType)
                .build();
    }

    public void delete(File file) {
        if(file.exists()) {
            file.delete();
            log.info("file deleted: {}", file.getName());
        } else {
            throw new CustomException(ErrorCode.FILE_NOT_EXIST);
        }
    }
}
