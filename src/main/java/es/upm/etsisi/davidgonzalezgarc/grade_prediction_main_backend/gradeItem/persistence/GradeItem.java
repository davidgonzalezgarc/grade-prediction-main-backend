package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence.Grade;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "grade_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GradeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private short percentage;

    private short position;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "gradeItem")
    private Set<Grade> grades;

}
