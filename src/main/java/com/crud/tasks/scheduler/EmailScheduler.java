package com.crud.tasks.scheduler;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.SimpleEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailScheduler {
    public static final String SUBJECT = "Tasks: Once a day email";
    @Autowired
    private AdminConfig adminConfig;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private SimpleEmailService simpleEmailService;

    @Scheduled(fixedDelay = 10000)
    public void sendInformationEmail(){
        simpleEmailService.send(new Mail(
                adminConfig.getAdminMail(),
                SUBJECT,
                "Currently in database you got: "+numberOfTasks()
        ));
    }
    private String numberOfTasks(){
        long size = taskRepository.count();
        if(size==1){
            return size+" task";
        }else
            return size+" tasks";
    }
}
