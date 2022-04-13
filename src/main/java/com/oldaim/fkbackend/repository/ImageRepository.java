package com.oldaim.fkbackend.repository;

import com.oldaim.fkbackend.entity.Image;
import com.oldaim.fkbackend.entity.enumType.ThumbNail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query("SELECT i FROM Image i WHERE i.information.id =?1 and i.thumbNail =?2")
    Image findByTargetIdWithThumb(Long targetId, ThumbNail thumbNail) throws NoSuchElementException;

    @Query("SELECT i FROM Image i WHERE i.information.id =?1")
    List<Image> findAllByTargetId(Long targetId) throws NoSuchElementException;
}
