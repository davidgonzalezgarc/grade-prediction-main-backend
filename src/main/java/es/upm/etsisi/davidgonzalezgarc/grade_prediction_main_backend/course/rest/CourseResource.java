package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.rest;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.CourseService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.StudentCourseRelationshipService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.TeacherCourseRelationshipService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.StudentCourseRelationship;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.rest.dto.*;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.GradeService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence.Grade;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.rest.dto.StudentResponseDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.GradeItemService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.UserService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.Role;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseResource {

    private final CourseService courseService;
    private final UserService userService;
    private final TeacherCourseRelationshipService teacherCourseRelationshipService;
    private final StudentCourseRelationshipService studentCourseRelationshipService;
    private final GradeItemService gradeItemService;
    private final GradeService gradeService;
    private final ModelMapper modelMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<?> create(@AuthenticationPrincipal User user, @RequestBody CourseCreationRequestDto request) {
        Course course = Course.builder()
                .name(request.getName()).build();
        Course savedCourse = courseService.save(course);
        teacherCourseRelationshipService.addTeacherToCourse(savedCourse, user);
        Set<GradeItem> gradeItems = request.getGradeItems().stream()
                .map(rgi -> gradeItemService.save(GradeItem.builder()
                        .name(rgi.getName())
                        .percentage(rgi.getPercentage())
                        .position(rgi.getPosition())
                        .course(savedCourse).build())).collect(Collectors.toSet());
        savedCourse.setGradeItems(gradeItems);
        courseService.save(savedCourse);
        return ResponseEntity.ok().build();
    }

    @GetMapping(params = "page")
    public ResponseEntity<Page<CourseResponseDto>> getAll(@AuthenticationPrincipal User user, @RequestParam int page) {
        Page<Course> courses = Page.empty();
        if (user.getRole() == Role.STUDENT) {
            courses = studentCourseRelationshipService.findAllCoursesByStudent(user, page);
        } else if (user.getRole() == Role.TEACHER) {
            courses = teacherCourseRelationshipService.findAllCoursesByTeacher(user, page);
        }
        Page<CourseResponseDto> response = courses.map(course -> modelMapper.map(course, CourseResponseDto.class));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> get(@PathVariable String id, @AuthenticationPrincipal User user) {
        Optional<Course> optCourse = courseService.getById(id);
        if (optCourse.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Course course = optCourse.get();
        CourseResponseDto response = modelMapper.map(course, CourseResponseDto.class);
        boolean exists = false;
        if (user.getRole() == Role.STUDENT) {
            exists = studentCourseRelationshipService
                    .isStudentInCourse(user, course);
        }
        if (user.getRole() == Role.TEACHER) {
            exists = teacherCourseRelationshipService
                    .isTeacherInCourse(user, course);
        }
        return exists ? ResponseEntity.ok().body(response) : ResponseEntity.status(403).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Course course = courseService.getReferenceById(id);
        courseService.delete(course);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/users")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<?> addUser(@PathVariable String id, @RequestBody CourseUserAdditionRequestDto request) {
        Course course = courseService.getReferenceById(id);
        Optional<User> optUser = userService.getByEmail(request.getEmail());
        if (optUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User user = optUser.get();
        if (user.getRole() == Role.STUDENT) {
            studentCourseRelationshipService.addStudentToCourse(course, user);
            gradeItemService.findAllByCourse(course).forEach(gradeItem -> {
                Grade grade = Grade.builder()
                        .id(Grade.GradeKey.builder()
                                .studentId(user.getId())
                                .gradeItemId(gradeItem.getId())
                                .schoolYear(course.getSchoolYear()).build())
                        .student(user)
                        .gradeItem(gradeItem)
                        .build();
                gradeService.save(grade);
            });
        }
        if (user.getRole() == Role.TEACHER) {
            teacherCourseRelationshipService.addTeacherToCourse(course, user);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{id}/users/students", params = "page")
    public ResponseEntity<Page<StudentResponseDto>> getAllStudents(@AuthenticationPrincipal User user, @PathVariable String id, @RequestParam int page) {
        Course course = courseService.getReferenceById(id);
        if ((user.getRole() == Role.STUDENT && !studentCourseRelationshipService.isStudentInCourse(user, course)) ||
                (user.getRole() == Role.TEACHER && !teacherCourseRelationshipService.isTeacherInCourse(user, course))) {
            return ResponseEntity.status(403).build();
        }
        Page<User> students = studentCourseRelationshipService.findAllByCourseAndSameSchoolYear(course, page);
        Page<StudentResponseDto> response = students.map(student -> modelMapper.map(student, StudentResponseDto.class));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(path = "/{id}/users/teachers", params = "page")
    public ResponseEntity<Page<TeacherResponseDto>> getAllTeachers(@AuthenticationPrincipal User user, @PathVariable String id, @RequestParam int page) {
        Course course = courseService.getReferenceById(id);
        if ((user.getRole() == Role.STUDENT && !studentCourseRelationshipService.isStudentInCourse(user, course)) ||
                (user.getRole() == Role.TEACHER && !teacherCourseRelationshipService.isTeacherInCourse(user, course))) {
            return ResponseEntity.status(403).build();
        }
        Page<User> teachers = teacherCourseRelationshipService.findAllTeachersByCourse(course, page);
        Page<TeacherResponseDto> response = teachers.map(teacher -> modelMapper.map(teacher, TeacherResponseDto.class));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}/users/{userEmail}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<?> removeUser(@PathVariable String id, @PathVariable String userEmail) {
        Course course = courseService.getReferenceById(id);
        Optional<User> optUser = userService.getByEmail(userEmail);
        if (optUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User user = optUser.get();
        if (user.getRole() == Role.STUDENT) {
            studentCourseRelationshipService.removeStudentFromCourse(course, user);
        }
        if (user.getRole() == Role.TEACHER) {
            teacherCourseRelationshipService.removeTeacherFromCourse(course, user);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/students/info")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> getInformation(@AuthenticationPrincipal User user, @PathVariable String id) {
        Course course = courseService.getReferenceById(id);
        Optional<StudentCourseRelationship> studentCourseRelationshipOpt = studentCourseRelationshipService
                .findByStudentAndCourse(user, course);
        if (studentCourseRelationshipOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        StudentCourseRelationship studentCourseRelationship = studentCourseRelationshipOpt.get();
        StudentCourseInformationDto dto = modelMapper.map(studentCourseRelationship, StudentCourseInformationDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/{id}/students/info")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> editInformation(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody StudentCourseInformationDto request) {
        Course course = courseService.getReferenceById(id);
        Optional<StudentCourseRelationship> studentCourseRelationshipOpt = studentCourseRelationshipService
                .findByStudentAndCourse(user, course);
        if (studentCourseRelationshipOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        StudentCourseRelationship studentCourseRelationship = studentCourseRelationshipOpt.get();
        modelMapper.map(request, studentCourseRelationship);
        studentCourseRelationshipService.save(studentCourseRelationship);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/change-school-year")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<?> changeSchoolYear(@AuthenticationPrincipal User user, @PathVariable String id) {
        Optional<Course> courseOptional = courseService.getById(id);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Course course = courseOptional.get();
        if (user.getRole() == Role.STUDENT ||
                (user.getRole() == Role.TEACHER && !teacherCourseRelationshipService.isTeacherInCourse(user, course))) {
            return ResponseEntity.status(403).build();
        }
        courseService.changeSchoolYear(course);
        return ResponseEntity.ok().build();
    }

}
