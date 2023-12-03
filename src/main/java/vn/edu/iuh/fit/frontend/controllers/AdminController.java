package vn.edu.iuh.fit.frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backend.enums.EmployeeStatus;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.*;
import vn.edu.iuh.fit.backend.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public String dashboard(){
        return "admin/dashBoard";
    }

    @GetMapping("/listProduct")
    public String listProduct(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "size", defaultValue = "20") int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Product> products = productRepository.findAllByStatus(ProductStatus.ACTIVE, pageRequest);
        model.addAttribute("products", products);
        model.addAttribute("paginatedList", products);
        return "admin/listProduct";
    }

    @GetMapping("/listProduct/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id){
        Product product = productRepository.findById(id).get();
        product.setStatus(ProductStatus.IN_ACTIVE);
        productRepository.save(product);
        return "redirect:/admin/listProduct";
    }

    @GetMapping("/listProduct/edit/{id}")
    public String editProduct(@PathVariable("id") long id, Model model){
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        return "admin/editProduct";
    }

    @PostMapping("/listProduct/edit/{id}")
    public String editProduct(@PathVariable("id") long id, @ModelAttribute("product") Product product){
        Product product1 = productRepository.findById(id).get();
        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        product1.setManufacturer(product.getManufacturer());
        product1.setUnit(product.getUnit());
        productRepository.save(product1);
        return "redirect:/admin/listProduct";
    }

    @PostMapping("/listProduct/addPrice/{id}")
    public String addPrice(@PathVariable("id") long id, @RequestParam("price") double price, @RequestParam("note") String note) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));

        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct(product);
        productPrice.setPrice(price);
        productPrice.setNote(note);
        productPrice.setPrice_date_time(LocalDateTime.now());

        productPriceRepository.save(productPrice);

        return "redirect:/admin/listProduct";
    }

    @GetMapping("/listProduct/addProduct")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        return "admin/newProduct";
    }

    @PostMapping("/listProduct/addProduct")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("price") double price, @RequestParam("note") String note){
        product.setStatus(ProductStatus.ACTIVE);
        productRepository.save(product);
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct(product);
        productPrice.setPrice(price);
        productPrice.setNote(note);
        productPrice.setPrice_date_time(LocalDateTime.now());
        productPriceRepository.save(productPrice);
        return "redirect:/admin/listProduct";
    }

    @GetMapping("/listEmployee")
    public String listEmployee(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                               @RequestParam(name = "size", defaultValue = "20") int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Employee> employees = employeeRepository.findAllByStatus(EmployeeStatus.ACTIVE, pageRequest);
        model.addAttribute("employees", employees);
        model.addAttribute("paginatedList", employees);
        return "admin/listEmployee";
    }

    @GetMapping("/listEmployee/delete/{id}")
    public String deleteEmployee(@PathVariable("id") long id){
        Employee employee = employeeRepository.findById(id).get();
        employee.setStatus(EmployeeStatus.IN_ACTIVE);
        employeeRepository.save(employee);
        return "redirect:/admin/listEmployee";
    }

    @GetMapping("/listEmployee/edit/{id}")
    public String editEmployee(@PathVariable("id") long id, Model model){
        Employee employee = employeeRepository.findById(id).get();
        model.addAttribute("employee", employee);
        return "admin/editEmployee";
    }

    @PostMapping("/listEmployee/edit/{id}")
    public String editEmployee(@RequestParam("dob") String dobString,@PathVariable("id") long id, @ModelAttribute("employee") Employee employee){
        LocalDate dob = LocalDate.parse(dobString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Employee employee1 = employeeRepository.findById(id).get();
        employee1.setFullname(employee.getFullname());
        employee1.setAddress(employee.getAddress());
        employee1.setDob(dob);
        employee1.setPhone(employee.getPhone());
        employee1.setEmail(employee.getEmail());
        employeeRepository.save(employee1);
        return "redirect:/admin/listEmployee";
    }

    @GetMapping("/listEmployee/addEmployee")
    public String addEmployee(Model model){
        model.addAttribute("employee", new Employee());
        return "admin/newEmployee";
    }

    @PostMapping("/listEmployee/addEmployee")
    public String addEmployee(@RequestParam("dob") String dobString, @ModelAttribute("employee") Employee employee){
        LocalDate dob = LocalDate.parse(dobString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setDob(dob);
        employeeRepository.save(employee);
        return "redirect:/admin/listEmployee";
    }
    @GetMapping("/listCustomer")
    public String listCustomer(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                               @RequestParam(name = "size", defaultValue = "20") int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Customer> customers = customerRepository.findAll(pageRequest);
        model.addAttribute("customers", customers);
        Page<Customer> customerPage = customerRepository.findAll(pageRequest);
        model.addAttribute("paginatedList", customerPage);
        return "admin/listCustomer";
    }

    @GetMapping("/listCustomer/delete/{id}")
    public String deleteCustomer(@PathVariable("id") long id){
        Customer customer = customerRepository.findById(id).get();

        customerRepository.delete(customer);
        return "redirect:/admin/listCustomer";
    }

    @GetMapping("/listCustomer/edit/{id}")
    public String editCustomer(@PathVariable("id") long id, Model model){
        Customer customer = customerRepository.findById(id).get();
        model.addAttribute("customer", customer);
        return "admin/editCustomer";
    }

    @PostMapping("/listCustomer/edit/{id}")
    public String editCustomer(@PathVariable("id") long id, @ModelAttribute("customer") Customer customer){
        Customer customer1 = customerRepository.findById(id).get();
        customer1.setName(customer.getName());
        customer1.setAddress(customer.getAddress());
        customer1.setEmail(customer.getEmail());
        customer1.setPhone(customer.getPhone());
        customerRepository.save(customer1);
        return "redirect:/admin/listCustomer";
    }

    @GetMapping("/listCustomer/addCustomer")
    public String addCustomer(Model model){
        model.addAttribute("customer", new Customer());
        return "admin/newCustomer";
    }

    @PostMapping("/listCustomer/addCustomer")
    public String addCustomer(@ModelAttribute("customer") Customer customer){
        customerRepository.save(customer);
        return "redirect:/admin/listCustomer";
    }
}
