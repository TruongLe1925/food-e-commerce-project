package com.myproject.e_commerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "authorities")
    private String authority;
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH,CascadeType.REFRESH}
    )
    @ToString.Exclude
    @JoinColumn(name = "username")
    private User user;

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }

}
