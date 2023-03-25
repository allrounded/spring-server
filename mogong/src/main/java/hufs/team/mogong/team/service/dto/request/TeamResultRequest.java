package hufs.team.mogong.team.service.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TeamResultRequest {

	private String teamName;
	private Integer numberOfTeam;
	private List<ImageUrlRequest> images;
	private ImageUrlRequest resultImageUrl;

}
