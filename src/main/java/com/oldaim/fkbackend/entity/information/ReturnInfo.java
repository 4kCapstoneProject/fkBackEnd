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
    private Double maskOnSimilarity;

    @Column
    private Double maskOffSimilarity;

    @Builder
    public ReturnInfo(Long id, User user, String personName, Long personAge, Double maskOnSimilarity, Double maskOffSimilarity) {
        super(id, user, personName, personAge);
        this.maskOnSimilarity = maskOnSimilarity;
        this.maskOffSimilarity = maskOffSimilarity;
    }
}


