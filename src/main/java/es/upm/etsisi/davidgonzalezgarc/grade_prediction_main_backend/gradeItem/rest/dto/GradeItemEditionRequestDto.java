package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeItemEditionRequestDto {
    private String name;
}
