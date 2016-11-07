package demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sudhir
 *         Date:2/11/16
 *         Time:3:23 PM
 *         Project:demo
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class ActivitiStarter {
    public static void main(String[] args) {
        ApplicationContext applicationContext=
                SpringApplication.run(ActivitiStarter.class);
        ProcessEngine processEngine=applicationContext.getBean(ProcessEngine.class);
        ProcessEngineConfiguration processEngineConfiguration=applicationContext.getBean(ProcessEngineConfiguration
                .class);
        System.out.println("Process Engine Started");
    }




}






