package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.CourseService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.GradeService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence.Grade;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto.CourseStudentGradesResponseDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto.GradeKeyDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto.StudentGradeDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeResource {

    private final GradeService gradeService;
    private final CourseService courseService;

    @GetMapping(params = "courseId")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<List<StudentGradeDto>> allByStudentAndCourse(
            @AuthenticationPrincipal User user, @RequestParam String courseId) {
        Course course = courseService.getReferenceById(courseId);
        List<StudentGradeDto> response = gradeService
                .findAllByStudentAndCourse(user, course).stream()
                .map(g -> StudentGradeDto.builder()
                        .id(GradeKeyDto.builder()
                                .studentId(g.getId().getStudentId())
                                .gradeItemId(g.getId().getGradeItemId())
                                .schoolYear(g.getId().getSchoolYear())
                                .build())
                        .grade(g.getGrade()).build())
                .toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(params = {"courseId", "page"})
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<Page<CourseStudentGradesResponseDto>> allByGradeItem(
            @AuthenticationPrincipal User user, @RequestParam String courseId, @RequestParam int page) {
        Course course = courseService.getReferenceById(courseId);
        Page<CourseStudentGradesResponseDto> response = gradeService
                .findAllByCourse(course, page);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<?> edit(@AuthenticationPrincipal User user, @RequestBody StudentGradeDto request) {
        Grade.GradeKey id = Grade.GradeKey.builder()
                .studentId(request
                        .getId().getStudentId())
                .gradeItemId(request
                        .getId().getGradeItemId())
                .schoolYear(request
                        .getId().getSchoolYear())
                .build();
        Optional<Grade> gradeOptional = gradeService.getById(id);
        if (gradeOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Grade grade = gradeOptional.get();
        grade.setGrade(request.getGrade());
        gradeService.save(grade);
        return ResponseEntity.ok().build();
    }

}
