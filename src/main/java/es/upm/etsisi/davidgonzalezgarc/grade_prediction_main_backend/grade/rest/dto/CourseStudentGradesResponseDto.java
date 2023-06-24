package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseStudentGradesResponseDto {
    private StudentResponseDto student;
    private List<StudentGradeDto> grades;
}
