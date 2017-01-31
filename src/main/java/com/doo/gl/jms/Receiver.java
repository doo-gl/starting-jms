package com.doo.gl.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    public static final String LISTENER_NAME = "mailbox";

    @JmsListener(destination = LISTENER_NAME, containerFactory = Application.CONTAINER_FACTORY)
    public void readEmail(Email email) {
        System.out.println("From: " + email.getFrom() + ", To: " + email.getTo());
    }

}
