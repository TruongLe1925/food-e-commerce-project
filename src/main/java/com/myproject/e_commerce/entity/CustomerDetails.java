package com.myproject.e_commerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @OneToOne(cascade = {
            CascadeType.PERSIST,CascadeType.MERGE,
            CascadeType.REFRESH,CascadeType.DETACH
    })
    @ToString.Exclude
    @JoinColumn(name = "users_id")
    private User user;

}
