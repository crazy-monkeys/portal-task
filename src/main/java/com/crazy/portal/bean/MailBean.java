package com.crazy.portal.bean;

import lombok.Data;
import org.springframework.core.io.FileSystemResource;

import java.util.Map;

/**
 * @Desc:
 * @Author: Bill
 * @Date: created in 22:59 2019-07-09
 * @Modified by:
 */
@Data
public class MailBean {

    /**
     * 发送人
     */
    private String tos;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 附件
     */
    private FileSystemResource file;

    /**
     * 附件名称
     */
    private String attachmentFilename;

    /**
     * 模板文件名
     */
    private String templateName;

    private Map<String,String> params;
}
