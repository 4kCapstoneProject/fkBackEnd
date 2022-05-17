package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import com.oldaim.fkbackend.entity.Image;
import com.oldaim.fkbackend.entity.enumType.ThumbNail;
import com.oldaim.fkbackend.entity.information.Information;
import com.oldaim.fkbackend.entity.information.TargetInfo;
import com.oldaim.fkbackend.repository.ImageRepository;
import com.oldaim.fkbackend.repository.informationRepository.TargetInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final TargetInfoRepository targetInfoRepository;

    @Value("${upload.path}") private String uploadPath;

    public void imageListFileUpload(List<MultipartFile> imageFileList, Long targetId, int thumbnailNumber) throws IOException {

        TargetInfo targetInfo = targetInfoRepository.findById(targetId)
                .orElseThrow(()->new IllegalArgumentException("유저가 존재하지 않습니다."));

        for (int i = 0, imageFileListSize = imageFileList.size(); i < imageFileListSize; i++) {

            MultipartFile file = imageFileList.get(i);

            if (i == thumbnailNumber - 1) {
                this.imageFileUpload(file, targetInfo, "Able");
            } else {
                this.imageFileUpload(file, targetInfo, "Unable");
            }
        }
    }

    // 이미지 저장
    public void imageFileUpload(MultipartFile file, Information info, String thumb) throws IOException {

        String fileName = file.getOriginalFilename();
        String fullPath = uploadPath + fileName;

        file.transferTo(new File(fullPath));
        imageRepository.save(fileToEntity(fileName,fullPath,info,thumb));

    }

    public void imageByteFileUpload(byte[] fileData, Information info, String thumb) throws IOException {

            String fileName = "uploadFile"+info.getId()+".png";
            String filePath = uploadPath + fileName;

            File imageFile = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            fileOutputStream.write(fileData);
            fileOutputStream.close();

            imageRepository.save(fileToEntity(fileName,filePath,info,thumb));


    }

    // 이미지 조회
    public ImagePathDto ImageFindByTargetId(Long targetId){

        return entityToDto(imageRepository.findByTargetIdWithThumb(targetId,ThumbNail.Able));

    }

    public List<ImagePathDto> ImageFindAllByTargetId(Long targetId) {

        List<Image> imageList = imageRepository.findAllByTargetId(targetId);

        return imageList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public void ImageDeleteByTargetId(Long targetId){
        imageRepository.deleteAllByTargetId(targetId);
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
