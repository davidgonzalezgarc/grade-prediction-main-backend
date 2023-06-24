package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.csv;


import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.CourseService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.StudentCourseRelationshipService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.TeacherCourseRelationshipService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.StudentCourseRelationship;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.GradeService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence.Grade;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.GradeItemService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.StudentInformationService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.UserService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.Role;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.StudentInformation;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class CsvToBbdd {
    private static final Map<String, String> COURSES = Map.of("mat", "Matemáticas", "por", "Portugués");
    private static final String[] HEADERS = {
            "school",
            "sex",
            "age",
            "address",
            "famsize",
            "Pstatus",
            "Medu",
            "Fedu",
            "Mjob",
            "Fjob",
            "reason",
            "guardian",
            "traveltime",
            "studytime",
            "failures",
            "schoolsup",
            "famsup",
            "paid",
            "activities",
            "nursery",
            "higher",
            "internet",
            "romantic",
            "famrel",
            "freetime",
            "goout",
            "Dalc",
            "Walc",
            "health",
            "absences",
            "G1",
            "G2",
            "G3"};
    private final PasswordEncoder passwordEncoder;
    private final CourseService courseService;
    private final UserService userService;
    private final TeacherCourseRelationshipService teacherCourseRelationshipService;
    private final StudentCourseRelationshipService studentCourseRelationshipService;
    private final GradeItemService gradeItemService;
    private final StudentInformationService studentInformationService;
    private final GradeService gradeService;

    @EventListener(ApplicationReadyEvent.class)
    public void run() throws Exception {
        for (Map.Entry<String, String> course : COURSES.entrySet()) {
            if (userService.getByEmail("teacher-" + course.getKey() + "@mail.com").isPresent()) {
                log.info("Asignatura " + course.getValue() + " inicializada previamente.");
            } else {
                processCourse(course.getKey(), course.getValue());
                log.info("Asignatura " + course.getValue() + " inicializada.");
            }
        }
    }

    public void processCourse(String courseKey, String courseName) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .setDelimiter(";").build();
        try (InputStream fileInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("student-" + courseKey + ".csv");
             Reader reader = new InputStreamReader(fileInputStream);
             CSVParser csvParser = new CSVParser(reader, format)) {
            User teacher = createUser("teacher-" + courseKey, Role.TEACHER);
            Course course = Course.builder()
                    .name(courseName)
                    .schoolYear((short) 0).build();
            course = courseService.save(course);

            teacherCourseRelationshipService.addTeacherToCourse(course, teacher);

            List<GradeItem> gradeItems = List.of(
                    GradeItem.builder()
                            .name("Parcial 1")
                            .percentage((short) 33)
                            .position((short) 0)
                            .course(course).build(),
                    GradeItem.builder()
                            .name("Parcial 2")
                            .percentage((short) 33)
                            .position((short) 1)
                            .course(course).build(),
                    GradeItem.builder()
                            .name("Parcial 3")
                            .percentage((short) 33)
                            .position((short) 2)
                            .course(course).build()
            );
            gradeItems = gradeItems.stream()
                    .map(gradeItemService::save)
                    .toList();
            course.setGradeItems(new HashSet<>(gradeItems));
            courseService.save(course);

            for (CSVRecord csvRecord : csvParser) {
                User student = createUser("student-" + courseKey + "-" + String.format("%03d",
                        csvRecord.getRecordNumber()), Role.STUDENT);
                createStudentInformation(csvRecord, student);
                createStudentCourseRelationship(csvRecord, student, course);
                createGrades(csvRecord, student, gradeItems);
                log.info("Estudiante " + csvRecord.getRecordNumber() + " procesado.");
            }
        }
    }

    private User createUser(String name, Role role) {
        User user = User.builder()
                .name(name)
                .email(name + "@mail.com")
                .password(passwordEncoder.encode("pass"))
                .role(role)
                .build();
        return userService.save(user);
    }

    private void createStudentInformation(CSVRecord csvRecord, User student) {
        studentInformationService.save(StudentInformation.builder()
                .id(student.getId())
                .sex(csvRecord.get("sex"))
                .age(Short.parseShort(csvRecord.get("age")))
                .address(csvRecord.get("address"))
                .familySize(csvRecord.get("famsize"))
                .parentsStatus(csvRecord.get("Pstatus"))
                .motherEducation(Short.parseShort(csvRecord.get("Medu")))
                .fatherEducation(Short.parseShort(csvRecord.get("Fedu")))
                .motherJob(csvRecord.get("Mjob"))
                .fatherJob(csvRecord.get("Fjob"))
                .extraCurricularActivities(csvRecord.get("activities").equals("yes"))
                .romanticRelationship(csvRecord.get("romantic").equals("yes"))
                .freeTime(Short.parseShort(csvRecord.get("freetime")))
                .goOut(Short.parseShort(csvRecord.get("goout")))
                .workdayAlcohol(Short.parseShort(csvRecord.get("Dalc")))
                .weekendAlcohol(Short.parseShort(csvRecord.get("Walc")))
                .healthStatus(Short.parseShort(csvRecord.get("health"))).build());
    }

    private void createStudentCourseRelationship(CSVRecord csvRecord, User student, Course course) {
        studentCourseRelationshipService.save(StudentCourseRelationship.builder()
                .id(StudentCourseRelationship.StudentCourseRelationshipKey.builder()
                        .courseId(course.getId())
                        .studentId(student.getId())
                        .schoolYear(course.getSchoolYear()).build())
                .course(course)
                .student(student)
                .travelTime(Short.parseShort(csvRecord.get("traveltime")))
                .weeklyStudyTime(Short.parseShort(csvRecord.get("studytime")))
                .failures(Short.parseShort(csvRecord.get("failures")))
                .extraEducationalSupport(csvRecord.get("schoolsup").equals("yes"))
                .familyEducationalSupport(csvRecord.get("famsup").equals("yes"))
                .extraPaidClasses(csvRecord.get("paid").equals("yes"))
                .absences(Short.parseShort(csvRecord.get("absences"))).build());
    }

    private void createGrades(CSVRecord csvRecord, User student, List<GradeItem> gradeItems) {
        List<Float> grades = List.of(
                (Float.parseFloat(csvRecord.get("G1")) * 10f) / 20f,
                (Float.parseFloat(csvRecord.get("G2")) * 10f) / 20f,
                (Float.parseFloat(csvRecord.get("G3")) * 10f) / 20f);
        gradeItems.forEach(gradeItem -> {
            Grade grade = Grade.builder()
                    .id(Grade.GradeKey.builder()
                            .studentId(student.getId())
                            .gradeItemId(gradeItem.getId())
                            .schoolYear((short) 0).build())
                    .student(student)
                    .gradeItem(gradeItem)
                    .grade(grades.get(gradeItem.getPosition()))
                    .build();
            gradeService.save(grade);
        });
    }

}
