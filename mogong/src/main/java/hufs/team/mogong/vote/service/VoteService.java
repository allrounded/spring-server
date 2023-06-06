package hufs.team.mogong.vote.service;

import hufs.team.mogong.member.Member;
import hufs.team.mogong.member.exception.NotFoundMemberIdException;
import hufs.team.mogong.member.repository.MemberRepository;
import hufs.team.mogong.team.DayOfWeek;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.exception.NotFoundTeamIdException;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.TimeTableRequest;
import hufs.team.mogong.team.service.dto.response.TimeResponses;
import hufs.team.mogong.team.service.dto.response.TimeTableResponse;
import hufs.team.mogong.timetable.TeamTimeTable;
import hufs.team.mogong.timetable.TimeTable;
import hufs.team.mogong.timetable.exception.NotFoundTeamTimeTableException;
import hufs.team.mogong.timetable.repository.TeamTimeTableRepository;
import hufs.team.mogong.vote.MemberVote;
import hufs.team.mogong.vote.TeamVote;
import hufs.team.mogong.vote.TeamVoteForm;
import hufs.team.mogong.vote.exception.AlreadyExistMemberVote;
import hufs.team.mogong.vote.exception.AlreadyExistsTeamVote;
import hufs.team.mogong.vote.exception.NotFoundTeamVoteException;
import hufs.team.mogong.vote.repository.MemberVoteRepository;
import hufs.team.mogong.vote.repository.TeamVoteFormRepository;
import hufs.team.mogong.vote.repository.TeamVoteRepository;
import hufs.team.mogong.vote.service.dto.request.MemberVoteRequest;
import hufs.team.mogong.vote.service.dto.request.VoteFormRequest;
import hufs.team.mogong.vote.service.dto.response.MemberVoteResponse;
import hufs.team.mogong.vote.service.dto.response.TeamTotalVotesResponse;
import hufs.team.mogong.vote.service.dto.response.VoteFormResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class VoteService {

	private static final int DIVISOR_MINUTES = 30;
	private static final int DAY_OF_WEEK = 7;
	private static final int TIME_LENGTH = 30;
	private static final int ZERO_NUMERIC_VALUE = Character.getNumericValue('0');

	private final MemberRepository memberRepository;
	private final TeamRepository teamRepository;
	private final TeamVoteFormRepository teamVoteFormRepository;
	private final TeamTimeTableRepository teamTimeTableRepository;
	private final TeamVoteRepository teamVoteRepository;
	private final MemberVoteRepository memberVoteRepository;

	public VoteService(MemberRepository memberRepository, TeamRepository teamRepository,
		TeamVoteFormRepository teamVoteFormRepository,
		TeamTimeTableRepository teamTimeTableRepository,
		TeamVoteRepository teamVoteRepository, MemberVoteRepository memberVoteRepository) {
		this.memberRepository = memberRepository;
		this.teamRepository = teamRepository;
		this.teamVoteFormRepository = teamVoteFormRepository;
		this.teamTimeTableRepository = teamTimeTableRepository;
		this.teamVoteRepository = teamVoteRepository;
		this.memberVoteRepository = memberVoteRepository;
	}

	@Transactional
	public VoteFormResponse createVoteForm(Long teamId, VoteFormRequest request) {
		Team team = findTeamByTeamId(teamId);
		checkAlreadyExistsTeamVote(teamId);
		log.debug("TEAM ID = {}",team.getTeamId());

		TeamVoteForm savedTeamVoteForm = teamVoteFormRepository.save(
			new TeamVoteForm(team, request.getDivisorMinutes(), request.isDuplicate())
		);

		return new VoteFormResponse(
			savedTeamVoteForm.getTeam().getTeamId(),
			savedTeamVoteForm.getDivisorMinutes(),
			savedTeamVoteForm.isDuplicate());
	}

	public VoteFormResponse findVoteForm(Long teamId) {
		TeamVoteForm teamVoteForm = findTeamVoteFormByTeamId(teamId);
		return new VoteFormResponse(
			teamVoteForm.getTeam().getTeamId(),
			teamVoteForm.getDivisorMinutes(),
			teamVoteForm.isDuplicate());
	}

	private void checkAlreadyExistsTeamVote(Long teamId) {
		Optional<TeamVoteForm> teamVote = teamVoteFormRepository.findByTeam_TeamId(teamId);
		if (teamVote.isPresent()) throw new AlreadyExistsTeamVote();
	}

	private Team findTeamByTeamId(Long teamId){
		return teamRepository.findById(teamId)
			.orElseThrow(NotFoundTeamIdException::new);
	}

	private TeamVoteForm findTeamVoteFormByTeamId(Long teamId){
		return teamVoteFormRepository.findByTeam_TeamId(teamId)
			.orElseThrow(NotFoundTeamVoteException::new);
	}

	@Transactional
	public MemberVoteResponse create(Long teamId, Long memberId, MemberVoteRequest request) {
		log.debug("TEAM ID={}, MEMBER ID={}", teamId, memberId);
		TeamTimeTable teamTimeTable = findTeamTimeTableByTeamId(teamId);
		TimeResponses teamTimeTableResponse = getTimeResponses(teamTimeTable);
		log.debug("TEAM TIME TABLE 조회 완료");


		TeamVote teamVote = getTeamVote(teamId);
		log.debug("TEAM VOTE 조회 완료(없으면 새로 생성)");

		TeamTotalVotesResponse teamTotalVotesResponse = new TeamTotalVotesResponse(
			teamId,
			teamTimeTableResponse,
			getTimeResponses(teamVote)
		);
		log.debug("TEAM TOTAL VOTES 생성 완료");

		MemberVote savedMemberVote = saveMemberVote(memberId, request);
		log.debug("MEMBER VOTE 생성 완료");

		return new MemberVoteResponse(
			teamTotalVotesResponse,
			memberId,
			getTimeResponses(savedMemberVote)
		);
	}

	private TeamVote getTeamVote(Long teamId) {
		Team team = findTeamByTeamId(teamId);
		Optional<TeamVote> tempTeamVote = teamVoteRepository.findByTeam_TeamId(teamId);
		return tempTeamVote.orElseGet(() -> teamVoteRepository.save(new TeamVote(team, initTeamVote())));
	}

	private MemberVote saveMemberVote(Long memberId, MemberVoteRequest request) {
		checkMemberVoteAlreadyExist(memberId);
		Member member = findMemberByMemberId(memberId);
		return memberVoteRepository.save(new MemberVote(member, getMemberVoteTimes(request)));
	}

	private List<Long> getMemberVoteTimes(MemberVoteRequest request) {
		TimeTableRequest[] timeTableRequests = request.getTimeRequests().getTimes();
		List<Long> times = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (TimeTableRequest timeTable : timeTableRequests) {
			int[] time = timeTable.getTime();
			sb.setLength(0);
			for (int t : time) {
				sb.append(t);
			}
			times.add(Long.parseLong(sb.toString(), 2));
		}
		return times;
	}

	private void checkMemberVoteAlreadyExist(Long memberId) {
		Optional<MemberVote> memberVote = memberVoteRepository.findByMember_MemberId(memberId);
		if (memberVote.isPresent()) {
			throw new AlreadyExistMemberVote();
		}
	}

	private TimeResponses getTimeResponses(TimeTable timeTable) {
		int[][] times = changeTimesToIntArray(getLongArray(timeTable));
		return new TimeResponses(DIVISOR_MINUTES, getTimeTableResponses(times));
	}

	private List<Long> initTeamVote() {
		List<Long> teamVoteList = new ArrayList<>();
		for (int i = 0; i < DAY_OF_WEEK; i++) {
			teamVoteList.add(0L);
		}
		return teamVoteList;
	}

	private long[] getLongArray(TimeTable timeTable) {
		long[] table = new long[DAY_OF_WEEK];
		table[0] = timeTable.getMon();
		table[1] = timeTable.getTue();
		table[2] = timeTable.getWed();
		table[3] = timeTable.getThu();
		table[4] = timeTable.getFri();
		table[5] = timeTable.getSat();
		table[6] = timeTable.getSun();
		return table;
	}

	private int[][] changeTimesToIntArray(long[] times) {
		int idx = 0;
		int[][] renewalTimes = new int[DAY_OF_WEEK][TIME_LENGTH];
		for (long time : times) {
			char[] chars = Long.toBinaryString(time).toCharArray();
			for (int i = 0; i < chars.length ; i++) {
				renewalTimes[idx][TIME_LENGTH-1-i] = Character.getNumericValue(chars[chars.length-1-i]) - ZERO_NUMERIC_VALUE;
			}
			idx++;
		}

		return renewalTimes;
	}

	private TimeTableResponse[] getTimeTableResponses(int[][] times) {
		TimeTableResponse[] timeTables = new TimeTableResponse[DAY_OF_WEEK];
		timeTables[0] = new TimeTableResponse(DayOfWeek.MON, times[0]);
		timeTables[1] = new TimeTableResponse(DayOfWeek.TUE, times[1]);
		timeTables[2] = new TimeTableResponse(DayOfWeek.WED, times[2]);
		timeTables[3] = new TimeTableResponse(DayOfWeek.THU, times[3]);
		timeTables[4] = new TimeTableResponse(DayOfWeek.FRI, times[4]);
		timeTables[5] = new TimeTableResponse(DayOfWeek.SAT, times[5]);
		timeTables[6] = new TimeTableResponse(DayOfWeek.SUN, times[6]);
		return timeTables;
	}

	private TeamTimeTable findTeamTimeTableByTeamId(Long teamId) {
		return teamTimeTableRepository.findByTeam_TeamId(teamId)
			.orElseThrow(NotFoundTeamTimeTableException::new);
	}

	private Member findMemberByMemberId(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(NotFoundMemberIdException::new);
	}
}
