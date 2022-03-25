package com.oldaim.fkbackend.Entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;



@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime localDateTime;


}
