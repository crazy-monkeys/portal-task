package com.crazy.portal.config.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Bill on 2017/8/11.
 */
@Configuration
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${spring.mail.tos}")
    private String[] emailTos;

    @Value("${spring.mail.file.path}")
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEmailFrom() {
        return emailFrom;
    }
    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String[] getEmailTos() {
        return emailTos;
    }

    public void setEmailTos(String[] emailTos) {
        this.emailTos = emailTos;
    }
}
