package com.myproject.e_commerce.entity;

import com.myproject.e_commerce.constants.StatusOrder;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private StatusOrder status;
    @Column(name = "description")
    private String description;
    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST,CascadeType.MERGE,
                    CascadeType.REFRESH,CascadeType.DETACH},mappedBy="status")
    private List<Orders> orders;
}
