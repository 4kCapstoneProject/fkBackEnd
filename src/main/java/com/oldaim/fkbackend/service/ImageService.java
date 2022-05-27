package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import com.oldaim.fkbackend.entity.Image;
import com.oldaim.fkbackend.entity.enumType.FileType;
import com.oldaim.fkbackend.entity.information.Information;
import com.oldaim.fkbackend.entity.information.TargetInfo;
import com.oldaim.fkbackend.repository.ImageRepository;
import com.oldaim.fkbackend.repository.informationRepository.TargetInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    public String imageListFileUpload(List<MultipartFile> imageFileList, Long targetId, int isUploadFile) throws IOException {

        TargetInfo targetInfo = targetInfoRepository.findById(targetId)
                .orElseThrow(()->new IllegalArgumentException("유저가 존재하지 않습니다."));

        String imagePath = "";

        for (MultipartFile file : imageFileList) {

            if (isUploadFile == 1) {
                imagePath = this.imageFileUpload(file, targetInfo, "UPLOAD_FILE");
            } else {
                imagePath = this.imageFileUpload(file, targetInfo, "CAPTURE_FILE");
            }
        }

        return imagePath;
    }

    // 이미지 저장
    public String imageFileUpload(MultipartFile file, Information info, String fileType) throws IOException {

        String fileName = file.getOriginalFilename();
        String fullPath = uploadPath + fileName;

        file.transferTo(new File(fullPath));
        imageRepository.save(fileToEntity(fileName,fullPath,info,fileType));

        return fullPath;
    }

    public void imageByteFileUpload(byte[] fileData, Information info, String fileType) throws IOException {

            String fileName = "uploadFile"+info.getId()+".png";
            String filePath = uploadPath + fileName;

            File imageFile = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            fileOutputStream.write(fileData);
            fileOutputStream.close();

            imageRepository.save(fileToEntity(fileName,filePath,info,fileType));


    }

    // 이미지 조회
    public ImagePathDto uploadImageFindByTargetId(Long targetId){

        return entityToDto(imageRepository.findByTargetIdWithThumb(targetId,FileType.UPLOAD_FILE));

    }

    public ImagePathDto captureImageFindByTargetId(Long targetId){

        return entityToDto(imageRepository.findByTargetIdWithThumb(targetId,FileType.CAPTURE_FILE));

    }


    public List<ImagePathDto> imageFindAllByInformationId(Long targetId) {

        List<Image> imageList = imageRepository.findAllByTargetId(targetId, FileType.UPLOAD_FILE);

        return imageList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public void ImageDeleteByTargetId(Long targetId){
        imageRepository.deleteAllByTargetId(targetId);
    }

    public void captureImageDeleteByTargetIdAndFileType(Long targetId){

        imageRepository.deleteByTargetIdAndFileType(targetId,FileType.CAPTURE_FILE);

    }

    // 이미지 DTO -> Entity
    private Image fileToEntity(String fileName, String fullPath, Information info,String fileType){

        return Image.builder()
                .information(info)
                .fileName(fileName)
                .filePath(fullPath)
                .fileType(FileType.valueOf(fileType))
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
