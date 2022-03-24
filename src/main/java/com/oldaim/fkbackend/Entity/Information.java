package com.oldaim.fkbackend.Entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Information extends BaseEntity {

    @Id
    @Column(name = "info_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_PK")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "image_Id")
    @OneToOne
    private Image recoverImage;

    @Column
    private Long similarity;

}
