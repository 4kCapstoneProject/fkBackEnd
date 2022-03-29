package com.oldaim.fkbackend.Entity.Information;


import com.oldaim.fkbackend.Entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class TargetInfo extends Information {

    @Column
    private String personCharacteristic;

    @Builder
    public TargetInfo(Long id, User user, String personName, Long personAge, String personCharacteristic) {
        super(id, user, personName, personAge);
        this.personCharacteristic = personCharacteristic;
    }
}
