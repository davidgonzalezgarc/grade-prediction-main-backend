package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.StudentCourseRelationship;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.StudentCourseRelationshipRepository;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence.GradeRepository;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class StudentCourseRelationshipService {
    private static final int PAGE_SIZE = 10;

    private final StudentCourseRelationshipRepository studentCourseRelationshipRepository;

    private final GradeRepository gradeRepository;

    public boolean isStudentInCourse(User student, Course course) {
        return studentCourseRelationshipRepository.existsById(StudentCourseRelationship.StudentCourseRelationshipKey.builder()
                .courseId(course.getId())
                .studentId(student.getId())
                .schoolYear(course.getSchoolYear()).build());
    }

    public Optional<StudentCourseRelationship> findByStudentAndCourse(User student, Course course) {
        return studentCourseRelationshipRepository.findByStudentAndCourse(student.getId(), course.getId());
    }

    public Page<Course> findAllCoursesByStudent(User student, int page) {
        return studentCourseRelationshipRepository.findAllByStudent(
                        student.getId(), PageRequest.of(page, PAGE_SIZE))
                .map(StudentCourseRelationship::getCourse);
    }

    public Page<User> findAllByCourseAndSameSchoolYear(Course course, int page) {
        return studentCourseRelationshipRepository.findAllByCourseAndSameSchoolYear(
                        course.getId(), PageRequest.of(page, PAGE_SIZE))
                .map(StudentCourseRelationship::getStudent);
    }

    public Set<StudentCourseRelationship> findAllByCourse(Course course) {
        return studentCourseRelationshipRepository.findAllByCourse(course);
    }

    public void addStudentToCourse(Course course, User student) {
        save(StudentCourseRelationship.builder()
                .id(StudentCourseRelationship.StudentCourseRelationshipKey.builder()
                        .courseId(course.getId())
                        .studentId(student.getId())
                        .schoolYear(course.getSchoolYear()).build())
                .course(course)
                .student(student).build());
    }

    public void save(StudentCourseRelationship studentCourseRelationship) {
        studentCourseRelationshipRepository.save(studentCourseRelationship);
    }

    public void removeStudentFromCourse(Course course, User student) {
        findByStudentAndCourse(student, course).ifPresent(scr -> {
            gradeRepository.findAllByStudentAndCourse(scr.getStudent().getId(),
                            scr.getCourse().getId())
                    .forEach(gradeRepository::delete);
            delete(scr);
        });
    }

    public void delete(StudentCourseRelationship studentCourseRelationship) {
        studentCourseRelationshipRepository.delete(studentCourseRelationship);
    }
}
