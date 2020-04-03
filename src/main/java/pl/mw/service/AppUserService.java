package pl.mw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.mw.model.AppUser;
import pl.mw.model.UserRole;
import pl.mw.repositories.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class AppUserService implements UserDetailsService {


    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findAppUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Uzytkownika nie znaleziono");
        }
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                userAuthorities(user.getUserRoles())
        );

        return userDetail;


    }

    private Set<GrantedAuthority> userAuthorities(Set<UserRole> userRoles) {
        Set<GrantedAuthority> authSet = new HashSet<>();
        userRoles.forEach(r -> {
            authSet.add(new SimpleGrantedAuthority(r.getRolename()));
        });
        return authSet;
    }
}
