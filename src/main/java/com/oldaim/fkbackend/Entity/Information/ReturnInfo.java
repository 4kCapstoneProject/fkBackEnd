package com.oldaim.fkbackend.Entity.Information;

import com.oldaim.fkbackend.Entity.Image;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
public class ReturnInfo extends Information  {

    @JoinColumn(name = "image_Id")
    @OneToOne
    private Image restoreImage;

    @Column
    private Long similarityFirst;

    @Column
    private Long similaritySecond;


}
