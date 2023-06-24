package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainModelRequestDto {
    private String courseId;
    private short schoolYear;
    private short gradeItemPosition;
}
