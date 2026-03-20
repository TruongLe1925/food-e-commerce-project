package com.myproject.e_commerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "original_price",precision = 15,scale = 2)
    private BigDecimal originalPrice;
    @Column(name = "discount_price",precision = 15,scale = 2)
    private BigDecimal discountPrice;
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH,CascadeType.REFRESH}
    )
    @ToString.Exclude
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH,CascadeType.REFRESH}
    )
    @ToString.Exclude
    @JoinColumn(name = "order_id")
    private Orders orders;

}
