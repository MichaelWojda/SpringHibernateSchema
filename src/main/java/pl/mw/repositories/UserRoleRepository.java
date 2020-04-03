package pl.mw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mw.model.AppUser;
import pl.mw.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    
    UserRole findUserRoleByRolename(String rolename);
}
