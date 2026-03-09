package com.myproject.e_commerce.dao.AdminDAO;

import com.myproject.e_commerce.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDAOImpl implements AdminDAO {
    private final EntityManager entityManager;
    public AdminDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

    }
    @Override
    public long countAllUsers() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class);
        return query.getSingleResult();
    }

    @Override
    public long countAllOrders() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(o) FROM Orders o", Long.class);
        return query.getSingleResult();
    }

    @Override
    public long countAllProducts() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
        return query.getSingleResult();
    }
}
