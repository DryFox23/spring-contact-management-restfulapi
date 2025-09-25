package contact.management.restfulapi.repository;

import contact.management.restfulapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {


}
