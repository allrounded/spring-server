package hufs.team.mogong.vote;

import hufs.team.mogong.team.Team;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamVote extends TimeTable {

	@Id
	@Column(name = "team_vote_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public TeamVote(Team team, List<Long> times) {
		this.team = team;
		this.mon = times.get(0);
		this.tue = times.get(1);
		this.wed = times.get(2);
		this.thu = times.get(3);
		this.fri = times.get(4);
		this.sat = times.get(5);
		this.sun = times.get(6);
	}

	public static TeamVote getDefault(Team team) {
		List<Long> times = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			times.add(0L);
		}
		return new TeamVote(team, times);
	}
}
