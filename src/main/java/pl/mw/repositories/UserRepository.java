package pl.mw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mw.model.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {

    AppUser findAppUserByUsername(String username);
}
