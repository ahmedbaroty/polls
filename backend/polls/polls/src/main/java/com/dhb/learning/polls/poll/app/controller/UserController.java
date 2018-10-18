package com.dhb.learning.polls.poll.app.controller;


import com.dhb.learning.polls.auth.server.model.User;
import com.dhb.learning.polls.auth.server.repository.UserRepository;
import com.dhb.learning.polls.auth.server.security.CurrentUser;
import com.dhb.learning.polls.auth.server.security.UserPrincipal;
import com.dhb.learning.polls.common.exception.ResourceNotFoundException;
import com.dhb.learning.polls.poll.app.payload.response.*;
import com.dhb.learning.polls.poll.app.repository.PollRepository;
import com.dhb.learning.polls.poll.app.repository.VoteRepository;
import com.dhb.learning.polls.poll.app.service.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return new UserSummary(userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getName());
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable String username) {

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("USer", "ID", username)
        );

        Long voteCount = voteRepository.countByUserId(user.getId());

        Long pollCount = pollRepository.countByCreateBy(user.getId());

        UserProfile userProfile = new UserProfile();
        userProfile.setId(user.getId());
        userProfile.setJoinedAt(user.getCreatedAt());
        userProfile.setName(user.getName());
        userProfile.setUsername(user.getUsername());
        userProfile.setPollCount(pollCount);
        userProfile.setVoteCount(voteCount);

        return userProfile;
    }

    @GetMapping("/users/polls")
    public List<PollResponse> getPollsCreatedBy(@RequestParam(value = "username") String username,
                                                @CurrentUser UserPrincipal currentUser) {
        System.out.println("getPollsCreatedBy");
        return pollService.getPollsCreatedBy(username, currentUser);
    }

    @GetMapping("/users/votes")
    public List<PollResponse> getPollsVotedBy(@RequestParam(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser) {
        return pollService.getPollsVotedBy(username, currentUser);
    }
}