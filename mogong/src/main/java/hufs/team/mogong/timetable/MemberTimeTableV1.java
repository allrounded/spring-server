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
public class MemberTimeTableV1 {

	@Id
	@Column(name = "member_time_table_v1_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false)
	private String mon;

	@Column(nullable = false)
	private String tue;

	@Column(nullable = false)
	private String wed;

	@Column(nullable = false)
	private String thu;

	@Column(nullable = false)
	private String fri;

	@Column(nullable = false)
	private String sat;

	@Column(nullable = false)
	private String sun;

	public MemberTimeTableV1(Member member, List<String> times) {
		this.member = member;
		this.mon = times.get(0);
		this.tue = times.get(1);
		this.wed = times.get(2);
		this.thu = times.get(3);
		this.fri = times.get(4);
		this.sat = times.get(5);
		this.sun = times.get(6);
	}

	public static MemberTimeTableV1 getDefault(Member member, int timeLength) {
		StringBuilder sb = new StringBuilder();
		sb.append("0" .repeat(Math.max(0, timeLength)));

		List<String> times = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			times.add(sb.toString());
		}
		return new MemberTimeTableV1(member, times);
	}

	public void update(List<String> times) {
		this.mon = times.get(0);
		this.tue = times.get(1);
		this.wed = times.get(2);
		this.thu = times.get(3);
		this.fri = times.get(4);
		this.sat = times.get(5);
		this.sun = times.get(6);
	}

	public void accumulate(int[][] times, int timeLength) {
		for (int i = 0; i < timeLength; i++) {
			times[0][i] = Math.max(times[0][i], getNumericValue(mon.charAt(i)));
			times[1][i] = Math.max(times[1][i], getNumericValue(tue.charAt(i)));
			times[2][i] = Math.max(times[2][i], getNumericValue(wed.charAt(i)));
			times[3][i] = Math.max(times[3][i], getNumericValue(thu.charAt(i)));
			times[4][i] = Math.max(times[4][i], getNumericValue(fri.charAt(i)));
			times[5][i] = Math.max(times[5][i], getNumericValue(sat.charAt(i)));
			times[6][i] = Math.max(times[6][i], getNumericValue(sun.charAt(i)));
		}
	}

	private int getNumericValue(char ch) {
		if (Character.getNumericValue(ch) == Character.getNumericValue('0')) {
			return 0;
		}
		return 1;
	}
}
