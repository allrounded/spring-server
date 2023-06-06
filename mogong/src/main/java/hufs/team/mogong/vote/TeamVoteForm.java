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
public class TeamVoteForm {

	@Id
	@Column(name = "team_vote_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	private Integer divisorMinutes;

	private boolean duplicate;

	public TeamVoteForm(Team team, Integer divisorMinutes, boolean duplicate) {
		this.team = team;
		this.divisorMinutes = divisorMinutes;
		this.duplicate = duplicate;
	}
}
