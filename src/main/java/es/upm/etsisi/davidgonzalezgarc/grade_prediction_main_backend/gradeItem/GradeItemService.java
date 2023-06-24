package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradeItemService {

    private final GradeItemRepository gradeItemRepository;

    public GradeItem getReferenceById(String id) {
        return gradeItemRepository.getReferenceById(id);
    }

    public Optional<GradeItem> getById(String id) {
        return gradeItemRepository.findById(id);
    }

    public List<GradeItem> findAllByCourse(Course course) {
        return gradeItemRepository.findAllByCourseOrderByPosition(course);
    }

    public GradeItem save(GradeItem gradeItem) {
        return gradeItemRepository.save(gradeItem);
    }

    public void delete(GradeItem gradeItem) {
        gradeItemRepository.delete(gradeItem);
    }
}
