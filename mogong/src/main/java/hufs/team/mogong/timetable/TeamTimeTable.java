package hufs.team.mogong.timetable;


import hufs.team.mogong.team.Team;
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
public class TeamTimeTable extends TimeTable {

	@Id
	@Column(name = "team_time_table_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public void update(long[] times) {
		this.mon = times[0];
		this.tue = times[1];
		this.wed = times[2];
		this.thu = times[3];
		this.fri = times[4];
		this.sat = times[5];
		this.sun = times[6];
	}

	public TeamTimeTable(Team team, long[] times) {
		this.team = team;
		this.mon = times[0];
		this.tue = times[1];
		this.wed = times[2];
		this.thu = times[3];
		this.fri = times[4];
		this.sat = times[5];
		this.sun = times[6];
	}
}
