package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.authentication.rest.dto;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private String role;

    public Role getRole() {
        if (role.equalsIgnoreCase("student")) {
            return Role.STUDENT;
        }
        if (role.equalsIgnoreCase("teacher")) {
            return Role.TEACHER;
        }
        return null;
    }
}
