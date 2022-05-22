package com.oldaim.fkbackend.repository.informationRepository;

import com.oldaim.fkbackend.entity.information.TargetInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetInfoRepository extends JpaRepository<TargetInfo,Long> {

    @Query("SELECT t FROM TargetInfo t WHERE t.user.userId = ?1")
    List<TargetInfo> findAllToExist(String userId);

    @Query("SELECT t FROM TargetInfo t WHERE t.user.userId = ?1")
    Page<TargetInfo> findAllByUserIdPageable(String userId, Pageable pageable);

    @Query("SELECT t FROM TargetInfo t WHERE t.user.userId=?1 and t.personName = ?2")
    Page<TargetInfo> findAllByPersonNamePageable(String userId, String searchString,Pageable pageable);
}
