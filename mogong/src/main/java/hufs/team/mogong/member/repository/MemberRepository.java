package hufs.team.mogong.member.repository;

import hufs.team.mogong.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findAllByTeam_TeamId(Long teamId);
}
