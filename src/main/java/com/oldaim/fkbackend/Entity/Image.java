package com.oldaim.fkbackend.Entity;

import com.oldaim.fkbackend.Entity.EnumType.ImageUsage;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Image extends BaseEntity{

    @Id
    @Column(name = "image_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileName;

    @Column
    private String filePath;

    @Column
    private ImageUsage imageUsage;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
