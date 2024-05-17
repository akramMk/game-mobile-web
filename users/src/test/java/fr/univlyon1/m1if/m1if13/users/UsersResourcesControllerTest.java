package fr.univlyon1.m1if.m1if13.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersResourcesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userFlowTest() throws Exception {
        // Etape 1 : Création d'un utilisateur
        String login = "test";
        String password = "test";
        String userJson = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}";
        mockMvc.perform(post("/userRessources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        // Etape 2 : Récupération de la liste des utilisateurs
        mockMvc.perform(get("/userRessources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));

//        // Etape 3 : Connexion
//        String token = mockMvc.perform(post("/login?login=" + login + "&password=" + password))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getHeader("Authorization");
//
//        System.out.println("E3 validée");
//        // Etape 4 : Récupération de l'utilisateur connecté
//        mockMvc.perform(get("/users/" + login)
//                        .header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.login").value(login));
//        System.out.println("E4 validée");
    }


}
