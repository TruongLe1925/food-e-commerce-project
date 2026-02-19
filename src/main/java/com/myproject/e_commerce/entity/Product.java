package com.myproject.e_commerce.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.generator.EventType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "price",precision = 15,scale = 2)
    private BigDecimal price;
    @Column(name = "stock")
    private int stock;
    @Column(name = "create_date" , updatable = false, insertable = false)
    @org.hibernate.annotations.Generated(event = EventType.INSERT)
    private LocalDateTime createDate;
    @Column(name="thumbnailUrl")
    private String thumbnailUrl;
    @Column(name = "imageUrl")
    private String imageUrl;
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH,
            CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "product")
    private List<OrderDetails> orderDetails;
    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "product_option",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id"))
    private List<Option> options;
}
