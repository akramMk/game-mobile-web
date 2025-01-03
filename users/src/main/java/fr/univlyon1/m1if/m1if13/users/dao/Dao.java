package fr.univlyon1.m1if.m1if13.users.dao;

import java.util.Optional;
import java.util.Set;

public interface Dao<T> {
    /**
     * Récupère un utilisateur enregistré
     * @param id Login de l'utilisateur
     * @return Un java.util.Optional qui contient (ou pas) l'utilisateur
     */
    Optional<T> get(String id);

    /**
     * Récupère tous les utilisateurs enregistrés
     * @return Un Set de login
     */
    Set<String> getAll();

    /**
     * Crée un utilisateur et le sauvegarde
     * @param t L'utilisateur à créer
     */
    void save(T t);

    /**
     * Modifie un utilisateur enregistré
     * @param t L'utilisateur à modifier
     * @param params Un tableau de 2 Strings : login et password
     */
    void update(T t, String[] params);

    /**
     * Supprime un utilisateur enregistré
     * @param t L'utilisateur à supprimer
     */
    void delete(T t);
}