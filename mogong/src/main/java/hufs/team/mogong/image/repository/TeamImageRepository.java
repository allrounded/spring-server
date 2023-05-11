package hufs.team.mogong.image.repository;

import hufs.team.mogong.image.TeamImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamImageRepository extends JpaRepository<TeamImage, Long> {

	Optional<TeamImage> findByTeam_TeamId(Long teamId);
}
