package hufs.team.mogong.vote.service;

import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.exception.NotFoundTeamIdException;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.vote.TeamVote;
import hufs.team.mogong.vote.exception.AlreadyExistsTeamVote;
import hufs.team.mogong.vote.exception.NotFoundTeamVoteException;
import hufs.team.mogong.vote.repository.TeamVoteRepository;
import hufs.team.mogong.vote.service.dto.request.VoteFormRequest;
import hufs.team.mogong.vote.service.dto.response.VoteFormResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class VoteService {

	private final TeamRepository teamRepository;
	private final TeamVoteRepository teamVoteRepository;

	public VoteService(TeamRepository teamRepository, TeamVoteRepository teamVoteRepository) {
		this.teamRepository = teamRepository;
		this.teamVoteRepository = teamVoteRepository;
	}

	@Transactional
	public VoteFormResponse createVoteForm(Long teamId, VoteFormRequest request) {
		Team team = findTeamByTeamId(teamId);
		checkAlreadyExistsTeamVote(teamId);
		log.debug("TEAM ID = {}",team.getTeamId());

		TeamVote savedTeamVote = teamVoteRepository.save(
			new TeamVote(team, request.getDivisorMinutes(), request.isDuplicate())
		);

		return new VoteFormResponse(
			savedTeamVote.getTeam().getTeamId(),
			savedTeamVote.getDivisorMinutes(),
			savedTeamVote.isDuplicate());
	}

	public VoteFormResponse findVoteForm(Long teamId) {
		TeamVote teamVote = findTeamVoteByTeamId(teamId);
		return new VoteFormResponse(
			teamVote.getTeam().getTeamId(),
			teamVote.getDivisorMinutes(),
			teamVote.isDuplicate());
	}

	private void checkAlreadyExistsTeamVote(Long teamId) {
		Optional<TeamVote> teamVote = teamVoteRepository.findByTeam_TeamId(teamId);
		if (teamVote.isPresent()) {
			throw new AlreadyExistsTeamVote();
		}
	}

	private Team findTeamByTeamId(Long teamId){
		return teamRepository.findById(teamId)
			.orElseThrow(NotFoundTeamIdException::new);
	}

	private TeamVote findTeamVoteByTeamId(Long teamId){
		return teamVoteRepository.findByTeam_TeamId(teamId)
			.orElseThrow(NotFoundTeamVoteException::new);
	}
}
