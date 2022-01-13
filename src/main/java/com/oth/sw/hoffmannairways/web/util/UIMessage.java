package com.oth.sw.hoffmannairways.web.util;

public class UIMessage {
    private String message;
    private String alertClass;
    //TODO source: https://stackoverflow.com/questions/46744586/thymeleaf-show-a-success-message-after-clicking-on-submit-button -->


    public UIMessage(String message, String alertClass) {
        this.message = message;
        this.alertClass = alertClass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlertClass() {
        return alertClass;
    }

    public void setAlertClass(String alertClass) {
        this.alertClass = alertClass;
    }
}
