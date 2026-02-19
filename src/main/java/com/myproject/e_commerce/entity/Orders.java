package com.myproject.e_commerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "order_date" , updatable = false, insertable = false)
    @org.hibernate.annotations.Generated(event = EventType.INSERT)
    private LocalDateTime orderDate;
    @Column(name = "order_address")
    private String orderAddress;
    @Column(name = "note")
    @Lob
    private String note;
    @ManyToOne(fetch = FetchType.LAZY
                ,cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="customer.detail")
    private CustomerDetails customerDetails;
    @ManyToOne(fetch = FetchType.LAZY
                ,cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "status_id")
    private Status status;
    @OneToMany(fetch = FetchType.LAZY
            ,cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH},mappedBy = "orders")
    private List<OrderDetails> orderDetails;
}
