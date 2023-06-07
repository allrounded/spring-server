package hufs.team.mogong.vote;

import hufs.team.mogong.member.Member;
import hufs.team.mogong.timetable.TimeTable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberVote extends TimeTable {

	@Id
	@Column(name = "member_vote_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public MemberVote(Member member, List<Long> times) {
		this.member = member;
		this.mon = times.get(0);
		this.tue = times.get(1);
		this.wed = times.get(2);
		this.thu = times.get(3);
		this.fri = times.get(4);
		this.sat = times.get(5);
		this.sun = times.get(6);
	}

	public void updateTimes(List<Long> times) {
		this.mon = times.get(0);
		this.tue = times.get(1);
		this.wed = times.get(2);
		this.thu = times.get(3);
		this.fri = times.get(4);
		this.sat = times.get(5);
		this.sun = times.get(6);
	}
}
