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
	private Long teamId;

	@Column(nullable = false, unique = true)
	private String teamName;

	@Column(nullable = false)
	private Integer numberOfMember;

	@Column(nullable = false)
	private String authCode;

	public Team(String teamName, Integer numberOfMember, String authCode) {
		this.teamName = teamName;
		this.numberOfMember = numberOfMember;
		this.authCode = authCode;
	}
}
