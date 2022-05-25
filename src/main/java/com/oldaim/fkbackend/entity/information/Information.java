package com.oldaim.fkbackend.entity.information;

import com.oldaim.fkbackend.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Information {
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


    public Information(Long id, User user, String personName, Long personAge) {
        this.id = id;
        this.user = user;
        this.personName = personName;
        this.personAge = personAge;
    }
}
