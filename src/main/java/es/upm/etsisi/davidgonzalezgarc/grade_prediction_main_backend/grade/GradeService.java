package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.StudentCourseRelationshipService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence.Grade;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence.GradeRepository;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto.CourseStudentGradesResponseDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto.GradeKeyDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto.StudentGradeDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto.StudentResponseDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    private final StudentCourseRelationshipService studentCourseRelationshipService;

    private final ModelMapper modelMapper;

    public Grade getReferenceById(Grade.GradeKey id) {
        return gradeRepository.getReferenceById(id);
    }

    public Optional<Grade> getById(Grade.GradeKey id) {
        return gradeRepository.findById(id);
    }

    public List<Grade> findAllByStudentAndCourse(User student, Course course) {
        return gradeRepository.findAllByStudentAndCourse(student.getId(), course.getId());
    }

    public Page<CourseStudentGradesResponseDto> findAllByCourse(Course course, int page) {
        return studentCourseRelationshipService.findAllByCourseAndSameSchoolYear(course, page)
                .map(student -> {
                    return CourseStudentGradesResponseDto.builder()
                            .student(modelMapper.map(student, StudentResponseDto.class))
                            .grades(gradeRepository.findAllByStudentAndCourse(student.getId(), course.getId()).stream()
                                    .map(g -> {
                                        return StudentGradeDto.builder()
                                                .id(GradeKeyDto.builder()
                                                        .studentId(g.getId().getStudentId())
                                                        .gradeItemId(g.getId().getGradeItemId())
                                                        .schoolYear(g.getId().getSchoolYear())
                                                        .build())
                                                .grade(g.getGrade())
                                                .build();
                                    }).toList())
                            .build();
                });
    }

    public Set<Grade> findAllByGradeItem(GradeItem gradeItem) {
        return gradeRepository.findAllByGradeItem(gradeItem);
    }

    public Grade save(Grade grade) {
        return gradeRepository.save(grade);
    }

    public void delete(Grade grade) {
        gradeRepository.delete(grade);
    }

}
