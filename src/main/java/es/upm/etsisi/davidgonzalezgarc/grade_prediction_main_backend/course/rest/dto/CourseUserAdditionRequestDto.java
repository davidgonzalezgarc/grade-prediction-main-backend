package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseUserAdditionRequestDto {
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;
}
