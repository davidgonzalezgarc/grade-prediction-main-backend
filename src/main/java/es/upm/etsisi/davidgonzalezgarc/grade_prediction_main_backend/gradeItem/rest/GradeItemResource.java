package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.rest;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.CourseService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.GradeItemService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.rest.dto.GradeItemEditionRequestDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.rest.dto.GradeItemResponseDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/grade-items")
@RequiredArgsConstructor
@Slf4j
public class GradeItemResource {

    private final GradeItemService gradeItemService;

    private final CourseService courseService;

    private final ModelMapper modelMapper;

    @GetMapping(params = "courseId")
    public ResponseEntity<List<GradeItemResponseDto>> allByCourse(@AuthenticationPrincipal User user, @RequestParam String courseId) {
        Course course = courseService.getReferenceById(courseId);
        List<GradeItem> gradeItems = gradeItemService.findAllByCourse(course);
        List<GradeItemResponseDto> response = gradeItems.stream()
                .map(gi -> modelMapper.map(gi, GradeItemResponseDto.class)).toList();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<?> modifyOne(@PathVariable String id, @RequestBody GradeItemEditionRequestDto request) {
        Optional<GradeItem> gradeItemOpt = gradeItemService.getById(id);
        if (gradeItemOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GradeItem gradeItem = gradeItemOpt.get();
        modelMapper.map(request, gradeItem);
        gradeItemService.save(gradeItem);
        return ResponseEntity.ok().build();
    }

}
