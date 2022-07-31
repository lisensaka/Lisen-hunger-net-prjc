package app.hungernet.management;

import app.hungernet.models.Role;
import app.hungernet.models.User;
import app.hungernet.repository.RoleRepository;
import app.hungernet.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AutoInsertValues implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) {
//        roleRepository.saveAll(getAllRoles());
//        User saveUser = userRepository.save(addDefaultAdminUser());
//        saveUser.setRole(roleRepository.findByRoleName("ADMIN"));
//        userRepository.save(saveUser);
    }

    public List<Role> getAllRoles(){
        Role userRole = new Role();
        userRole.setRoleName("CLIENT");

        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");

        Role managerRole = new Role();
        managerRole.setRoleName("RESTAURANT_MANAGER");

        return List.of(userRole,adminRole,managerRole);
    }

    public User addDefaultAdminUser(){
        User user = new User();
        user.setFirstName("Admin");
        user.setLastName("Admin");
        //user.setRole(roleRepository.findByRoleName("ADMIN"));
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("admin"));
        return user;
    }
}
