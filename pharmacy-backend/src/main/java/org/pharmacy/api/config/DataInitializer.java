/*
 * Author: Shady Ahmed
 * Date: 2025-09-27
 * Project: Delta Pharmacy API
 * My Linked-in: https://www.linkedin.com/in/shady-ahmed97/.
 */
package org.pharmacy.api.config;

import org.pharmacy.api.model.Product;
import org.pharmacy.api.model.User;
import org.pharmacy.api.repository.ProductRepository;
import org.pharmacy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Create default users
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setEmail("admin@pharmacy.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFullName("Admin User");
                admin.setPhone("1234567890");
                admin.setAddress("123 Admin St");
                admin.setRole(User.UserRole.ADMIN);
                userRepository.save(admin);

                User pharmacist = new User();
                pharmacist.setEmail("pharmacist@pharmacy.com");
                pharmacist.setPassword(passwordEncoder.encode("pharma123"));
                pharmacist.setFullName("John Pharmacist");
                pharmacist.setPhone("1234567891");
                pharmacist.setAddress("456 Pharma Ave");
                pharmacist.setRole(User.UserRole.PHARMACIST);
                userRepository.save(pharmacist);

                User customer = new User();
                customer.setEmail("customer@example.com");
                customer.setPassword(passwordEncoder.encode("customer123"));
                customer.setFullName("Jane Customer");
                customer.setPhone("1234567892");
                customer.setAddress("789 Customer Rd");
                customer.setRole(User.UserRole.CUSTOMER);
                userRepository.save(customer);

                System.out.println("✅ Default users created:");
                System.out.println("   Admin: admin@pharmacy.com / admin123");
                System.out.println("   Pharmacist: pharmacist@pharmacy.com / pharma123");
                System.out.println("   Customer: customer@example.com / customer123");
            }

            // Create sample products
            if (productRepository.count() == 0) {
                Product p1 = new Product();
                p1.setName("Paracetamol 500mg");
                p1.setDescription("Pain relief and fever reducer");
                p1.setPrice(5.99);
                p1.setCategory("Pain Relief");
                p1.setBrand("GenericPharm");
                p1.setImageUrl("https://example.com/paracetamol.jpg");
                p1.setPrescriptionRequired(false);
                p1.setStockQuantity(500);
                p1.setReorderLevel(50);
                productRepository.save(p1);

                Product p2 = new Product();
                p2.setName("Amoxicillin 250mg");
                p2.setDescription("Antibiotic for bacterial infections");
                p2.setPrice(12.99);
                p2.setCategory("Antibiotics");
                p2.setBrand("MediCorp");
                p2.setImageUrl("https://example.com/amoxicillin.jpg");
                p2.setPrescriptionRequired(true);
                p2.setStockQuantity(200);
                p2.setReorderLevel(30);
                productRepository.save(p2);

                Product p3 = new Product();
                p3.setName("Ibuprofen 400mg");
                p3.setDescription("Anti-inflammatory pain reliever");
                p3.setPrice(8.99);
                p3.setCategory("Pain Relief");
                p3.setBrand("PainAway");
                p3.setImageUrl("https://example.com/ibuprofen.jpg");
                p3.setPrescriptionRequired(false);
                p3.setStockQuantity(300);
                p3.setReorderLevel(40);
                productRepository.save(p3);

                Product p4 = new Product();
                p4.setName("Lisinopril 10mg");
                p4.setDescription("Blood pressure medication");
                p4.setPrice(15.99);
                p4.setCategory("Cardiovascular");
                p4.setBrand("HeartCare");
                p4.setImageUrl("https://example.com/lisinopril.jpg");
                p4.setPrescriptionRequired(true);
                p4.setStockQuantity(150);
                p4.setReorderLevel(25);
                productRepository.save(p4);

                Product p5 = new Product();
                p5.setName("Vitamin D3 1000IU");
                p5.setDescription("Vitamin supplement for bone health");
                p5.setPrice(9.99);
                p5.setCategory("Vitamins");
                p5.setBrand("HealthPlus");
                p5.setImageUrl("https://example.com/vitamind.jpg");
                p5.setPrescriptionRequired(false);
                p5.setStockQuantity(400);
                p5.setReorderLevel(50);
                productRepository.save(p5);

                Product p6 = new Product();
                p6.setName("Metformin 500mg");
                p6.setDescription("Diabetes medication");
                p6.setPrice(18.99);
                p6.setCategory("Diabetes");
                p6.setBrand("DiabeCare");
                p6.setImageUrl("https://example.com/metformin.jpg");
                p6.setPrescriptionRequired(true);
                p6.setStockQuantity(180);
                p6.setReorderLevel(30);
                productRepository.save(p6);

                Product p7 = new Product();
                p7.setName("Omeprazole 20mg");
                p7.setDescription("Acid reflux and heartburn relief");
                p7.setPrice(11.99);
                p7.setCategory("Digestive");
                p7.setBrand("StomachEase");
                p7.setImageUrl("https://example.com/omeprazole.jpg");
                p7.setPrescriptionRequired(false);
                p7.setStockQuantity(250);
                p7.setReorderLevel(35);
                productRepository.save(p7);

                Product p8 = new Product();
                p8.setName("Cetirizine 10mg");
                p8.setDescription("Allergy relief medication");
                p8.setPrice(7.99);
                p8.setCategory("Allergy");
                p8.setBrand("AllergyFree");
                p8.setImageUrl("https://example.com/cetirizine.jpg");
                p8.setPrescriptionRequired(false);
                p8.setStockQuantity(350);
                p8.setReorderLevel(45);
                productRepository.save(p8);

                Product p9 = new Product();
                p9.setName("Aspirin 75mg");
                p9.setDescription("Blood thinner and pain relief");
                p9.setPrice(6.99);
                p9.setCategory("Cardiovascular");
                p9.setBrand("HeartGuard");
                p9.setImageUrl("https://example.com/aspirin.jpg");
                p9.setPrescriptionRequired(false);
                p9.setStockQuantity(450);
                p9.setReorderLevel(50);
                productRepository.save(p9);

                Product p10 = new Product();
                p10.setName("Atorvastatin 20mg");
                p10.setDescription("Cholesterol lowering medication");
                p10.setPrice(22.99);
                p10.setCategory("Cardiovascular");
                p10.setBrand("CholesterolCare");
                p10.setImageUrl("https://example.com/atorvastatin.jpg");
                p10.setPrescriptionRequired(true);
                p10.setStockQuantity(120);
                p10.setReorderLevel(20);
                productRepository.save(p10);

                System.out.println("✅ Sample products created: " + productRepository.count() + " products");
            }
        };
    }
}
