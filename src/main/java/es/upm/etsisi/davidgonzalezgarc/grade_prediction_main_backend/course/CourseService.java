package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.CourseRepository;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.rest.dto.TrainModelRequestDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.GradeService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.GradeItemService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final GradeItemService gradeItemService;
    private final GradeService gradeService;
    private final StudentCourseRelationshipService studentCourseRelationshipService;
    private final TeacherCourseRelationshipService teacherCourseRelationshipService;
    private final RestTemplate restTemplate;
    @Value("${prediction-backend.url}")
    private String host;

    public Course getReferenceById(String id) {
        return courseRepository.getReferenceById(id);
    }

    public Optional<Course> getById(String id) {
        return courseRepository.findById(id);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public Course changeSchoolYear(Course course) {
        List<GradeItem> gradeItems = gradeItemService.findAllByCourse(course);
        gradeItems.forEach(gradeItem -> {
            restTemplate.postForLocation(URI.create(host + "/v1/train"),
                    TrainModelRequestDto.builder()
                            .courseId(course.getId())
                            .schoolYear(course.getSchoolYear())
                            .gradeItemPosition(gradeItem.getPosition())
                            .build());
        });
        course.setSchoolYear((short) (course.getSchoolYear() + 1));
        return courseRepository.save(course);
    }

    public void delete(Course course) {
        studentCourseRelationshipService
                .findAllByCourse(course)
                .forEach(studentCourseRelationshipService::delete);
        teacherCourseRelationshipService
                .findAllByCourse(course)
                .forEach(teacherCourseRelationshipService::delete);
        gradeItemService.findAllByCourse(course)
                .forEach(gradeItem -> {
                    gradeService
                            .findAllByGradeItem(gradeItem)
                            .forEach(gradeService::delete);
                    gradeItemService.delete(gradeItem);
                });
        courseRepository.delete(course);
    }

}
