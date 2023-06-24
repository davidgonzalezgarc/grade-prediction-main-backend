package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"gradeItems"})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private short schoolYear;

    @OneToMany(mappedBy = "course")
    private Set<StudentCourseRelationship> students;

    @OneToMany(mappedBy = "course")
    private Set<TeacherCourseRelationship> teachers;

    @OneToMany(mappedBy = "course")
    private Set<GradeItem> gradeItems;

}
