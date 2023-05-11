package hufs.team.mogong.timetable.repository;

import hufs.team.mogong.timetable.TimeTableV1;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableV1Repository extends JpaRepository<TimeTableV1, Long> {

	Optional<TimeTableV1> findByMember_MemberId(Long memberId);
}
