package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.prediction.rest;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.Course;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.GradeItemService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.gradeItem.persistence.GradeItem;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.prediction.PredictionService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.prediction.rest.dto.PredictionRequestDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.prediction.rest.dto.PredictionResponseDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/predictions")
@RequiredArgsConstructor
public class PredictionResource {

    private final PredictionService predictionService;

    private final GradeItemService gradeItemService;

    @GetMapping(value = "/predict", params = "gradeItemId")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> predict(@AuthenticationPrincipal User user, @RequestParam String gradeItemId) {
        Optional<GradeItem> gradeItemOpt = gradeItemService.getById(gradeItemId);
        if (gradeItemOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GradeItem gradeItem = gradeItemOpt.get();
        Course course = gradeItem.getCourse();
        PredictionRequestDto request = PredictionRequestDto.builder()
                .courseId(course.getId())
                .schoolYear(course.getSchoolYear())
                .gradeItemPosition(gradeItem.getPosition())
                .studentId(user.getId()).build();
        PredictionResponseDto response = predictionService.predict(request);
        return ResponseEntity.ok().body(response);
    }

}
