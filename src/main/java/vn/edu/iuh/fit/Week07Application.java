package vn.edu.iuh.fit;

import net.datafaker.providers.base.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.edu.iuh.fit.backend.enums.EmployeeStatus;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.*;
import vn.edu.iuh.fit.backend.repositories.*;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication(scanBasePackages = "vn.edu.iuh.fit")
public class Week07Application {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;
@Autowired
	private ProductPriceRepository productPriceRepository;

	@Autowired
	private ProductImageRepository productImageRepository;
	public static void main(String[] args) {

		SpringApplication.run(Week07Application.class, args);
	}

//		@Bean
//	CommandLineRunner commandLineRunner(){
//
//        return args -> {
//			for (int i = 0; i < 200; i++){
//				Product product = new Product();
//				Faker faker = new Faker();
//				product.setName(faker.commerce().productName());
//				product.setDescription(faker.lorem().characters(30));
//				product.setManufacturer(faker.company().name());
//				product.setUnit(faker.commerce().material());
//				product.setStatus(ProductStatus.ACTIVE);
//				ProductPrice productPrice = new ProductPrice(product, LocalDateTime.of(2023, 11, 30, 10, 0), i + 200 , "");
//				ProductImage productImage = new ProductImage(faker.internet().image(), faker.lorem().characters(10));
//				product.addProductPrice(productPrice);
//				product.addProductImage(productImage);
//				productRepository.save(product);
//			}
//
//			for (int i = 0; i < 200; i++){
//				Employee employee = new Employee();
//				Faker faker = new Faker();
//				employee.setFullname(faker.name().fullName());
//				employee.setDob(LocalDate.of(2002, 8, 12));
//				employee.setEmail(faker.lorem().characters(10, 15));
//				employee.setPhone(faker.phoneNumber().subscriberNumber(10));
//				employee.setAddress(faker.address().fullAddress());
//				employee.setStatus(EmployeeStatus.ACTIVE);
//
//				employeeRepository.save(employee);
//			}
//        };
//    }


}
