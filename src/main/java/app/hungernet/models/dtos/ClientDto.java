package app.hungernet.models.dtos;

import app.hungernet.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public static UserDto convertClientToUser(ClientDto clientDto) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(clientDto.getFirstName());
        userDto.setLastName(clientDto.getLastName());
        userDto.setUsername(clientDto.getUsername());
        userDto.setPassword(clientDto.getPassword());
        userDto.setRoleName("CLIENT");
        return userDto;
    }
}
