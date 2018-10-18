package com.dhb.learning.polls.poll.app.util;

import com.dhb.learning.polls.auth.server.model.User;
import com.dhb.learning.polls.poll.app.model.Poll;
import com.dhb.learning.polls.poll.app.payload.response.ChoiceResponse;
import com.dhb.learning.polls.poll.app.payload.response.PollResponse;
import com.dhb.learning.polls.poll.app.payload.response.UserSummary;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelMapper {

    public static PollResponse mapPollToPollResponse(
            Poll poll,
            Map<Long, Long> choiceVoteMap,
            User creator,
            Long userVote) {

        PollResponse pollResponse = new PollResponse();

        // set poll info
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());

        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(Instant.now()));

        // set choices ant their counts
        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(
                (choice) -> {
                    ChoiceResponse choiceResponse = new ChoiceResponse();
                    choiceResponse.setId(choice.getId());
                    choiceResponse.setText(choice.getText());

                    if (choiceVoteMap.containsKey(choice.getId())) {
                        choiceResponse.setVoteCount(choiceVoteMap.get(choice.getId()));
                    } else {
                        choiceResponse.setVoteCount(0L);
                    }
                    return choiceResponse;
                }
        ).collect(Collectors.toList());


        pollResponse.setChoices(choiceResponses);
        // set user summary
        pollResponse.setUserSummary(
                new UserSummary(
                        creator.getId(),
                        creator.getUsername(),
                        creator.getName()
                )
        );

        if(userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }

        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse :: getVoteCount).sum();

        pollResponse.setTotalVotes(totalVotes);

        return pollResponse;
    }

}
