package com.myproject.e_commerce.entity;

import com.myproject.e_commerce.constants.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    @Column(name="name")
    private String name;
    @Column(name="discount_type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    @Column(name = "discount_value",precision = 15,scale = 2)
    private BigDecimal discountValue;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.REFRESH,
            CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "promotion")
    private List<Orders> Orders;
}
