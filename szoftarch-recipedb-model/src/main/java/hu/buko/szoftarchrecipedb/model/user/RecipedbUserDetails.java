package hu.buko.szoftarchrecipedb.model.user;

import hu.buko.szoftarchrecipedb.model.RecipedbUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class RecipedbUserDetails implements UserDetails {
    private RecipedbUser user;

    public RecipedbUserDetails() {
    }

    public RecipedbUserDetails(RecipedbUser recipedbUser) {
        this.user = recipedbUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> auth = new HashSet<>();
        for (String role : user.getAuthorities()) {
            auth.add(new SimpleGrantedAuthority(role));
        }
        return auth;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
