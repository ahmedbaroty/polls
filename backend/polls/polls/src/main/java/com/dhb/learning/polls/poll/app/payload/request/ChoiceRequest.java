package com.dhb.learning.polls.poll.app.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChoiceRequest {

    @NotBlank
    @Size(min = 1 , max = 40)
    private String text;

    public ChoiceRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
