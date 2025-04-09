package com.mvfiscal.api.userservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USUARIO")
@Builder
@SequenceGenerator(
        name = "SEQ_USUARIO",
        sequenceName = "SEQ_USUARIO",
        allocationSize = 1,
        initialValue = 1)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "SEQ_USUARIO", strategy = GenerationType.SEQUENCE)
    @Column(name = "CD_USUARIO", length = 8)
    private Long id;

    @Column(name = "NM_USUARIO", length = 200, nullable = false)
    private String name;

    @Column(name = "DS_EMAIL", unique = true, length = 200, nullable = false)
    private String email;

    @Column(name = "DH_CRIACAO", nullable = false)
    private Date createdDate;
}
