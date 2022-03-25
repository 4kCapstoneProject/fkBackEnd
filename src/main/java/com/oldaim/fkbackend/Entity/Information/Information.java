package com.oldaim.fkbackend.Entity.Information;

import com.oldaim.fkbackend.Entity.BaseEntity;
import com.oldaim.fkbackend.Entity.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class Information extends BaseEntity {
    @Id
    @Column(name = "info_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_PK")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String personName;

    @Column
    private Long personAge;


}
