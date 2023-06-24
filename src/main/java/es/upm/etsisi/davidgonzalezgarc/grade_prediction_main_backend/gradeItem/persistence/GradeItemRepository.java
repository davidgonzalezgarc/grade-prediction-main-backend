package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeItemRepository extends JpaRepository<GradeItem, String> {

    List<GradeItem> findAllByCourseOrderByPosition(Course course);
}
