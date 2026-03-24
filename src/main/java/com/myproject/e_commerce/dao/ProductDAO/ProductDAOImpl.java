package com.myproject.e_commerce.dao.ProductDAO;

import com.myproject.e_commerce.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ProductDAOImpl implements ProductDAO {
    private final EntityManager entityManager;
    public ProductDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public List<Product> getProducts() {
        TypedQuery<Product> query = entityManager.createQuery("FROM Product p LEFT JOIN FETCH p.categories WHERE p.stock > 0", Product.class);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByCategory(Integer id) {
        TypedQuery<Product> query = entityManager.createQuery("FROM Product p LEFT JOIN FETCH p.categories cat WHERE cat.id =: id", Product.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByInStock() {
        TypedQuery<Product> query = entityManager.createQuery("FROM Product p LEFT JOIN FETCH p.categories cat WHERE p.stock = 0", Product.class);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByOutOfStock() {
        TypedQuery<Product> query = entityManager.createQuery("FROM Product p LEFT JOIN FETCH p.categories cat WHERE p.stock > 0", Product.class);
        return query.getResultList();
    }

    @Override
    public List<Product> SearchProduct(String keyword) {
        TypedQuery<Product> query = entityManager.createQuery("FROM Product p LEFT JOIN FETCH p.categories cat WHERE cat.name LIKE : keyword ", Product.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }

    @Override
    public Product getProductById(Integer id) {
        TypedQuery<Product> query1 = entityManager.createQuery(
                "SELECT p FROM Product p LEFT JOIN FETCH p.categories WHERE p.id = :id", Product.class);
        query1.setParameter("id", id);
        Product product = query1.getSingleResult();
        TypedQuery<Product> query2 = entityManager.createQuery(
                "SELECT p FROM Product p LEFT JOIN FETCH p.orderDetails WHERE p.id = :id", Product.class);
        query2.setParameter("id", id);
        product = query2.getSingleResult();
        return product;
    }
}
