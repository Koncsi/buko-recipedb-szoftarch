package hu.buko.szoftarchrecipedb.service;

import hu.buko.szoftarchrecipedb.dao.UserRepository;
import hu.buko.szoftarchrecipedb.model.RecipedbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void addUser(String username, String password) {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_USER");
        RecipedbUser user = new RecipedbUser();
        user.setAuthorities(authorities);
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(username);
        userRepository.save(user);
    }

    public void addAdmin(String username, String password) {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_ADMIN");
        authorities.add("ROLE_USER");
        RecipedbUser user = new RecipedbUser();
        user.setAuthorities(authorities);
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(username);
        userRepository.save(user);
    }

    public RecipedbUser getUserByUsername(String username) throws UsernameNotFoundException {
        Optional<RecipedbUser> user = userRepository.findByUsername(username);
        if (!user.isPresent())
            throw new UsernameNotFoundException("No user found with username: " + username);
        return user.get();
    }

    public List<RecipedbUser> getAllUsers() {
        return userRepository.findAll();
    }
}
