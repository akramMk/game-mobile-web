package fr.univlyon1.m1if.m1if13.users.dto;

import fr.univlyon1.m1if.m1if13.users.classes.Species;

public class UserRequestDto {
    private String login;
    private Species species;
    private String password;
    private String image;

    // No-argument constructor
    public UserRequestDto() {
    }

    // Constructor initializing all attributes
    public UserRequestDto(String login, Species species, String password, String image) {
        this.login = login;
        this.species = species;
        this.password = password;
        this.image = image;
    }

    // Getters and setters for all attributes
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
