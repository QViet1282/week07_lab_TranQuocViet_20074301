package vn.edu.iuh.fit.frontend.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.*;
import vn.edu.iuh.fit.backend.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    @GetMapping("/{id}/listProduct")
    public String list(HttpSession httpSession, Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "20") int size, @PathVariable("id") long id){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Product> products = productRepository.findAllByStatus(ProductStatus.ACTIVE, pageRequest);
//        for (Product product : products) {
////            product.addProductPrice(productPriceRepository.findTopByProductProductPricesOrderByPrice_date_timeDesc(product.getProduct_id()).get());
//        }
        model.addAttribute("products", products);
        Page<Product> productPage = productRepository.findAll(pageRequest);
        model.addAttribute("paginatedList", productPage);
        model.addAttribute("userId", id);
        httpSession.setAttribute("user", customerRepository.findById(id).get());

        return "customer/listProduct";
    }

    @GetMapping("/add/{id}")
    public String addOrderDetail(@PathVariable("id") long id, HttpSession httpSession) {
        Order order = httpSession.getAttribute("order") == null ? new Order() : (Order) httpSession.getAttribute("order");
//        order.setCustomer((Customer) httpSession.getAttribute("user"));
        List<OrderDetail> orderDetails = order.getOrderDetails() == null ? new ArrayList<>() : order.getOrderDetails();


        Optional<OrderDetail> existingOrderDetail = orderDetails.stream()
                .filter(detail -> detail.getProduct().getProduct_id() == id)
                .findFirst();

        if (existingOrderDetail.isPresent()) {
            OrderDetail orderDetail = existingOrderDetail.get();
            orderDetail.setQuantity(orderDetail.getQuantity() + 1);
        } else {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(productRepository.findById(id).get());
            orderDetail.setPrice(productRepository.findById(id).get().getCurrentPrice().get().getPrice());
            orderDetail.setQuantity(1);
            orderDetail.setNote("");
            orderDetails.add(orderDetail);

        }

        order.setOrderDetails(orderDetails);
        httpSession.setAttribute("order", order);

        return "redirect:/customer/" + ((Customer) httpSession.getAttribute("user")).getId() + "/listProduct";
    }


    @GetMapping("/{id}/cart")
    public String cart(@PathVariable("id") long id,HttpSession httpSession, Model model){


        Order order = httpSession.getAttribute("order") == null ? new Order() : (Order) httpSession.getAttribute("order");
        double total = order.getOrderDetails().stream().mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity()).sum();
        model.addAttribute("total", total);
        model.addAttribute("order", order);
        model.addAttribute("userId", id);
        return "customer/cart";
    }

    @GetMapping("/increaseQuantity/{id}")
    public String increaseQuantity(@PathVariable("id") long id, HttpSession httpSession){
        Order order = httpSession.getAttribute("order") == null ? new Order() : (Order) httpSession.getAttribute("order");
        List<OrderDetail> orderDetails = order.getOrderDetails() == null ? new ArrayList<>() : order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            if(orderDetail.getProduct().getProduct_id() == id){
                orderDetail.setQuantity(orderDetail.getQuantity() + 1);
            }
        }
        order.setOrderDetails(orderDetails);
        httpSession.setAttribute("order", order);
        return "redirect:/customer/"+((Customer)httpSession.getAttribute("user")).getId()+"/cart";
    }

    @GetMapping("/decreaseQuantity/{id}")
    public String decreaseQuantity(@PathVariable("id") long id, HttpSession httpSession){
        Order order = httpSession.getAttribute("order") == null ? new Order() : (Order) httpSession.getAttribute("order");
        List<OrderDetail> orderDetails = order.getOrderDetails() == null ? new ArrayList<>() : order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            if(orderDetail.getProduct().getProduct_id() == id){
                if (orderDetail.getQuantity() > 1)
                    orderDetail.setQuantity(orderDetail.getQuantity() - 1);
            }
        }
        order.setOrderDetails(orderDetails);
        httpSession.setAttribute("order", order);
        return "redirect:/customer/"+((Customer)httpSession.getAttribute("user")).getId()+"/cart";
    }

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(@PathVariable("id") long id, HttpSession httpSession){
        Order order = httpSession.getAttribute("order") == null ? new Order() : (Order) httpSession.getAttribute("order");
        List<OrderDetail> orderDetails = order.getOrderDetails() == null ? new ArrayList<>() : order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            if(orderDetail.getProduct().getProduct_id() == id){
                orderDetails.remove(orderDetail);
                break;
            }
        }
        order.setOrderDetails(orderDetails);
        httpSession.setAttribute("order", order);
        return "redirect:/customer/"+((Customer)httpSession.getAttribute("user")).getId()+"/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession httpSession){
        Order order = httpSession.getAttribute("order") == null ? new Order() : (Order) httpSession.getAttribute("order");
        order.setCustomer((Customer) httpSession.getAttribute("user"));
        order.setOrderDate(java.time.LocalDateTime.now());
        order.setEmployee(employeeRepository.findById(1L).get());
        orderRepository.save(order);
        httpSession.removeAttribute("order");
        return "redirect:/customer/"+((Customer)httpSession.getAttribute("user")).getId()+"/listProduct";
    }
}
