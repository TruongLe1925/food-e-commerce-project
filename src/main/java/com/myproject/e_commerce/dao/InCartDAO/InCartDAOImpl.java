package com.myproject.e_commerce.dao.InCartDAO;

import com.myproject.e_commerce.entity.Cart;
import com.myproject.e_commerce.entity.CartItems;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class InCartDAOImpl implements InCartDAO{
    private final EntityManager entityManager;
    public InCartDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Cart getCart(String username) {
        try {
            TypedQuery<Cart> query = entityManager.createQuery(
                    "SELECT c FROM Cart c LEFT JOIN FETCH c.user LEFT JOIN FETCH c.cartItems " +
                            "WHERE c.user.username = :username", Cart.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
