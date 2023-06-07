package hufs.team.mogong.vote;

import hufs.team.mogong.member.Member;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.timetable.TimeTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamVote {

	@Id
	@Column(name = "team_vote_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

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

	public TeamVote(Team team, String[] times) {
		this.team = team;
		this.mon = times[0];
		this.tue = times[1];
		this.wed = times[2];
		this.thu = times[3];
		this.fri = times[4];
		this.sat = times[5];
		this.sun = times[6];
	}

	public void apply(int[][] vote, int diff) {
		char[] monCharArr = this.mon.toCharArray();
		char[] tueCharArr = this.tue.toCharArray();
		char[] wedCharArr = this.wed.toCharArray();
		char[] thuCharArr = this.thu.toCharArray();
		char[] friCharArr = this.fri.toCharArray();
		char[] satCharArr = this.sat.toCharArray();
		char[] sunCharArr = this.sun.toCharArray();

		for (int i = 0; i < vote[0].length; i++) {
			monCharArr[i] += vote[0][i]*diff;
			tueCharArr[i] += vote[1][i]*diff;
			wedCharArr[i] += vote[2][i]*diff;
			thuCharArr[i] += vote[3][i]*diff;
			friCharArr[i] += vote[4][i]*diff;
			satCharArr[i] += vote[5][i]*diff;
			sunCharArr[i] += vote[6][i]*diff;
		}

		this.mon = Arrays.toString(monCharArr);
		this.tue = Arrays.toString(tueCharArr);
		this.wed = Arrays.toString(wedCharArr);
		this.thu = Arrays.toString(thuCharArr);
		this.fri = Arrays.toString(friCharArr);
		this.sat = Arrays.toString(satCharArr);
		this.sun = Arrays.toString(sunCharArr);
	}
}
