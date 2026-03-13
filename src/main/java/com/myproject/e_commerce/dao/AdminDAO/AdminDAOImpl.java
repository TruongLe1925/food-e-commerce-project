package com.myproject.e_commerce.dao.AdminDAO;

import com.myproject.e_commerce.dto.AuthorityDTO;
import com.myproject.e_commerce.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminDAOImpl implements AdminDAO {
    private final EntityManager entityManager;
    public AdminDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

    }
    @Override
    public long countAllUsers() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM User u JOIN u.authorities au WHERE au.authority = 'ROLE_CUSTOMER'", Long.class);
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

    @Override
    public List<User> getAllUser() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.authorities au WHERE au.authority = 'ROLE_MANAGER' OR au.authority = 'ROLE_ADMIN'", User.class);
        return query.getResultList();
    }
}
