package com.oldaim.fkbackend.entity.information;

import com.oldaim.fkbackend.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class ReturnInfo extends Information  {

    @Column
    private Float lpips ;
    @Column
    private Long targetId;

    @Builder
    public ReturnInfo(Long id, User user, String personName, Long personAge, Float lpips, Long targetId) {
        super(id, user, personName, personAge);
        this.lpips = lpips;
        this.targetId = targetId;
    }
}


