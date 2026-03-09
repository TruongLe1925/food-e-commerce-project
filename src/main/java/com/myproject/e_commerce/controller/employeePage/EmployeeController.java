package com.myproject.e_commerce.controller.employeePage;
import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dto.OrderDetailsWrapperDTO;
import com.myproject.e_commerce.dto.OrderProcessDTO;
import com.myproject.e_commerce.service.OrderService.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import java.util.List;
@Controller
@RequestMapping("/manager")
public class EmployeeController {
    private final OrderService orderService;
    public EmployeeController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/orders")
    public String orders(@RequestParam(name = "status", required = false) StatusOrder status , Model model, Principal principal) {
        String username = principal.getName();
        List<OrderProcessDTO> orderProcessDTOS;
        if(status != null){
            orderProcessDTOS = orderService.getOrderProcessByStatus(status);
        }else {
            orderProcessDTOS = orderService.getOrderProcess();
        }
        model.addAttribute("currentStatus", status);
        model.addAttribute("orderProcessDTOS", orderProcessDTOS);
        model.addAttribute("username", username);
        return "/employee/employee";
    }
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam(name = "orderId") Integer orderId) {
        orderService.updateToNextStatus(orderId);
        return  "redirect:/manager/orders";
    }
    @GetMapping("/order-details")
    public String orderDetails(@RequestParam(name = "orderId") Integer orderId, Model model) {
        OrderDetailsWrapperDTO orderDetailsWrapperDTO = orderService.getOrderDetailsForEmployee(orderId);

        model.addAttribute("orderDetails",orderDetailsWrapperDTO);
        return "/employee/orderDetails";
    }
}
