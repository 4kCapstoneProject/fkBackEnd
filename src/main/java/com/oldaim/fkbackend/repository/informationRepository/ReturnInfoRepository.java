package com.oldaim.fkbackend.repository.informationRepository;

import com.oldaim.fkbackend.entity.information.ReturnInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnInfoRepository extends JpaRepository<ReturnInfo,Long> {
}
