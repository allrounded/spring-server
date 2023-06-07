package hufs.team.mogong.vote;

import hufs.team.mogong.team.Team;
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
		StringBuilder sbMon = new StringBuilder();
		StringBuilder sbTue = new StringBuilder();
		StringBuilder sbWed = new StringBuilder();
		StringBuilder sbThu = new StringBuilder();
		StringBuilder sbFri = new StringBuilder();
		StringBuilder sbSat = new StringBuilder();
		StringBuilder sbSun = new StringBuilder();
		for (int i = 0; i < vote[0].length; i++) {
			sbMon.append(Math.max(0, Character.getNumericValue(monCharArr[i]) + vote[0][i]*diff));
			sbTue.append(Math.max(0, Character.getNumericValue(tueCharArr[i]) + vote[1][i]*diff));
			sbWed.append(Math.max(0, Character.getNumericValue(wedCharArr[i]) + vote[2][i]*diff));
			sbThu.append(Math.max(0, Character.getNumericValue(thuCharArr[i]) + vote[3][i]*diff));
			sbFri.append(Math.max(0, Character.getNumericValue(friCharArr[i]) + vote[4][i]*diff));
			sbSat.append(Math.max(0, Character.getNumericValue(satCharArr[i]) + vote[5][i]*diff));
			sbSun.append(Math.max(0, Character.getNumericValue(sunCharArr[i]) + vote[6][i]*diff));
		}

		this.mon = sbMon.toString();
		this.tue = sbTue.toString();
		this.wed = sbWed.toString();
		this.thu = sbThu.toString();
		this.fri = sbFri.toString();
		this.sat = sbSat.toString();
		this.sun = sbSun.toString();
	}
}
