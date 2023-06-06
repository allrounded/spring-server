package hufs.team.mogong.timetable.repository;

import hufs.team.mogong.timetable.TeamTimeTable;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamTimeTableRepository extends JpaRepository<TeamTimeTable, Long> {

	Optional<TeamTimeTable> findByTeam_TeamId(Long teamId);
}

