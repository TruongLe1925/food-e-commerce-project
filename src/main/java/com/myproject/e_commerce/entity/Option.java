package com.myproject.e_commerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "option")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private String price;
}
