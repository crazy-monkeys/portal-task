package com.crazy.portal.controller;

import com.crazy.portal.service.email.EmailService;
import com.crazy.portal.util.common.CommonEnum;
import com.crazy.portal.bean.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * Created by Bill on 2017/9/29.
 */
@RestController
public class WebServiceController {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceController.class);

    @Resource
    private EmailService emailService;

    @RequestMapping(value = "/sendEmail")
    public boolean sendEmail(@RequestBody EmailParamsBean emailParamsBean){
        try {
            Assert.notNull(emailParamsBean.getContent());
            Assert.notNull(emailParamsBean.getEmailTos());
            Assert.notNull(emailParamsBean.getTitle());
            emailService.sendSimpleMail(emailParamsBean);
        } catch (Exception e) {
            logger.error("",e);
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/file/sendEmail", method = RequestMethod.POST)
    public boolean sendEmailFormFile(@RequestBody EmailParamsBean emailParamsBean){
        try {
            Assert.notNull(emailParamsBean.getContent());
            Assert.notNull(emailParamsBean.getEmailTos());
            Assert.notNull(emailParamsBean.getTitle());
            Assert.notNull(emailParamsBean.getFileName());
            emailService.sendSimpleMailFormFile(emailParamsBean);
        } catch (Exception e) {
            logger.error("",e);
            return false;
        }
        return true;
    }

}
