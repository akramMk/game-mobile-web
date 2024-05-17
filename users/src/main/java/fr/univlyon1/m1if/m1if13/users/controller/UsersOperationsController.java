package fr.univlyon1.m1if.m1if13.users.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import fr.univlyon1.m1if.m1if13.users.dao.Dao;
import fr.univlyon1.m1if.m1if13.users.classes.User;
import fr.univlyon1.m1if.m1if13.users.utils.JwtHelper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Controller
public class UsersOperationsController {

    private final Dao<User> userCollection;

    @Autowired
    public UsersOperationsController(Dao<User> userCollection) {
        this.userCollection = userCollection;
    }

    /**
     * Procédure de login utilisée par un utilisateur
     * @param login Le login de l'utilisateur. L'utilisateur doit avoir été créé préalablement et son login doit être présent dans le DAO.
     * @param password Le password à vérifier.
     * @return Une ResponseEntity avec le JWT dans le header "Authentication" si le login s'est bien passé, et le code de statut approprié (204, 401 ou 404).
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login an user", tags = {"users"},
            responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204",
                    description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "User not found")
    })
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://192.168.75.14:3376", "https://192.168.75.14/game", "http://localhost:5173"})
    public ResponseEntity<Void> login(@RequestParam("login") String login,
                                      @RequestParam("password") String password,
                                      @RequestHeader("Origin") String origin)
            throws AuthenticationException {
        Optional<User> optionalUser = userCollection.get(login);
        //System.out.println("###### In Login ######");
        if (optionalUser.isPresent() && optionalUser.get().authenticate(password)) {
            try {
                String token = JwtHelper.generateToken(login, origin);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + token);
                headers.add("Access-Control-Expose-Headers", "Authorization"); // Expose the Authorization header
                return ResponseEntity.status(HttpStatus.NO_CONTENT).
                        headers(headers).
                        build();

            } catch (JWTCreationException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Réalise la déconnexion
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout an user", tags = {"users"},
            responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204",
                    description = "Logout successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                    description = "Unauthorized")
    })
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://192.168.75.14:3376", "https://192.168.75.14/game", "http://localhost:5173"})
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String jwt,
                                       @RequestHeader("Origin") String origin) {
        try {
            String token = jwt.replace("Bearer ", "");
            String login = JwtHelper.verifyToken(token, origin);
            userCollection.get(login).ifPresent(User::disconnect);
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Méthode destinée au serveur Node pour valider l'authentification d'un utilisateur.
     * @param jwt Le token JWT qui se trouve dans le header "Authentication" de la requête
     * @param origin L'origine de la requête (pour la comparer avec celle du client, stockée dans le token JWT)
     * @return Une réponse vide avec un code de statut approprié (204, 400, 401).
     */
    @GetMapping("/authenticate")
    @Operation(summary = "Authenticate", description = "Authenticate an user", tags = {"users"},
            responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204",
                    description = "Authentication successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Bad request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                    description = "Unauthorized")
    })
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14" , "http://192.168.75.14:3376", "https://192.168.75.14/game", "http://localhost:5173"})
    public ResponseEntity<Void> authenticate(@RequestParam("Authorization") String jwt, @RequestParam("Origin") String origin) {
        // Extraire le token du header Authorization
        String token = jwt.replace("Bearer ", "");
        try {
            JwtHelper.verifyToken(token, origin);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (JWTVerificationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}