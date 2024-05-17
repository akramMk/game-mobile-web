package fr.univlyon1.m1if.m1if13.users.classes;

import javax.naming.AuthenticationException;

public class User {
    private final String login;
    private Species species;
    private String password;
    // Permet d'invalider une connexion même si le token est toujours valide
    private boolean connected = false;
    // Nom du fichier image qui représentera l'utilisateur sur la carte
    private String image;

    public User() {
        this.login = "";
        this.species = null;
        this.password = "";
        this.image = "";
    }
    public User(String login, Species species, String password) {
        this.login = login;
        this.species = species;
        this.password = password;
    }

    public User(String login, Species species, String password, String image) {
        this.login = login;
        this.species = species;
        this.password = password;
        this.image = image;
    }

    public String getLogin() {
        return login;
    }


    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public boolean authenticate(String password) throws AuthenticationException {
        if(!verifyPassword(password)) {
            throw new AuthenticationException("Erroneous password");
        }
        this.connected = true;
        return true;
    }

    public void disconnect() {
        this.connected = false;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Vérifie un password par rapport à celui stocké dans l'instance.
     * @param password Le password à vérifier
     * @return un booléen indiquant le succès de la comparaison des 2 passwords
     */
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public String getPassword(){
        return this.password;
    }
}
