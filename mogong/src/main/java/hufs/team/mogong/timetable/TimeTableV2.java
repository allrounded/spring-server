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
public class TimeTableV2 {

	@Id
	@Column(name = "time_table_v2_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false)
	private Long mon;

	@Column(nullable = false)
	private Long tue;

	@Column(nullable = false)
	private Long wed;

	@Column(nullable = false)
	private Long thu;

	@Column(nullable = false)
	private Long fri;

	@Column(nullable = false)
	private Long sat;

	@Column(nullable = false)
	private Long sun;

	public TimeTableV2(Member member, List<Long> times) {
		this.member = member;
		this.mon = times.get(0);
		this.tue = times.get(1);
		this.wed = times.get(2);
		this.thu = times.get(3);
		this.fri = times.get(4);
		this.sat = times.get(5);
		this.sun = times.get(6);
	}

	public static TimeTableV2 getDefault(Member member) {
		List<Long> times = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			times.add(0L);
		}
		return new TimeTableV2(member, times);
	}

	public void update(List<Long> times) {
		this.mon = times.get(0);
		this.tue = times.get(1);
		this.wed = times.get(2);
		this.thu = times.get(3);
		this.fri = times.get(4);
		this.sat = times.get(5);
		this.sun = times.get(6);
	}

	public void accumulate(long[] times) {
		times[0] |= this.mon;
		times[1] |= this.tue;
		times[2] |= this.wed;
		times[3] |= this.thu;
		times[4] |= this.fri;
		times[5] |= this.sat;
		times[6] |= this.sun;
	}
}
