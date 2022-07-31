package app.hungernet.repository;

import app.hungernet.models.User;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Page<User> getAllByRole_RoleNameIsNotOrUuidEquals(Pageable pageable, String roleName, String userID);
    Page<User> getAllByRole_RoleNameEquals(final String roleName, Pageable pageable);
}
