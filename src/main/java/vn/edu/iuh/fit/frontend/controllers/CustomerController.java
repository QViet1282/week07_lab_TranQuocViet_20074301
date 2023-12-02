package vn.edu.iuh.fit.frontend.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.Customer;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.models.ProductPrice;
import vn.edu.iuh.fit.backend.repositories.CustomerRepository;
import vn.edu.iuh.fit.backend.repositories.ProductPriceRepository;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;


    @GetMapping("/{id}/listProduct")
    public String list(HttpSession httpSession, Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "20") int size, @PathVariable("id") long id){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Product> products = productRepository.findAllByStatus(ProductStatus.ACTIVE, pageRequest);
        for (Product product : products) {
            List<ProductPrice> productPrices = new ArrayList<>();
            productPrices.add(productPriceRepository.findNearestPrice(product.getProduct_id()).get());
            product.setProductPrices(productPrices);
        }
        model.addAttribute("products", products);
        Page<Product> productPage = productRepository.findAll(pageRequest);
        model.addAttribute("posts", productPage);
        model.addAttribute("paginatedList", productPage);
        model.addAttribute("userId", id);
        httpSession.setAttribute("user", customerRepository.findById(id).get());

        return "customer/listProduct";
    }

    @GetMapping("/add/{id}")
    public String addOrderDetail(@PathVariable("id") long id,HttpSession httpSession){
        List<Product> cart = (List<Product>) httpSession.getAttribute("cart");
        if(cart == null){
            cart = new ArrayList<Product>();
        }
        cart.add(productRepository.findById(id).get());
        httpSession.setAttribute("cart", cart);
        System.out.println(cart);
        return "redirect:/customer/"+((Customer)httpSession.getAttribute("user")).getId()+"/listProduct";
    }

    @GetMapping("/{id}/cart")
    public String cart(@PathVariable("id") long id,HttpSession httpSession, Model model){


        List<Product> cart = (List<Product>) httpSession.getAttribute("cart");
        for (Product product : cart) {
            List<ProductPrice> productPrices = new ArrayList<>();
            productPrices.add(productPriceRepository.findNearestPrice(product.getProduct_id()).get());
            product.setProductPrices(productPrices);
        }
        model.addAttribute("cart", cart);
        model.addAttribute("userId", id);
        return "customer/cart";
    }
}
