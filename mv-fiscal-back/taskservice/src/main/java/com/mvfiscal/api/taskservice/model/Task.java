package com.mvfiscal.api.taskservice.model;

import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TAREFA")
@Check(constraints = "TP_STATUS IN ('P', 'A', 'C')")
@Builder
@SequenceGenerator(
        name = "SEQ_TAREFA",
        sequenceName = "SEQ_TAREFA",
        allocationSize = 1,
        initialValue = 1)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(generator = "SEQ_TAREFA", strategy = GenerationType.SEQUENCE)
    @Column(name = "SEQ_TAREFA", length = 8)
    private Long id;

    @Column(name = "DS_TITULO", length = 100)
    private String title;

    @Column(name = "DS_DESCRICAO", length = 255)
    private String description;

    @Comment("Status da tarefa: P(Pendente), A(Em andamento) ou C(Concluída)")
    @Column(name = "TP_STATUS", length = 1)
    private String status;

    @Column(name = "DH_CRIACAO")
    private Date createdDate;

    @Column(name = "DT_LIMITE")
    private Date limitDate;

    @Column(name = "CD_USUARIO", length = 8, nullable = false)
    private Long userId;
}
