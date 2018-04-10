package org.hamster.sunflower_v2.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * Created by ONB-CZEIDE on 03/23/2018
 */
@Component
public class SunflowerSmtpMailSender {

    private JavaMailSender javaMailSender;

    public static final String senderEmail = "sunflower.hamster.apps@gmail.com";
    public static final String senderName = "Sunflower Team";

    public static final String signatureLine =
            "Sunflower Team<br />" +
            "P.S. Need help? <a href='mailto:sunflower.hamster.apps@gmail.com'>Contact us</a> anytime with your questions and/or feedback. ";

    @Autowired
    public SunflowerSmtpMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void send(String to, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        MimeMessagePreparator mailMessage = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(
                    mimeMessage, true, "UTF-8");

            message.setFrom(senderEmail, senderName);
            message.setTo(to);
            message.setSubject(subject);
            message.setText("", body);
        };

        javaMailSender.send(mailMessage);
    }
}
