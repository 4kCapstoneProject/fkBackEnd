package com.oldaim.fkbackend.repository;

import com.oldaim.fkbackend.entity.Image;
import com.oldaim.fkbackend.entity.enumType.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query("SELECT i FROM Image i WHERE i.information.id =?1 and i.fileType =?2")
    Image findByTargetIdWithThumb(Long targetId, FileType fileType) throws NoSuchElementException;

    @Query("SELECT i FROM Image i WHERE i.information.id =?1 and i.fileType = ?2")
    List<Image> findAllByTargetId(Long targetId, FileType fileType) throws NoSuchElementException;
    @Transactional
    @Modifying
    @Query("DELETE FROM Image i WHERE i.information.id =?1 ")
    void deleteAllByTargetId(Long targetId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Image i WHERE  i.information.id =?1 AND i.fileType =?2")
    void deleteByTargetIdAndFileType(Long targetId, FileType captureFile);
}
