package fr.univlyon1.m1if.m1if13.users.controller;

import fr.univlyon1.m1if.m1if13.users.dao.Dao;
import fr.univlyon1.m1if.m1if13.users.classes.User;
import fr.univlyon1.m1if.m1if13.users.dto.UserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;


@Controller
@RequestMapping("/userRessources")
public class UserResourcesController {

    private final Dao<User> userCollection;

    @Autowired
    public UserResourcesController(Dao<User> userCollection) {
        this.userCollection = userCollection;
    }

    @GetMapping( produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Get users' list", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"),
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/xml"),
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "text/html")
            })
    })
    @ResponseBody
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://localhost:5173"})
    public Set<String> getUsers(){
        return userCollection.getAll();
    }

    @GetMapping( produces = {MediaType.TEXT_HTML_VALUE} )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://localhost:5173"})
    public String getUsers(Model model) {
        model.addAttribute("users", userCollection.getAll());
        return "users";
    }

    @GetMapping(path = "/{login}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Get user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"),
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/xml"),
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "text/html")
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {}),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Access Denied")
    })
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://localhost:5173"})
    @ResponseBody
    public User getUser(@PathVariable("login") String login){
        Optional<User> userOptional = userCollection.get(login);
        if (userOptional.isPresent()){
            return userOptional.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping(path = "/{login}", produces = {MediaType.TEXT_HTML_VALUE})
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://localhost:5173"})
    public String getUser(@PathVariable("login") String login, Model model) {
        Optional<User> userOptional = userCollection.get(login);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            model.addAttribute("user", user);
            return "user";
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE},
                 produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Create user", responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"),
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/xml")
            }),
            @ApiResponse(responseCode = "409", description = "Login already taken", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"),
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/xml")
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Access Denied")
    })
    @ResponseBody
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://localhost:5173"})
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userCollection.save(user);
            return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
        }
        catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login already taken");
        }
    }

    @PostMapping(consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE},
                 produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Create user", responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"),
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/xml")
            }),
            @ApiResponse(responseCode = "409", description = "Login already taken", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"),
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/xml")
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Access Denied")
    })
    @ResponseBody
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://localhost:5173"})
    public ResponseEntity<String> registerUrlEncoded(@ModelAttribute("user") UserRequestDto userDto) {
        try {
            User newUser = new User(userDto.getLogin(), userDto.getSpecies(), userDto.getPassword());
            userCollection.save(newUser);
            return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
        }
        catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login already taken");
        }
    }

    @DeleteMapping(path = "/{login}")
    @Operation(summary = "Delete user", responses = {
            @ApiResponse(responseCode = "202", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Access Denied")
    })
    @ResponseBody
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://localhost:5173"})
    public ResponseEntity<String> delete(@PathVariable("login") String login){
        Optional<User> userOptional = userCollection.get(login);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userCollection.delete(user);
            return new ResponseEntity<>("User is Deleted successfully",HttpStatus.ACCEPTED);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PutMapping(path = "/{login}",
            consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Update user", responses = {
            @ApiResponse(responseCode = "202", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Access Denied")
    })
    @ResponseBody
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://localhost:5173"})
    public ResponseEntity<String> updateUrlEncoded(
            @PathVariable("login") String login,
            @ModelAttribute("user") UserRequestDto body){
        Optional<User> userOptional = userCollection.get(login);
        if (userOptional.isPresent()) {
            User user = userOptional.get(); // Access the User object safely
            System.out.println("the userDto.getPassword() = "+ body.getPassword());
            System.out.println("the userDto.getSpecie() = "+ body.getSpecies().name());
            String[] params = {login, body.getPassword()};
            userCollection.update(user, params);
            return new ResponseEntity<>("User is updated successfully", HttpStatus.ACCEPTED);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PutMapping(path = "/{login}",
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Update user", responses = {
            @ApiResponse(responseCode = "202", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Access Denied")
    })
    @ResponseBody
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3376", "http://192.168.75.14:8080", "https://192.168.75.14", "http://localhost:5173"})
    public ResponseEntity<String> update(
            @PathVariable("login") String login,
            @RequestBody UserRequestDto userDto){
        Optional<User> userOptional = userCollection.get(login);
        if (userOptional.isPresent()) {
            User user = userOptional.get(); // Access the User object safely
            String[] params = {login, userDto.getPassword()};
            userCollection.update(user, params);
            return new ResponseEntity<>("User is updated successfully", HttpStatus.ACCEPTED);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}