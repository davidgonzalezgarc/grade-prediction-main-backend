package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "teacher_course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseRelationship {

    @EmbeddedId
    private TeacherCourseRelationshipKey id;

    @ManyToOne
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class TeacherCourseRelationshipKey implements Serializable {

        private String teacherId;
        private String courseId;

    }

}
