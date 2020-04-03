package pl.mw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mw.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material,Long> {
}
