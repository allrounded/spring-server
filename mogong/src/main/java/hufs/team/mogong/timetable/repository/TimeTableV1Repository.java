package hufs.team.mogong.timetable.repository;

import hufs.team.mogong.timetable.MemberTimeTableV1;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableV1Repository extends JpaRepository<MemberTimeTableV1, Long> {

	Optional<MemberTimeTableV1> findByMember_MemberId(Long memberId);
}
