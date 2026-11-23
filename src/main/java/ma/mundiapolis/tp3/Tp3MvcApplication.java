package ma.mundiapolis.tp3;

import ma.mundiapolis.tp3.entities.Product;
import ma.mundiapolis.tp3.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

// Sécurité désactivée temporairement pendant le développement des vues
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Tp3MvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tp3MvcApplication.class, args);
    }

    // Test de la couche DAO : insertion de quelques produits au démarrage
    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder().name("Computer").price(4500).quantity(10).build());
            productRepository.save(Product.builder().name("Printer").price(1200).quantity(4).build());
            productRepository.save(Product.builder().name("Smartphone").price(3200).quantity(25).build());
            productRepository.save(Product.builder().name("Smart Watch").price(900).quantity(7).build());
            productRepository.save(Product.builder().name("Keyboard").price(250).quantity(30).build());
            productRepository.save(Product.builder().name("Mouse").price(120).quantity(50).build());
            productRepository.findAll().forEach(System.out::println);
        };
    }
}
