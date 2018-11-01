package com.dhb.learning.polls.auth.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class MailContentBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    public String build(String name , String link) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("link", link);

        return templateEngine.process("mailTemplate", context);
    }

}
