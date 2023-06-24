package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}
