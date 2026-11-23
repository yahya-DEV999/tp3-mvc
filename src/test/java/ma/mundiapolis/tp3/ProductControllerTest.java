package ma.mundiapolis.tp3;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void anonymousIsRedirectedToLogin() throws Exception {
        mvc.perform(get("/user/index")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void loginPageRenders() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Authentification")));
    }

    @Test
    void userSeesProductList() throws Exception {
        mvc.perform(get("/user/index").with(user("user1").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Computer")))
                .andExpect(content().string(containsString("Liste des produits")));
    }

    @Test
    void searchFilters() throws Exception {
        mvc.perform(get("/user/index").param("keyword", "smart").with(user("user1").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Smart Watch")));
    }

    @Test
    void userCannotOpenAdminPage() throws Exception {
        mvc.perform(get("/admin/newProduct").with(user("user1").roles("USER")))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/notAuthorized"));
    }

    @Test
    void adminFormPagesRender() throws Exception {
        mvc.perform(get("/admin/newProduct").with(user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk());
        mvc.perform(get("/admin/editProduct").param("id", "1").with(user("admin").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Mettre à jour")));
    }

    @Test
    void invalidProductShowsErrors() throws Exception {
        mvc.perform(post("/admin/saveProduct").with(csrf()).with(user("admin").roles("USER", "ADMIN"))
                        .param("name", "a").param("price", "-5").param("quantity", "0"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("product", "name", "price", "quantity"));
    }

    @Test
    void adminCanSaveAndDelete() throws Exception {
        mvc.perform(post("/admin/saveProduct").with(csrf()).with(user("admin").roles("USER", "ADMIN"))
                        .param("name", "Tablet").param("price", "2000").param("quantity", "3"))
                .andExpect(redirectedUrl("/user/index"));
        mvc.perform(post("/admin/delete").with(csrf()).with(user("admin").roles("USER", "ADMIN"))
                        .param("id", "6"))
                .andExpect(status().is3xxRedirection());
    }
}
