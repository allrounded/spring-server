package hufs.team.mogong.team;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String teamId;

	@Column(nullable = false)
	private Integer numberOfMember;

	public Team(String teamId, Integer numberOfMember) {
		this.teamId = teamId;
		this.numberOfMember = numberOfMember;
	}
}
