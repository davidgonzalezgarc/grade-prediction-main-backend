package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.authentication.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface TokenRepository extends JpaRepository<Token, String> {

    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :userId and (t.expired = false or t.revoked = false)\s
            """)
    Set<Token> findAllValidTokenByUser(String userId);

    Optional<Token> findByToken(String token);
}
