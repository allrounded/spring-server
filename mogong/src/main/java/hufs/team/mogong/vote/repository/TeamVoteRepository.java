package hufs.team.mogong.vote.repository;

import hufs.team.mogong.vote.TeamVote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamVoteRepository extends JpaRepository<TeamVote, Long> {

	Optional<TeamVote> findByTeam_TeamId(Long teamId);
}
