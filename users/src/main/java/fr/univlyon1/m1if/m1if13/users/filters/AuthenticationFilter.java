package fr.univlyon1.m1if.m1if13.users.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.auth0.jwt.exceptions.JWTVerificationException;
import fr.univlyon1.m1if.m1if13.users.utils.JwtHelper;
import org.springframework.stereotype.Component;

/**
 * Filtre d'authentification.
 * Vérifie qu'une requête est authentifiée, sauf requêtes autorisées.<br>
 * Autorise les requêtes suivantes :
 * <ol>
 *     <li>URLs ne nécessitant pas d'authentification (whitelist), y compris les requêtes d'authentification</li>
 *     <li>Requêtes d'utilisateurs déjà authentifiés</li>
 * </ol>
 * Dans les cas contraires, renvoie un code d'erreur HTTP 401 (Unauthorized).
 *
 * @author Lionel Médini
 */

@Component
//@Order(1)
@WebFilter
public class AuthenticationFilter extends HttpFilter {
    private static final String[] WHITELIST = {"/", "/authenticate","/index.html",
            "/userRessources", "/userRessources/", "/login", "/logout" , "/api-docs", "/api-docs.yaml",
            "/users/userRessources", "/swagger-ui.html", "/swagger-ui/"};

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Permet de retrouver la fin de l'URL (après l'URL du contexte) -> indépendant de l'URL de déploiement
        String url = request.getRequestURI().replace(request.getContextPath(), "");

        // 1) Laisse passer les URLs ne nécessitant pas d'authentification
        for(String tempUrl: WHITELIST) {
            if(url.equals(tempUrl) || url.contains("/swagger")) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 2) Traite les requêtes qui doivent être authentifiées
        // Note :
        //   le paramètre false dans request.getSession(false) permet de récupérer null si la session n'est pas déjà créée.
        //   Sinon, l'appel de la méthode getSession() la crée automatiquement.
        // 2) Traite les requêtes qui doivent être authentifiées
        String token = request.getHeader("Authorization"); // Example: Header "Authorization: Bearer your_token_here"
        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("Token missing !");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Token is missing. Vous devez vous connecter pour accéder au site.");
            return;
        } else {
                try {
                    String JWT = token.replace("Bearer ", "");

                    // Validate the token using the TodosM1if03JwtHelper
                    if (JwtHelper.verifyToken(JWT, request.getHeader("Origin")) != null) {
                        // Assuming the token contains user information, you can set it as a request
                        // attribute
//                        request.setAttribute("token", token.substring(7));
                        request.setAttribute("token",JWT);
                        chain.doFilter(request, response);
                        return;
                    }

                } catch (JWTVerificationException e) {
                    // Token is invalid
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid. Vous devez vous connecter pour accéder au site.");
                    return;
                }

        }
        System.out.println("Tentative blockée!");
        // 3) Bloque les autres requêtes
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez vous connecter pour accéder au site.");
    }
}
