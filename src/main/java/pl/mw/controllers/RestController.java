package pl.mw.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import pl.mw.model.AppUser;
import pl.mw.repositories.UserRepository;
import pl.mw.repositories.UserRoleRepository;
import pl.mw.service.AddUserService;

import java.net.URI;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/users")
public class RestController {

    private UserRepository userRepository;
    private AddUserService addUserService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAddUserService(AddUserService addUserService) {
        this.addUserService = addUserService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppUser>> showAll() {
        List<AppUser> list = userRepository.findAll();
        return ResponseEntity.ok(list);


    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUser> getOne(@PathVariable Long id) {
        AppUser user = userRepository.findById(id).get();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody AppUser appUser) {
        AppUser savedUser = addUserService.addUserWithDefaultRole(appUser);
        if (appUser.getId() == null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedUser.getId())
                    .toUri();
            return ResponseEntity.created(location).body(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        }

    }


}
