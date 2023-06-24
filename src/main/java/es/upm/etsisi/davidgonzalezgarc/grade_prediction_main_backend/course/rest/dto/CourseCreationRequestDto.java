package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.rest.dto;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.rest.dto.GradeItemCreationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreationRequestDto {
    private String name;
    private List<GradeItemCreationRequestDto> gradeItems;
}
