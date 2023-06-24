package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDto {
    private String id;
    private String name;
    private String email;
}
