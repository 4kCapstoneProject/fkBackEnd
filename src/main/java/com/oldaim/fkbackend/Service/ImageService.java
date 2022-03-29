package com.oldaim.fkbackend.Service;

import com.oldaim.fkbackend.Entity.Image;
import com.oldaim.fkbackend.Entity.Information.Information;
import com.oldaim.fkbackend.Repository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final String uploadPath = "/Users/kimdonggyun/Desktop/bootpicture/";

    // 이미지 DTO -> Entity
    public Image fileToEntity(String fileName, String fullPath, Information info){

        return Image.builder()
                .information(info)
                .fileName(fileName)
                .filePath(fullPath)
                .build();
    }

    // 이미지 저장
    public Long imageFileUpload(MultipartFile file,Information info) throws IOException {
        String fileName = file.getName();
        String fullPath = uploadPath + fileName;

        file.transferTo(new File(fullPath));
        Image image = fileToEntity(fileName,fullPath,info);

        imageRepository.save(image);

        return image.getId();
    }

    // 이미지 조회
    public String ImageFindById(Long imageId){

        Optional<Image> image = imageRepository.findById(imageId);

        if (image.isPresent()) {
            return  image.get().getFilePath();
        }
        else{
            throw new NullPointerException();
        }
    }

    // 이미지 삭제
    public void ImageDelete(Long imageId){

        imageRepository.deleteById(imageId);

    }

}
