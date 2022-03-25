package com.oldaim.fkbackend.Entity.Information;


import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
public class TargetInfo extends Information {

    @Column
    private String personCharacteristic;
}
