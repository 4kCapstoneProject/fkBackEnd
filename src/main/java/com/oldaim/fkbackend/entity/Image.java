package com.oldaim.fkbackend.entity;

import com.oldaim.fkbackend.entity.enumType.ThumbNail;
import com.oldaim.fkbackend.entity.information.Information;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Image extends BaseEntity{

    @Id
    @Column(name = "image_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileName;

    @Column
    private String filePath;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Information information;

    @Column
    @Enumerated
    private ThumbNail thumbNail;

    @Builder
    public Image(Long id, String fileName, String filePath, Information information, ThumbNail thumbNail) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.information = information;
        this.thumbNail = thumbNail;
    }
}