package hufs.team.mogong.vote.repository;

import hufs.team.mogong.vote.MemberVote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberVoteRepository extends JpaRepository<MemberVote, Long> {

	Optional<MemberVote> findByMember_MemberId(Long memberId);
}
