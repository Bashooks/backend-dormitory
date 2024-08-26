package com.example.demo.service;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;

    }

    public Person registerUser(Person person) {
        return personRepository.save(person);
    }

    public Optional<String> loginUser(String email, String password) {
        Optional<Person> userOptional = personRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Person user = userOptional.get();
            if (password.equals(user.getPassword())) {
                // สร้าง JWT token หลังจากผู้ใช้ล็อกอินสำเร็จ
                String token = Jwts.builder()
                        .setSubject(user.getEmail())
                        .claim("id", user.getId())
                        .claim("email", user.getEmail())
                        .claim("firstName", user.getFirstName())
                        .claim("lastName", user.getLastName())
                        .claim("userType", user.getUserType())
                        .claim("address", user.getAddress())
                        
                        .claim("gender", user.getGender())
                        .signWith(key)
                        .compact();
                
                return Optional.of(token); // ส่งคืน token
            }
        }
        return Optional.empty();
    }

    public Optional<Person> updateUser(Long id, Person updatedPerson) {
        Optional<Person> existingPerson = personRepository.findById(id);
        if (existingPerson.isPresent()) {
            Person person = existingPerson.get();
            person.setFirstName(updatedPerson.getFirstName());
            person.setLastName(updatedPerson.getLastName());
             // ในกรณีที่ต้องการเปลี่ยนรหัสผ่าน
            person.setAddress(updatedPerson.getAddress());
            person.setDateOfBirth(updatedPerson.getDateOfBirth()); 
            person.setGender(updatedPerson.getGender());
            // อัปเดตฟิลด์อื่นๆ ตามที่ต้องการ
            return Optional.of(personRepository.save(person));
        }
        return Optional.empty(); // กรณีที่ไม่พบผู้ใช้
    }
}
