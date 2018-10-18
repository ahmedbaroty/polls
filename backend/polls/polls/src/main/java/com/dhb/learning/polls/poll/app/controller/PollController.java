package com.dhb.learning.polls.poll.app.controller;

import com.dhb.learning.polls.auth.server.payload.ApiResponse;
import com.dhb.learning.polls.auth.server.security.CurrentUser;
import com.dhb.learning.polls.auth.server.security.UserPrincipal;
import com.dhb.learning.polls.poll.app.model.Poll;
import com.dhb.learning.polls.poll.app.payload.request.PollRequest;
import com.dhb.learning.polls.poll.app.payload.request.VoteRequest;
import com.dhb.learning.polls.poll.app.payload.response.PollResponse;
import com.dhb.learning.polls.poll.app.service.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @GetMapping
    public List<PollResponse> getPolls(
            @CurrentUser UserPrincipal currentUser){
        return pollService.getAllPolls(currentUser);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) {

        System.out.println(pollRequest);
        Poll poll = pollService.createPoll(pollRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{pollId}")
                .buildAndExpand(poll.getId()).toUri();

        return ResponseEntity.created(location).body(
                new ApiResponse(true, "Poll Created Successfully"));

    }

    @GetMapping("/{pollId}")
    public PollResponse getPollById(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long pollId) {
        return pollService.getPollById(pollId, currentUser);
    }


    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse castVote(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable Long pollId,
            @Valid @RequestBody VoteRequest voteRequest) {

        return pollService.castVoteAndGetUpdatedPoll(pollId , voteRequest , currentUser);

    }
}
