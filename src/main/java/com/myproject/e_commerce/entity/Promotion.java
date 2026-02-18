package com.myproject.e_commerce.entity;

import com.myproject.e_commerce.contants.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "promotion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    private String name;
    private DiscountType discountType;
    private String discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    @OneToOne(cascade = CascadeType.ALL)
    private OrdersDetail ordersDetail;
}
