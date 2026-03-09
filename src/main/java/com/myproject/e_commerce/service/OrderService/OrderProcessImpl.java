package com.myproject.e_commerce.service.OrderService;
import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.dto.OrderDetailsDTO;
import com.myproject.e_commerce.dto.OrderDetailsWrapperDTO;
import com.myproject.e_commerce.dto.OrderProcessDTO;
import com.myproject.e_commerce.entity.CustomerDetails;
import com.myproject.e_commerce.entity.OrderDetails;
import com.myproject.e_commerce.entity.Orders;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class OrderProcessImpl implements OrderProcess {
    @Override
    public List<OrderProcessDTO> findOrderForEmployee(List<Orders> orders) {
        return orders.stream().map(order -> {
            BigDecimal totalAmount = order.getOrderDetails().stream()
                    .map(detail -> detail.getOriginalPrice()
                            .multiply(new BigDecimal(detail.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            OrderProcessDTO dto = new OrderProcessDTO();
            dto.setOrderId(order.getId());
            dto.setFullname(order.getCustomerDetails().getFullName());
            dto.setOrderDate(order.getOrderDate());
            dto.setTotalPrice(totalAmount);
            dto.setStatus(order.getStatus().getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public OrderDetailsWrapperDTO getOrderDetails(Orders orders,Integer orderId) {
        List<OrderDetails> orderDetailsList = orders.getOrderDetails();
        CustomerDetails customerDetails = orders.getCustomerDetails();
        CustomerDetailDTO customerDetailDTO = CustomerDetailDTO.builder()
                .fullName(customerDetails.getFullName())
                .email(customerDetails.getEmail())
                .phoneNumber(customerDetails.getPhoneNumber())
                .address(customerDetails.getAddress())
                .build();
        List<OrderDetailsDTO> orderDetailsDTOList = orderDetailsList.stream()
                .map(orderDetails -> OrderDetailsDTO.builder()
                        .productName(orderDetails.getProduct().getName())
                        .quantity(orderDetails.getQuantity())
                        .originalPrice(orderDetails.getOriginalPrice())
                        .imageUrl(orderDetails.getProduct().getImageUrl())
                        .note(orderDetails.getOrders().getNote())
                        .totalPrice(orderDetails.getOriginalPrice().multiply(BigDecimal.valueOf(orderDetails.getQuantity())))
                        .discountPromotion(orderDetails.getOriginalPrice())
                        .build())
                .toList();
        BigDecimal totalPrice = orderDetailsDTOList.stream()
                .map(OrderDetailsDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return OrderDetailsWrapperDTO.builder()
                .orderDetailsHistoryListDTO(orderDetailsDTOList)
                .customerDetailDTO(customerDetailDTO)
                .orderDate(orders.getOrderDate())
                .orderId(orderId)
                .grandTotalPrice(totalPrice)
                .status(orders.getStatus().getStatus())
                .build();
    }
}
