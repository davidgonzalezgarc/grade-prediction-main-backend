package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface StudentCourseRelationshipRepository extends JpaRepository<StudentCourseRelationship, StudentCourseRelationship.StudentCourseRelationshipKey> {


    @Query(value = """
            SELECT scr FROM StudentCourseRelationship scr INNER JOIN scr.course c\s
            WHERE scr.id.studentId = :studentId AND scr.id.schoolYear = c.schoolYear\s
            """)
    Page<StudentCourseRelationship> findAllByStudent(@Param("studentId") String studentId, Pageable pageable);

    @Query(value = """
            SELECT scr FROM StudentCourseRelationship scr INNER JOIN scr.course c INNER JOIN scr.student u\s
            WHERE scr.id.courseId = :courseId AND scr.id.schoolYear = c.schoolYear\s
            ORDER BY u.name\s
            """)
    Page<StudentCourseRelationship> findAllByCourseAndSameSchoolYear(@Param("courseId") String courseId, Pageable pageable);

    @Query(value = """
            SELECT scr FROM StudentCourseRelationship scr INNER JOIN scr.course c\s
            WHERE scr.id.studentId = :studentId AND scr.id.courseId = :courseId AND scr.id.schoolYear = c.schoolYear\s
            """)
    Optional<StudentCourseRelationship> findByStudentAndCourse(@Param("studentId") String studentId,
                                                               @Param("courseId") String courseId);

    Set<StudentCourseRelationship> findAllByCourse(Course course);
}
