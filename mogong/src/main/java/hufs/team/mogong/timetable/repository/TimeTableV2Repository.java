package hufs.team.mogong.timetable.repository;

import hufs.team.mogong.timetable.TimeTableV2;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableV2Repository extends JpaRepository<TimeTableV2, Long> {

	Optional<TimeTableV2> findByMember_MemberId(Long memberId);
}
