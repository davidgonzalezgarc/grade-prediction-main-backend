package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface GradeRepository extends JpaRepository<Grade, Grade.GradeKey> {

    @Query(value = """
            SELECT g FROM Grade g INNER JOIN g.gradeItem gi INNER JOIN gi.course c\s
            WHERE g.id.studentId = :studentId AND c.id = :courseId AND g.id.schoolYear = c.schoolYear\s
            ORDER BY gi.position\s
            """)
    List<Grade> findAllByStudentAndCourse(@Param("studentId") String studentId,
                                          @Param("courseId") String courseId);

    @Query(value = """
            SELECT g FROM Grade g INNER JOIN g.gradeItem gi INNER JOIN gi.course c\s
            WHERE g.id.gradeItemId = :gradeItemId AND g.id.schoolYear = c.schoolYear\s
            """)
    Page<Grade> findAllByGradeItem(@Param("gradeItemId") String gradeItemId, Pageable pageable);

    Set<Grade> findAllByGradeItem(GradeItem gradeItem);

}
