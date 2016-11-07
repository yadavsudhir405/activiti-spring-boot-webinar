package demo.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author sudhir
 *         Date:7/11/16
 *         Time:5:41 PM
 *         Project:demo
 */
@Service
@Transactional
public class EmailService {
    @Autowired
    private RuntimeService runtimeService;
    public void sendEmail(){
        ProcessInstance processInstance=runtimeService.startProcessInstanceByKey("DemoJavaDelegateService",
                Collections.singletonMap("greetingMessage", Arrays.asList("Bangalore","Delhi")));
        System.out.println("Process Instance Started with id{"+processInstance.getId()+"}");
    }
}
