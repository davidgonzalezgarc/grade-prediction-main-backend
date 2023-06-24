package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}
