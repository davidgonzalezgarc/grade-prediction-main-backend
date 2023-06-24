package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.user.persistence;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.authentication.persistence.Token;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.StudentCourseRelationship;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.course.persistence.TeacherCourseRelationship;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.grade.persistence.Grade;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private StudentInformation studentInformation;

    @OneToMany(mappedBy = "user")
    private Set<Token> tokens;

    @OneToMany(mappedBy = "student")
    private Set<StudentCourseRelationship> studentCoursRelationships;

    @OneToMany(mappedBy = "teacher")
    private Set<TeacherCourseRelationship> teacherCoursRelationships;

    @OneToMany(mappedBy = "student")
    private Set<Grade> grades;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
