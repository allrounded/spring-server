package hufs.team.mogong.timetable.repository;

import hufs.team.mogong.timetable.MemberTimeTableV2;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTimeTableV2Repository extends JpaRepository<MemberTimeTableV2, Long> {

	Optional<MemberTimeTableV2> findByMember_MemberId(Long memberId);
}
