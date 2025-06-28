package com.company.ws.dto.request;

import com.company.ws.entity.User;
import com.company.ws.utilities.UniqueEmail;
import com.company.ws.utilities.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserCreateRequest {
    @NotBlank
    @Size(min = 4, max = 12)
    @UniqueUsername
    private String username;
    @NotBlank
    @Size(min = 8, max = 16)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@*#$%^&+=]).{8,}$")
    private String password;
    @NotBlank
    @Email
    @UniqueEmail
    private String email;

    public UserCreateRequest(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }


}
