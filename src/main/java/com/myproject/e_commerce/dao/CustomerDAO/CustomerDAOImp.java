package com.myproject.e_commerce.dao.CustomerDAO;

import com.myproject.e_commerce.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAOImp implements CustomerDAO {
    private final EntityManager entityManager;
    public CustomerDAOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public boolean existsByUsername(String username) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(c) FROM User c WHERE c.username = :username", Long.class);
        query.setParameter("username", username);
        Long count = query.getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(cd) FROM CustomerDetails cd WHERE cd.email = :email", Long.class);
        query.setParameter("email", email);
        Long count = query.getSingleResult();
        return count > 0;
    }
}
