package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.rest;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.StudentInformationService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.UserService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.StudentInformation;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence.User;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.rest.dto.StudentInformationDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    private final StudentInformationService studentInformationService;

    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable String id) {
        Optional<User> optUser = userService.getById(id);
        if (optUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User user = optUser.get();
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<StudentInformationDto> getInformation(@AuthenticationPrincipal User user) {
        Optional<StudentInformation> studentInformationOpt = studentInformationService.getById(user.getId());
        if (studentInformationOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        StudentInformation studentInformation = studentInformationOpt.get();
        StudentInformationDto dto = modelMapper.map(studentInformation, StudentInformationDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/info")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> editInformation(@AuthenticationPrincipal User user, @RequestBody StudentInformationDto request) {
        Optional<StudentInformation> studentInformationOpt = studentInformationService.getById(user.getId());
        if (studentInformationOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        StudentInformation studentInformation = studentInformationOpt.get();
        modelMapper.map(request, studentInformation);
        studentInformationService.save(studentInformation);
        return ResponseEntity.ok().build();
    }

}
