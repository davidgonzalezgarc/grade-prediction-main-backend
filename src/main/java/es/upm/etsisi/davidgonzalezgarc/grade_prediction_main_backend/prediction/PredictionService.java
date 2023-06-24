package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.prediction;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.prediction.rest.dto.PredictionRequestDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.prediction.rest.dto.PredictionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final RestTemplate restTemplate;
    @Value("${prediction-backend.url}")
    private String host;

    public PredictionResponseDto predict(PredictionRequestDto request) {
        ResponseEntity<PredictionResponseDto> response = restTemplate.getForEntity(URI.create(host +
                "/v1/predict" +
                "?course_id=" + request.getCourseId() +
                "&school_year=" + request.getSchoolYear() +
                "&grade_item_position=" + request.getGradeItemPosition() +
                "&student_id=" + request.getStudentId()), PredictionResponseDto.class);
        return response.getBody();
    }
}
