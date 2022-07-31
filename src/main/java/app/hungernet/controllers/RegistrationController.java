package app.hungernet.controllers;

import app.hungernet.models.dtos.ClientDto;
import app.hungernet.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static app.hungernet.constants.ApplicationConstantsConfigs.REGISTRATION_API;
import static app.hungernet.models.dtos.ClientDto.convertClientToUser;

@RestController
@RequestMapping(REGISTRATION_API)
@AllArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody ClientDto clientDto) throws Exception {
        userService.addNewUser(convertClientToUser(clientDto));
        return ResponseEntity.status(HttpStatus.CREATED).body("User was created successfully");
    }

}
