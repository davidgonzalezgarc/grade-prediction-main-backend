package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.StudentInformation;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.StudentInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentInformationService {
    private final StudentInformationRepository studentInformationRepository;

    public StudentInformation getReferenceById(String id) {
        return studentInformationRepository.getReferenceById(id);
    }

    public Optional<StudentInformation> getById(String id) {
        return studentInformationRepository.findById(id);
    }

    public void save(StudentInformation studentInformation) {
        studentInformationRepository.save(studentInformation);
    }

    public void delete(StudentInformation studentInformation) {
        studentInformationRepository.delete(studentInformation);
    }

}
