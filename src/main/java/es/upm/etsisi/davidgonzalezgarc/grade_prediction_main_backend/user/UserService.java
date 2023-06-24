package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.Role;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.StudentInformation;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final StudentInformationService studentInformationService;

    public User getReferenceById(String id) {
        return userRepository.getReferenceById(id);
    }

    public Optional<User> getById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        User savedUser = userRepository.save(user);
        if (savedUser.getRole() == Role.STUDENT) {
            studentInformationService.save(StudentInformation.builder()
                    .id(savedUser.getId()).build());
        }
        return savedUser;
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

}
