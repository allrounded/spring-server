package hufs.team.mogong.image.repository;

import hufs.team.mogong.image.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

	List<Image> findAllByTeam_TeamId(Long teamId);
}
