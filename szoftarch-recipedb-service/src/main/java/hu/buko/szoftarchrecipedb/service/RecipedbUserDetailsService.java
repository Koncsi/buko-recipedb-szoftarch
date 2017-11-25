package hu.buko.szoftarchrecipedb.service;

import hu.buko.szoftarchrecipedb.dao.UserRepository;
import hu.buko.szoftarchrecipedb.model.RecipedbUser;
import hu.buko.szoftarchrecipedb.model.user.RecipedbUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipedbUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public RecipedbUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        if (username == null)
            throw new UsernameNotFoundException("Username is null");
        Optional<RecipedbUser> user = userRepository.findByUsername(username);
        if (!user.isPresent())
            throw new UsernameNotFoundException("No user found with username: " + username);
        return new RecipedbUserDetails(user.get());
    }
}
