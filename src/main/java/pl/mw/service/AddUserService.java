package pl.mw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mw.model.AppUser;
import pl.mw.model.UserRole;
import pl.mw.repositories.UserRepository;
import pl.mw.repositories.UserRoleRepository;

@Service
public class AddUserService {

    private static final String DEFAULT_ROLE="BASIC";
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AddUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public AppUser addUserWithDefaultRole(AppUser user){
        UserRole userRole = userRoleRepository.findUserRoleByRolename(DEFAULT_ROLE);
        user.getUserRoles().add(userRole);
        String passHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passHash);
        AppUser saved = userRepository.save(user);
        return saved;
    }
}
