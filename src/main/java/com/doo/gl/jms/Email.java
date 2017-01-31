package com.doo.gl.jms;


public class Email {

    private String from;
    private String to;

    public Email() {
    }

    public Email(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
