package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "grade")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @EmbeddedId
    private GradeKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @MapsId("gradeItemId")
    @JoinColumn(name = "grade_item_id")
    private GradeItem gradeItem;

    private float grade;

    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class GradeKey implements Serializable {

        private String studentId;
        private String gradeItemId;
        private short schoolYear;

    }
}
