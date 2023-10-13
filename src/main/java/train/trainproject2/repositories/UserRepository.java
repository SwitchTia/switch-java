package train.trainproject2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import train.trainproject2.entities.Users;

@Repository
public interface UserRepository extends JpaRepository <Users, Integer> {
    Users findByEmail (String email);
    boolean existsByEmail (String email);
}
