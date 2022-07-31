package app.hungernet.controllers;

import app.hungernet.models.User;
import app.hungernet.models.dtos.UserDto;
import app.hungernet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static app.hungernet.constants.ApplicationConstantsConfigs.USER_API;

@RestController
@RequestMapping(USER_API)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "createdAt", required = false) String createdAt,
            Principal principal
    ) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(createdAt)));
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers(pageable, principal.getName()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
    }

    @GetMapping("/filter-by-role/{userRole}")
    public ResponseEntity<?> filterUserByRole(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "createdAt", required = false) String createdAt,
            @PathVariable String userRole) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(createdAt)));
        return ResponseEntity.status(HttpStatus.OK).body(userService.filterUserByRole(userRole, pageable));
    }

    @PostMapping
    public ResponseEntity<String> addNewUser(@RequestBody UserDto userDto) throws Exception {
        userService.addNewUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User was created successfully");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserById(@RequestBody UserDto userDto, @PathVariable String userId) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserById(userDto, userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable String userId) throws Exception {
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body("User was deleted successfully");
    }
}
