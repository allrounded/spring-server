package hufs.team.mogong.team.repository;

import hufs.team.mogong.team.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

	Optional<Team> findByTeamName(String teamName);
}
