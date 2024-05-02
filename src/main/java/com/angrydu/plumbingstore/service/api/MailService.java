package com.angrydu.plumbingstore.service.api;

public interface MailService {
    void sendEmail(String to, String subject, String text);
}
