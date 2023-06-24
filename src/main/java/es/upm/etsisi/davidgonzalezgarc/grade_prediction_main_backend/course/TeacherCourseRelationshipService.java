package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.TeacherCourseRelationship;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.TeacherCourseRelationshipRepository;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherCourseRelationshipService {
    private static final int PAGE_SIZE = 10;

    private final TeacherCourseRelationshipRepository teacherCourseRelationshipRepository;

    public boolean isTeacherInCourse(User teacher, Course course) {
        return teacherCourseRelationshipRepository.existsById(TeacherCourseRelationship.TeacherCourseRelationshipKey.builder()
                .courseId(course.getId())
                .teacherId(teacher.getId()).build());
    }

    public Optional<TeacherCourseRelationship> findByTeacherAndCourse(User teacher, Course course) {
        return teacherCourseRelationshipRepository.findByTeacherAndCourse(teacher, course);
    }

    public Page<Course> findAllCoursesByTeacher(User teacher, int page) {
        return teacherCourseRelationshipRepository.findAllByTeacher(
                        teacher, PageRequest.of(page, PAGE_SIZE))
                .map(TeacherCourseRelationship::getCourse);
    }

    public Page<User> findAllTeachersByCourse(Course course, int page) {
        return teacherCourseRelationshipRepository.findAllByCourse(
                        course,
                        PageRequest.of(page, PAGE_SIZE))
                .map(TeacherCourseRelationship::getTeacher);
    }

    public Set<TeacherCourseRelationship> findAllByCourse(Course course) {
        return teacherCourseRelationshipRepository.findAllByCourse(course);
    }

    public void addTeacherToCourse(Course course, User teacher) {
        save(TeacherCourseRelationship.builder()
                .id(TeacherCourseRelationship.TeacherCourseRelationshipKey.builder()
                        .courseId(course.getId())
                        .teacherId(teacher.getId()).build())
                .course(course)
                .teacher(teacher).build());
    }

    private void save(TeacherCourseRelationship teacherCourseRelationship) {
        teacherCourseRelationshipRepository.save(teacherCourseRelationship);
    }

    public void removeTeacherFromCourse(Course course, User teacher) {
        findByTeacherAndCourse(teacher, course).ifPresent(this::delete);
    }

    public void delete(TeacherCourseRelationship teacherCourseRelationship) {
        teacherCourseRelationshipRepository.delete(teacherCourseRelationship);
    }

}
