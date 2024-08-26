package com.example.demo.controller;

import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/register")
    public ResponseEntity<Person> registerUser(@RequestBody Person person) {
        Person registeredUser = personService.registerUser(person);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        Optional<String> token = personService.loginUser(email, password);
        if (token.isPresent()) {
            return ResponseEntity.ok().body(token.get()); // ส่งคืน JWT token
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Person> updateUser(@PathVariable("id") Long id, @RequestBody Person updatedPerson) {
        Optional<Person> updatedUser = personService.updateUser(id, updatedPerson);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}
