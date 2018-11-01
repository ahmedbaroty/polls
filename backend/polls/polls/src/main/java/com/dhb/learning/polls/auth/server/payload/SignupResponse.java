package com.dhb.learning.polls.auth.server.payload;

public class SignupResponse  {

    private Boolean success;
    private String message;
    private Long userId;

    public SignupResponse(Boolean success, String message, Long userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}