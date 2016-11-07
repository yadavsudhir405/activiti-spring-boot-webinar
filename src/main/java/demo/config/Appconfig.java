package demo.config;

import demo.javaDelegateService.JavaDelegateService;
import demo.javaDelegateService.JavaService;
import demo.entity.Photo;
import demo.service.PhotoService;
import doge.photo.DogePhotoManipulator;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.spring.integration.ActivitiInboundGateway;
import org.activiti.spring.integration.IntegrationActivityBehavior;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

/**
 * @author sudhir
 *         Date:7/11/16
 *         Time:5:33 PM
 *         Project:demo
 */
@Configuration
public class Appconfig {
    @Bean
    JavaDelegateService javaDelegateService(){
        return new JavaDelegateService();
    }
    @Bean
    JavaService javaService(){
        return new JavaService();
    }
    @Bean
    CommandLineRunner init(IdentityService identityService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {

                // install groups & users
                Group approvers = group("photoReviewers");
                Group uploaders = group("photoUploaders");

                User joram = user("sudhir", "Joram", "Barrez");
                identityService.createMembership(joram.getId(), approvers.getId());
                identityService.createMembership(joram.getId(), uploaders.getId());

                User josh = user("jlong", "Josh", "Long");
                identityService.createMembership(josh.getId(), uploaders.getId());
            }

            private User user(String userName, String f, String l) {
                User u = identityService.newUser(userName);
                u.setFirstName(f);
                u.setLastName(l);
                u.setPassword("password");
                identityService.saveUser(u);
                return u;
            }

            private Group group(String groupName) {
                Group group = identityService.newGroup(groupName);
                group.setName(groupName);
                group.setType("security-role");
                identityService.saveGroup(group);
                return group;
            }
        };

    }
    @Bean
    DogePhotoManipulator dogePhotoManipulator() {
        DogePhotoManipulator dogePhotoManipulator = new DogePhotoManipulator();
        dogePhotoManipulator.addTextOverlay("pivotal", "abstractfactorybean", "java");
        dogePhotoManipulator.addTextOverlay("spring", "annotations", "boot");
        dogePhotoManipulator.addTextOverlay("code", "semicolonfree", "groovy");
        dogePhotoManipulator.addTextOverlay("clean", "juergenized", "spring");
        dogePhotoManipulator.addTextOverlay("workflow", "activiti", "BPM");
        return dogePhotoManipulator;
    }
    @Bean
    IntegrationActivityBehavior activitiDelegate(ActivitiInboundGateway activitiInboundGateway) {
        return new IntegrationActivityBehavior(activitiInboundGateway);
    }

    @Bean
    ActivitiInboundGateway inboundGateway(ProcessEngine processEngine) {
        return new ActivitiInboundGateway(processEngine, "processed", "userId", "photo", "photos");
    }

    @Bean
    IntegrationFlow inboundProcess(
            ActivitiInboundGateway activitiInboundGateway, PhotoService photoService) {
        return IntegrationFlows
                .from(activitiInboundGateway)
                .handle(
                        new GenericHandler<DelegateExecution>() {
                            @Override
                            public Object handle(DelegateExecution execution, Map<String, Object> headers) {
                                Photo photo = (Photo) execution.getVariable("photo");
                                Long photoId = photo.getId();
                                System.out.println("integration: handling execution " + headers.toString());
                                System.out.println("integration: handling photo #" + photoId);

                                photoService.dogifyPhoto(photo);

                                return MessageBuilder.withPayload(execution)
                                        .setHeader("processed", (Object) true)
                                        .copyHeaders(headers).build();
                            }
                        }
                )
                .get();
    }

}
