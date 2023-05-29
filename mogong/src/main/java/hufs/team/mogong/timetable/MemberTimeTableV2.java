package hufs.team.mogong.timetable;

import hufs.team.mogong.member.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTimeTableV2 extends TimeTable {

	@Id
	@Column(name = "member_time_table_v2_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public MemberTimeTableV2(Member member, List<Long> times) {
		this.member = member;
		this.mon = times.get(0);
		this.tue = times.get(1);
		this.wed = times.get(2);
		this.thu = times.get(3);
		this.fri = times.get(4);
		this.sat = times.get(5);
		this.sun = times.get(6);
	}

	public static MemberTimeTableV2 getDefault(Member member) {
		List<Long> times = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			times.add(0L);
		}
		return new MemberTimeTableV2(member, times);
	}
}
