package com.oldaim.fkbackend.repository.informationRepository;

import com.oldaim.fkbackend.entity.information.ReturnInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnInfoRepository extends JpaRepository<ReturnInfo,Long> {

    @Query("SELECT r FROM ReturnInfo r WHERE r.targetId = ?1")
    List<ReturnInfo> findAllReturnInfoByTargetId(Long id);
}
