package com.oldaim.fkbackend.Entity.Information;

import com.oldaim.fkbackend.Entity.BaseEntity;
import com.oldaim.fkbackend.Entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public Information(Long id, User user, String personName, Long personAge) {
        this.id = id;
        this.user = user;
        this.personName = personName;
        this.personAge = personAge;
    }
}
