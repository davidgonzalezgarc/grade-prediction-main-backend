package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseInformationDto {
    private short travelTime;
    private short weeklyStudyTime;
    private short failures;
    private boolean extraEducationalSupport;
    private boolean familyEducationalSupport;
    private boolean extraPaidClasses;
    private short absences;
}
