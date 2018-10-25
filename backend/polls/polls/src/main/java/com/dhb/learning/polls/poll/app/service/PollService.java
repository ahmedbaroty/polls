package com.dhb.learning.polls.poll.app.service;

import com.dhb.learning.polls.auth.server.model.User;
import com.dhb.learning.polls.auth.server.repository.UserRepository;
import com.dhb.learning.polls.auth.server.security.UserPrincipal;
import com.dhb.learning.polls.common.exception.BadRequestException;
import com.dhb.learning.polls.common.exception.ResourceNotFoundException;
import com.dhb.learning.polls.poll.app.model.Choice;
import com.dhb.learning.polls.poll.app.model.ChoiceVoteCount;
import com.dhb.learning.polls.poll.app.model.Poll;
import com.dhb.learning.polls.poll.app.model.Vote;
import com.dhb.learning.polls.poll.app.payload.request.PollRequest;
import com.dhb.learning.polls.poll.app.payload.request.VoteRequest;
import com.dhb.learning.polls.poll.app.payload.response.PollResponse;
import com.dhb.learning.polls.poll.app.repository.PollRepository;
import com.dhb.learning.polls.poll.app.repository.VoteRepository;
import com.dhb.learning.polls.poll.app.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(PollService.class);

    public List<PollResponse> getAllPolls(UserPrincipal currentUser) {

        // Retrieve polls
        List<Poll> polls = pollRepository.findAll();

        // check if empty
        if (polls.size() == 0) {
            return Collections.emptyList();
        }

        // Map Polls to PollResponses containing vote counts and poll creator details

        List<Long> pollIds = polls.stream().map(poll -> poll.getId()).collect(Collectors.toList());

        Map<Long, Long> choiceVoteCountMap = getChoicesVoteCountMap(pollIds);

        Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);

        Map<Long, User> creatorMap = getPollCreatorMap(polls);

        List<PollResponse> pollResponses = polls.stream().map(poll -> {

            return
                    ModelMapper
                            .mapPollToPollResponse(
                                    poll,
                                    choiceVoteCountMap,
                                    creatorMap.get(poll.getCreateBy()),
                                    pollUserVoteMap == null ?
                                            null :
                                            pollUserVoteMap.
                                                    getOrDefault(
                                                            poll.getId(), null));
        }).collect(Collectors.toList());

        return pollResponses;
    }

    public List<PollResponse> getPollsCreatedBy(
            String username,
            UserPrincipal currentUser) {

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new ResourceNotFoundException("User" , "username" , username)
        );

        // Retrieve all polls created by the given username
        List<Poll> polls = pollRepository.findByCreateBy(user.getId());

        if(polls.size() == 0 ){
            return Collections.emptyList();
        }

        // Map Polls to PollResponses containing vote counts and poll creator details
        List<Long> pollIds = polls.stream().map(Poll::getId).collect(Collectors.toList());
        Map<Long, Long> choiceVoteCountMap = getChoicesVoteCountMap(pollIds);
        Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);

        List<PollResponse> pollResponses = polls.stream().map(
                poll -> {
                    return ModelMapper.mapPollToPollResponse(
                            poll,
                            choiceVoteCountMap,
                            user,
                            pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
                }).collect(Collectors.toList());

        return pollResponses;
    }

    public List<PollResponse> getPollsVotedBy(String username, UserPrincipal currentUser) {

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new ResourceNotFoundException("USER" , "username" , username)
        );

        // Retrieve all pollIds in which the given username has voted
        List<Long> userVotedPollIds = voteRepository.findVotedPollIdsByUserId(user.getId());

        if (userVotedPollIds.size() == 0) {
            return Collections.emptyList();
        }

        // Retrieve all poll details from the voted pollIds.

        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        List<Poll> polls = pollRepository.findByIdIn(userVotedPollIds, sort);

        Map<Long, Long> choiceVoteCountMap = getChoicesVoteCountMap(userVotedPollIds);
        Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, userVotedPollIds);
        Map<Long, User> creatorMap = getPollCreatorMap(polls);

        List<PollResponse> pollResponses = polls.stream().map(
                poll -> {
                    return ModelMapper.mapPollToPollResponse(
                            poll,
                            choiceVoteCountMap,
                            creatorMap.get(poll.getCreateBy()),
                            pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
                }).collect(Collectors.toList());

        return pollResponses;
    }

    public Poll createPoll(PollRequest pollRequest) {
        Poll poll = new Poll();
        poll.setQuestion(pollRequest.getQuestion());

        pollRequest.getChoices().forEach(choiceRequest -> {
            Choice choice = new Choice();
            choice.setText(choiceRequest.getText());
            poll.addChoice(choice);
        });

        Instant now = Instant.now();
        Instant expirationDateTime =
                now
                        .plus(Duration.ofDays(pollRequest.getPollLength().getDays()))
                        .plus(Duration.ofHours(pollRequest.getPollLength().getHours()));

        poll.setExpirationDateTime(expirationDateTime);
        return pollRepository.save(poll);
    }


    public PollResponse getPollById(Long pollId, UserPrincipal currentUser) {

        Poll poll = pollRepository.findById(pollId).orElseThrow(
                () -> new ResourceNotFoundException("Poll ", "ID = ", pollId)
        );

        // Retrieve Vote Counts of every choice belonging to the current poll
        List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);

        Map<Long, Long> choiceVoteMap = votes.stream()
                .collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

        // Retrieve poll creator details
        User creator = userRepository.findById(poll.getCreateBy()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", poll.getCreateBy())
        );

        // Retrieve vote done by logged in user
        Vote userVote = null;
        if (currentUser != null) {
            userVote = voteRepository.findByUserIdAndPollId(currentUser.getId(), pollId);
        }

        return ModelMapper.mapPollToPollResponse(
                poll, choiceVoteMap, creator, userVote == null ? null : userVote.getChoice().getId()
        );

    }

    public PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser) {

        Poll poll = pollRepository.findById(pollId).orElseThrow(
                () -> new ResourceNotFoundException("Poll", "ID", pollId));

        if (poll.getExpirationDateTime().isBefore(Instant.now())) {
            throw new BadRequestException("Sorry! This Poll has already Expired", null);
        }

        User user = userRepository.getOne(currentUser.getId());

        Choice selectedChoice =
                poll.getChoices()
                        .stream()
                        .filter(choice -> choice.getId().equals(voteRequest.getChoiceId()))
                        .findFirst().orElseThrow(
                        () -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));

        Vote vote = new Vote();
        vote.setChoice(selectedChoice);
        vote.setUser(user);
        vote.setPoll(poll);

        try {
            vote = voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) {
            logger.info("User {} has already voted in Poll {}", currentUser.getId(), pollId);
            throw new BadRequestException("Sorry! You have already cast your vote in this poll", null);
        }

        //-- Vote Saved, Return the updated Poll Response now --

        // Retrieve Vote Counts of every choice belonging to the current poll
        List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(poll.getId());

        Map<Long, Long> choiceVoteMap = votes.stream().collect(
                Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount)
        );

        // Retrieve poll creator details
        User creator = userRepository.findById(poll.getCreateBy()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", poll.getCreateBy())
        );

        return ModelMapper.mapPollToPollResponse(poll, choiceVoteMap, creator, vote.getChoice().getId());
    }


    private Map<Long, User> getPollCreatorMap(List<Poll> polls) {
        List<Long> creatorsIds =
                polls.stream()
                        .map(Poll::getCreateBy)
                        .distinct()
                        .collect(Collectors.toList());

        List<User> users = userRepository.findByIdIn(creatorsIds);

        Map<Long, User> creatorMap =
                users
                        .stream()
                        .collect(
                                Collectors
                                        .toMap(User::getId, Function.identity())
                        );

        return creatorMap;
    }

    private Map<Long, Long> getPollUserVoteMap(UserPrincipal currentUser, List<Long> pollIds) {
        // Retrieve Votes done by the logged in user to the given pollIds
        Map<Long, Long> pollUserVoteMap = new HashMap<>();
        if (currentUser != null) {
            List<Vote> userVotes = voteRepository.findByUserIdAndPollIdIn(currentUser.getId(), pollIds);

            pollUserVoteMap =
                    userVotes
                            .stream()
                            .collect(
                                    Collectors
                                            .toMap(
                                                    vote -> vote.getPoll().getId(),
                                                    vote -> vote.getChoice().getId()
                                            )
                            );
        }
        return pollUserVoteMap;
    }

    private Map<Long, Long> getChoicesVoteCountMap(List<Long> pollIds) {

        List<ChoiceVoteCount> votes = voteRepository.countByPollIdInGroupByChoiceId(pollIds);

        Map<Long, Long> choicesvoteMap = votes.stream().collect(Collectors.toMap(
                ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount
        ));
        return choicesvoteMap;
    }


}
