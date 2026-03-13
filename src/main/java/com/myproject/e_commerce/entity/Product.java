package com.myproject.e_commerce.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.generator.EventType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH,
            CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "product")
    private List<CartItems> cartItems;
    public void addCategory(Category category){
        if(this.categories == null){ // Kiểm tra danh sách của Product
            this.categories = new ArrayList<>();
        }
        if(category != null) {
            this.categories.add(category);
            category.getProducts().add(this);
        }
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", createDate=" + createDate +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
