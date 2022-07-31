package app.hungernet.services;

import app.hungernet.models.User;
import app.hungernet.models.dtos.UserDto;
import app.hungernet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static app.hungernet.models.dtos.UserDto.convertUserDtoToUser;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;

    public void addNewUser(UserDto userDto) throws Exception {
        try {
            User user = new User();
            convertUserDtoToUser(userDto, user);
            encryptUserPasswordBeforeSavingInDb(userDto, user);
            user.setRole(roleService.findRoleByRoleName(userDto.getRoleName()));
            userRepository.save(user);
        } catch (ConstraintViolationException c) {
            throw new Exception("Error occurred while adding new user " + c.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username does not exist" + username));
    }

    public Page<User> getAllUsers(Pageable pageable, String loggedUsername) throws Exception {
        User loggedUser = (User) loadUserByUsername(loggedUsername);
        try {
            return userRepository.getAllByRole_RoleNameIsNotOrUuidEquals(pageable, loggedUser.getRole().getRoleName(), loggedUser.getUuid());
        } catch (Exception e) {
            throw new Exception("Error occurred while getting all users: " + e.getMessage());
        }
    }

    public User updateUserById(UserDto userDto, String userId) throws Exception {

        try {
            User userById = getUserById(userId);
            convertUserDtoToUser(userDto, userById);
            encryptUserPasswordBeforeSavingInDb(userDto, userById);

            return userRepository.save(userById);
        } catch (Exception e) {
            throw new Exception("Error occurred while updating users by id : " + e.getMessage());
        }

    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException(String.format("User with id: %s, was not found", userId)));
    }

    public void deleteUserById(String userId) throws Exception {
        try {
            userRepository.delete(getUserById(userId));
        } catch (Exception e) {
            throw new Exception("Error occurred while deleting users by id : " + e.getMessage());
        }
    }

    public Page<?> filterUserByRole(String userRole, Pageable pageable) throws Exception {
        try {
            return userRepository.getAllByRole_RoleNameEquals(userRole, pageable);
        } catch (Exception e) {
            throw new Exception("Error occurred while filtering all users by role : " + e.getMessage());
        }
    }

    private void encryptUserPasswordBeforeSavingInDb(UserDto userDto, User user) {
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
    }
}
