package ma.mundiapolis.tp3.web;

import ma.mundiapolis.tp3.entities.Product;
import ma.mundiapolis.tp3.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    // Liste des produits avec pagination
    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("listProducts", products.getContent());
        model.addAttribute("pages", new int[products.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "products";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, @RequestParam(defaultValue = "0") int page) {
        productRepository.deleteById(id);
        return "redirect:/index?page=" + page;
    }
}
