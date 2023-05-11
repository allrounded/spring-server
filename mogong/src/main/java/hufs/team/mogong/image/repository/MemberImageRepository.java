package hufs.team.mogong.image.repository;

import hufs.team.mogong.image.MemberImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

	Optional<MemberImage> findByMember_MemberId(Long memberId);
}
