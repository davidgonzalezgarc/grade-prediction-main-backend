package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TeacherCourseRelationshipRepository extends JpaRepository<TeacherCourseRelationship, TeacherCourseRelationship.TeacherCourseRelationshipKey> {

    Page<TeacherCourseRelationship> findAllByTeacher(User teacher, Pageable pageable);

    Page<TeacherCourseRelationship> findAllByCourse(Course course, Pageable pageable);

    Set<TeacherCourseRelationship> findAllByCourse(Course course);

    Optional<TeacherCourseRelationship> findByTeacherAndCourse(User teacher, Course course);
}
