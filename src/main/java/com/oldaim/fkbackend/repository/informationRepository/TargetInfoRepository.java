package com.oldaim.fkbackend.repository.informationRepository;

import com.oldaim.fkbackend.entity.information.TargetInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetInfoRepository extends JpaRepository<TargetInfo,Long> {
}
