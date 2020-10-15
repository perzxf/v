package com.ls.service.monitor.impl;

import com.ls.service.monitor.MailService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @description: TODO
 * @author: zcf
 * @date: 2020/8/29 22:21
 * @version: v1.0
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {
//    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from; // 读取配置文件中的参数

    @Override
    public void sendMail(String to, String subject, String content) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from); //发件人
            helper.setCc(from);  //抄送自己
            helper.setTo(to);   //收件人
            helper.setSubject(subject);  //邮件标题
            helper.setText(content, true);  //邮件内容
        } catch (MessagingException e) {
            log.error("邮件发送失败",e.getMessage());
            e.printStackTrace();
        }
        mailSender.send(message);
        log.info("邮件发送成功！！！");
    }
}
