package com.dhb.learning.polls.auth.server;

import com.dhb.learning.polls.auth.server.model.User;
import com.dhb.learning.polls.auth.server.model.VerificationToken;
import com.dhb.learning.polls.auth.server.payload.LoginRequest;
import com.dhb.learning.polls.auth.server.repository.VerificationTokenRepository;
import com.dhb.learning.polls.auth.server.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    public void sendMail(User user) throws MailException {

        VerificationToken verificationToken = this.confirmRegistration(user);

        String link = "http://localhost:4200/verify?token=" + verificationToken.getToken();

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            String content = mailContentBuilder.build(user.getName(), link);

            messageHelper.setText(content, true);
            messageHelper.setTo(user.getEmail());
        };

        javaMailSender.send(messagePreparator);
    }


    public User verifyConfirmUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        User user = verificationToken.getUser();
        return user;
    }

    private VerificationToken confirmRegistration(User user) {

        String token = tokenProvider.generateVerifyToken(user.getId());

        VerificationToken verificationToken = new VerificationToken(token, user);

        return verificationTokenRepository.save(verificationToken);
    }


}
