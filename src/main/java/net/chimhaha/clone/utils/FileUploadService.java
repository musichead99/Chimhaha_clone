package net.chimhaha.clone.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ImageFileDto getImageFile(String path) {
        File file = new File(path);
        byte[] bytes;
        MediaType mediaType;

        try {
            bytes = Files.readAllBytes(file.toPath());
            mediaType = MediaType.parseMediaType(Files.probeContentType(file.toPath()));
        } catch (Exception e) {
            throw new FileDownloadException("해당 파일을 다운로드 할 수 없습니다.");
        }

        return ImageFileDto.builder()
                .bytes(bytes)
                .mediaType(mediaType)
                .build();
    }
}
