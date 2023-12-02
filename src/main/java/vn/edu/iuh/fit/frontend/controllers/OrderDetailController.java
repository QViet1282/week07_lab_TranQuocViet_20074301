package vn.edu.iuh.fit.frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.iuh.fit.backend.models.OrderDetail;
import vn.edu.iuh.fit.backend.repositories.OrderDetailRepository;
import vn.edu.iuh.fit.backend.repositories.ProductPriceRepository;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;

@Controller
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;
    @PostMapping("/add/{id}")
    public String addOrderDetail(@PathVariable("id") long id){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(productRepository.findById(id).get());
        orderDetail.setPrice(productPriceRepository.findNearestPrice(id).get().getPrice());
        orderDetail.setQuantity(1);
        orderDetailRepository.save(orderDetail);

        return "product/listProduct";
    }
}
