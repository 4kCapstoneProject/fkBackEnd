package com.oldaim.fkbackend.Repository.InformationRepository;

import com.oldaim.fkbackend.Entity.Information.TargetInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetInfoRepository extends JpaRepository<TargetInfo,Long> {
}
