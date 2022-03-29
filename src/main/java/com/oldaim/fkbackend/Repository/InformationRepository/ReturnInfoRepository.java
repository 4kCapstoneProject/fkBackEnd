package com.oldaim.fkbackend.Repository.InformationRepository;

import com.oldaim.fkbackend.Entity.Information.ReturnInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnInfoRepository extends JpaRepository<ReturnInfo,Long> {
}
