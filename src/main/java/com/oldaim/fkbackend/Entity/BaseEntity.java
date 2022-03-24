package com.oldaim.fkbackend.Entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;



@MappedSuperclass
public abstract class BaseEntity {

    @Column
    private LocalDateTime localDateTime;


}
