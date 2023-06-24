package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.rest.dto;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String id;
    private String name;
    private String email;
    private Role role;
}
