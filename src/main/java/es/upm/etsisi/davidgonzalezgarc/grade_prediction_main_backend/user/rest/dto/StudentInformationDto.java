package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentInformationDto {
    private String id;
    private String sex;
    private short age;
    private String address;
    private String familySize;
    private String parentsStatus;
    private short motherEducation;
    private short fatherEducation;
    private String motherJob;
    private String fatherJob;
    private boolean extraCurricularActivities;
    private boolean romanticRelationship;
    private short freeTime;
    private short goOut;
    private short workdayAlcohol;
    private short weekendAlcohol;
    private short healthStatus;
}
