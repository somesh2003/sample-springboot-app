package com.datajpa.demo.jpa.mappings;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1/useraccounts")
public class UserAccountController {

    private final UserAccountRepository userAccountRepository;
    private final AddressRepository addressRepository;
    private final PostRepository postRepository;

    public UserAccountController(UserAccountRepository userAccountRepository, AddressRepository addressRepository, PostRepository postRepository) {
        this.userAccountRepository = userAccountRepository;
        this.addressRepository = addressRepository;
        this.postRepository = postRepository;
    }

    @PostMapping("/register")
    public UserAccount registerNewUserAccount(@RequestBody UserAccount newUserAccount) {
        return userAccountRepository.save(newUserAccount);
    }

    @PostMapping("/{id}/addaddress")
    @Transactional
    public UserAccount registernewAddress(@PathVariable Integer id, @RequestBody Address newAddress) {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id not available"));
        Address address = addressRepository.save(newAddress);
        userAccount.setAddress(address);
        return userAccountRepository.save(userAccount);
    }

    @PostMapping("/{id}/posts")
    @Transactional
    public UserAccount addUserPost(@PathVariable Integer id, @RequestBody Post newPost) {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id not available"));
        Post savedPost = postRepository.save(newPost);
        userAccount.getPosts().add(savedPost);
        return userAccountRepository.save(userAccount);
    }

    @GetMapping("/{id}")
    public UserAccount getUserAccountById(@PathVariable Integer id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id not available"));
    }
}
