package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import com.oldaim.fkbackend.entity.Image;
import com.oldaim.fkbackend.entity.enumType.ThumbNail;
import com.oldaim.fkbackend.entity.information.Information;
import com.oldaim.fkbackend.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    @Value("${upload.path}")
    private String uploadPath;

    // 이미지 저장
    public void imageFileUpload(MultipartFile file, Information info, String thumb) throws IOException {

        String fileName = file.getOriginalFilename();
        String fullPath = uploadPath + fileName;

        file.transferTo(new File(fullPath));
        imageRepository.save(fileToEntity(fileName,fullPath,info,thumb));

    }

    // 이미지 조회
    public ImagePathDto ImageFindByTargetId(Long targetId){

        return entityToDto(imageRepository.findByTargetIdWithThumb(targetId,ThumbNail.Able));

    }

    public List<ImagePathDto> ImageFindAllByTargetId(Long targetId) {

        List<Image> imageList = imageRepository.findAllByTargetId(targetId);

        return imageList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    // 이미지 DTO -> Entity
    private Image fileToEntity(String fileName, String fullPath, Information info,String thumb){

        return Image.builder()
                .information(info)
                .fileName(fileName)
                .filePath(fullPath)
                .thumbNail(ThumbNail.valueOf(thumb))
                .build();
    }

    private ImagePathDto entityToDto(Image image){

        return ImagePathDto.builder()
                .targetPk(image.getInformation().getId())
                .fileName(image.getFileName())
                .filePath(image.getFilePath())
                .build();
    }



}
