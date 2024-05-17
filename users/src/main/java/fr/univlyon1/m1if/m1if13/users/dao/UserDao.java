package fr.univlyon1.m1if.m1if13.users.dao;

import fr.univlyon1.m1if.m1if13.users.classes.Species;
import fr.univlyon1.m1if.m1if13.users.classes.User;
import fr.univlyon1.m1if.m1if13.users.dao.Dao;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
//public class UserDao implements Dao<User> {
//
//    private final List<User> users = new ArrayList<>();
//
////    public UserDao() {
////    }
//
//    @Override
//    public Optional<User> get(String login) {
//        return Optional.ofNullable(users.get(login));
//    }
//
//    @Override
//    public Set<String> getAll() {
//        Set<String> logins = new HashSet<>();
//        users.forEach(user -> logins.add(user.getLogin()));
//        return logins;
//
//    }
//
//    @Override
//    public void save(User user) {
//        users.add(user);
//    }
//
//    @Override
//    public void update(User user, String[] params) {
//        user.setPassword(Objects.requireNonNull(
//                params[1], "Password cannot be null"));
//        user.setImage(Objects.requireNonNull(
//                params[3], "Image cannot be null"));
//
//        users.add(user);
//    }
//
//    @Override
//    public void delete(User user) {
//        users.remove(user);
//    }
//}


@Repository // Cette annotation déclare UserDao comme un bean Spring
public class UserDao implements Dao<User> {
    // Utilisation d'une HashMap pour stocker les utilisateurs avec leur login comme clé
    private final Map<String, User> userMap;

    public UserDao() {
        userMap = new HashMap<>();
        // Initialize the map with two users
        userMap.put("akram", new User("akram", Species.ADMIN, "password1"));
        userMap.put("user2", new User("user2", Species.VILLAGEOIS, "password2"));
    }

    @Override
    public Optional<User> get(String login) {
        return Optional.ofNullable(userMap.get(login));
    }

    @Override
    public Set<String> getAll() {
        return userMap.keySet();
    }

    @Override
    public void save(User user) {
        Optional<User> userOptional = Optional.ofNullable(userMap.get(user.getLogin()));
        if (userOptional.isPresent()){
            throw new IllegalStateException("login already TOKEN !!");
        }
        else {
            userMap.put(user.getLogin(), user);
        }
    }

    @Override
    public void update(User user, String[] params) {
        // Suppose que les paramètres sont un tableau de deux chaînes : login et password
        if (params.length == 2) {
            user.setPassword(params[1]);
            userMap.replace(params[0], user);
        }
    }

    @Override
    public void delete(User user) {
        userMap.remove(user.getLogin());
    }
}
