package hufs.team.mogong.vote.repository;

import hufs.team.mogong.vote.TeamVoteForm;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamVoteFormRepository extends JpaRepository<TeamVoteForm, Long> {

	Optional<TeamVoteForm> findByTeam_TeamId(Long teamId);
}
