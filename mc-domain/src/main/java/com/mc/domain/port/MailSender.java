package com.mc.domain.port;

import java.util.List;

public interface MailSender {

    /**
     * Sends an email with the specified recipient, subject, and body.
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param body    the body content of the email
     */
    void send(String to, String subject, String body);

    void send(String[] to, String subject, String body);
}
