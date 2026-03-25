package com.myproject.e_commerce.dao.OrderDAO;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.entity.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    private  final EntityManager entityManager;
    public OrderDetailsDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Optional<Orders> findOrderById(Integer orderId) {
        try {
            TypedQuery<Orders> query = entityManager.createQuery("SELECT o FROM Orders o LEFT JOIN FETCH o.orderDetails LEFT JOIN FETCH o.customerDetails LEFT JOIN FETCH o.status LEFT JOIN FETCH o.orderDetails.product  LEFT JOIN FETCH o.customerDetails.user WHERE o.id = :orderId", Orders.class);
            query.setParameter("orderId", orderId);
            return Optional.ofNullable(query.getSingleResult());
        }catch(NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Orders> findAllOrder() {
        String jpql = "SELECT DISTINCT o FROM Orders o " +
                "LEFT JOIN FETCH o.orderDetails od " +
                "LEFT JOIN FETCH od.product " +
                "LEFT JOIN FETCH o.customerDetails cd " +
                "LEFT JOIN FETCH cd.user " +
                "LEFT JOIN FETCH o.status";

        TypedQuery<Orders> query = entityManager.createQuery(jpql, Orders.class);
        return query.getResultList();
    }

    @Override
    public List<Orders> findAllOrderByStatus(StatusOrder status) {
        String jpql = "SELECT DISTINCT o FROM Orders o " +
                "LEFT JOIN FETCH o.orderDetails od " +
                "LEFT JOIN FETCH od.product " +
                "LEFT JOIN FETCH o.customerDetails cd " +
                "LEFT JOIN FETCH cd.user " +
                "LEFT JOIN FETCH o.status WHERE o.status.status = :status";

        TypedQuery<Orders> query = entityManager.createQuery(jpql, Orders.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Orders> findAllOrderByUsername(String username) {
        String jpql = "SELECT DISTINCT o FROM Orders o " +
                "LEFT JOIN FETCH o.orderDetails od " +
                "LEFT JOIN FETCH od.product " +
                "LEFT JOIN FETCH o.customerDetails cd " +
                "LEFT JOIN FETCH cd.user " +
                "LEFT JOIN FETCH o.status WHERE cd.user.username = :username ORDER BY o.id DESC";

        TypedQuery<Orders> query = entityManager.createQuery(jpql, Orders.class);
        query.setParameter("username", username);
        return query.getResultList();
    }


}
