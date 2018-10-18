package com.dhb.learning.polls.poll.app.payload.request;

import javax.validation.constraints.Size;

import java.util.List;

public class PollRequest {

    @Size(max=140)
    private String question;

    @Size(min=2 , max=6)
    private List<ChoiceRequest> choices;

    private PollLength pollLength;

    public PollRequest() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<ChoiceRequest> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceRequest> choices) {
        this.choices = choices;
    }

    public PollLength getPollLength() {
        return pollLength;
    }

    public void setPollLength(PollLength pollLength) {
        this.pollLength = pollLength;
    }
}
