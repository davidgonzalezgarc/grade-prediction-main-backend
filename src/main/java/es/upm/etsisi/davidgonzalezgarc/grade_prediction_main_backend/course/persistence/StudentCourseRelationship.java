package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "student_course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseRelationship {

    @EmbeddedId
    private StudentCourseRelationshipKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    private short travelTime;

    private short weeklyStudyTime;

    private short failures;

    private boolean extraEducationalSupport;

    private boolean familyEducationalSupport;

    private boolean extraPaidClasses;

    private short absences;

    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class StudentCourseRelationshipKey implements Serializable {

        private String studentId;
        private String courseId;
        private short schoolYear;

    }

}
