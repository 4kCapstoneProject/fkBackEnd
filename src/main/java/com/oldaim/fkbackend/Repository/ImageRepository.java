package com.oldaim.fkbackend.Repository;

import com.oldaim.fkbackend.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
