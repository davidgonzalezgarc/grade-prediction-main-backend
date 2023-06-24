package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentInformationRepository extends JpaRepository<StudentInformation, String> {
}
