package com.crazy.portal.service.email;

import com.crazy.portal.bean.EmailParamsBean;
import com.crazy.portal.config.email.EmailConfig;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Bill on 2017/8/11.
 */
@Service
public class EmailService {

    @Resource
    private EmailConfig emailConfig;
    @Resource
    private JavaMailSender mailSender;

    public void sendSimpleMail(String title, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getEmailFrom());
        message.setTo(emailConfig.getEmailTos());
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendSimpleMail(EmailParamsBean emailParamsBean) throws Exception{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
        helper.setFrom(emailConfig.getEmailFrom());
        helper.setTo(emailParamsBean.getEmailTos().split(","));
        helper.setSubject(emailParamsBean.getTitle());
        helper.setText(emailParamsBean.getContent(),true);
        mailSender.send(message);
    }

    public void sendSimpleMailFormFile(EmailParamsBean emailParamsBean) throws Exception{
        String globalFilePath = String.format("%s%s.%s", emailConfig.getFilePath(), emailParamsBean.getFileName(), "xlsx");
        if(!existsFile(globalFilePath)){
            if(null == emailParamsBean.getDatas() || emailParamsBean.getDatas().isEmpty())
                throw new NullPointerException();
            genXlsxFile(emailParamsBean.getDatas(), globalFilePath);
        }
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
        messageHelper.setFrom(emailConfig.getEmailFrom());
        messageHelper.setTo(emailParamsBean.getEmailTos().split(","));
        messageHelper.setSubject(emailParamsBean.getTitle());
        messageHelper.setText(emailParamsBean.getContent(),true);

        FileSystemResource file = new FileSystemResource(new File(globalFilePath));
        messageHelper.addAttachment(String.format("%s.%s", emailParamsBean.getFileName(), "xlsx"), file);

        mailSender.send(mailMessage);
    }

    private boolean existsFile(String globalFilePath){
        File file = new File(globalFilePath);
        return file.exists();
    }

    private void genXlsxFile(List<String> datas, String globalFilePath){
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet();
        for(int i = 0, size = datas.size() - 1; i <= size; i++){
            XSSFRow row = sheet.createRow(i);
            row.createCell(0).setCellValue(datas.get(i));
        }
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(globalFilePath);
            xssfWorkbook.write(stream);
        } catch (Exception ex){
            ex.printStackTrace();
        }finally {
            IOUtils.closeQuietly(stream);
        }
    }

}
