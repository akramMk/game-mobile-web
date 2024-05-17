package fr.univlyon1.m1if.m1if13.users.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Classe qui centralise les opérations de validation et de génération d'un token "métier", c'est-à-dire dédié à cette application.
 *
 * @author Lionel Médini
 */
public final class JwtHelper {
    private static final String SECRET = "monsecret2024";
    private static final String ISSUER = "MIF-13 2024";
    private static final long LIFETIME = 1800000; // Durée de vie d'un token : 30 minutes ; vous pouvez le modifier pour tester
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    /**
     * Pour valider la règle <code>HideUtilityClassConstructorCheck</code> de CheckStyle...
     * Une classe utilitaire qui n'a que des méthodes statiques ne doit pas avoir de constructeur public, parce qu'il ne servirait à rien de l'instancier.
     */
    private JwtHelper() {
        throw new UnsupportedOperationException();
    }


    /**
     * Vérifie l'authentification d'un utilisateur grâce à un token JWT.
     *
     * @param token le token à vérifier
     * @param origin   la requête HTTP (nécessaire pour vérifier si l'origine de la requête est la même que celle du token
     * @return un booléen qui indique si le token est bien formé et valide (pas expiré) et si l'utilisateur est authentifié
     */
    public static String verifyToken(String token, @NotNull String origin) throws NullPointerException, JWTVerificationException {
        JWTVerifier authenticationVerifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .withAudience(origin) // Non-reusable verifier instance
                .build();

        authenticationVerifier.verify(token); // Lève une NullPointerException si le token n'existe pas, et une JWTVerificationException s'il est invalide
        DecodedJWT jwt = JWT.decode(token); // Pourrait lever une JWTDecodeException mais comme le token est vérifié avant, cela ne devrait pas arriver
        return jwt.getClaim("sub").asString();
    }

    /**
     * Crée un token avec les caractéristiques de l'utilisateur.
     *
     * @param subject le login de l'utilisateur
     * @param origin     l'origine de la requete
     * @return le token signé
     * @throws JWTCreationException si les paramètres ne permettent pas de créer un token
     */
    public static String generateToken(String subject, String origin) throws JWTCreationException {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withAudience(origin)
                .withExpiresAt(new Date(new Date().getTime() + LIFETIME))
                .sign(ALGORITHM);
    }

}
