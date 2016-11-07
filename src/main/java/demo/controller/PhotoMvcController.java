package demo.controller;

import demo.entity.Photo;
import demo.repository.PhotoRepository;
import demo.service.EmailService;
import demo.service.PhotoService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sudhir
 *         Date:7/11/16
 *         Time:5:46 PM
 *         Project:demo
 */
@Controller
public class PhotoMvcController {
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoService photoService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private TaskService taskService;


    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    String upload(MultipartHttpServletRequest request /*Principal principal*/) {

        //System.out.println("uploading for " + principal.toString());
        Optional.ofNullable(request.getMultiFileMap()).ifPresent(m -> {

            // gather all the MFs in one collection
            List<MultipartFile> multipartFiles = new ArrayList<>();
            m.values().forEach((t) -> {
                multipartFiles.addAll(t);
            });
            List<String> cities= Arrays.asList("Bangalore","Delhi");


            // convert them all into `Photo`s
            List<Photo> photos = multipartFiles.stream().map(f -> {
                try {
                    return this.photoService.createPhoto("sudhir"/*principal.getName()*/, f.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());

           /* System.out.println("started photo process w/ process instance ID: " +
                    this.photoService.launchPhotoProcess(photos).getId());*/

        });
        return "redirect:/";
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    Resource image(@PathVariable Long id) {
        return new InputStreamResource(this.photoService.readPhoto(this.photoRepository.findOne(id)));
    }

    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    String approveTask(@RequestParam String taskId, @RequestParam String approved) {
        boolean isApproved = !(approved == null || approved.trim().equals("") || approved.toLowerCase().contains("f") || approved.contains("0"));
        this.taskService.complete(taskId, Collections.singletonMap("approved", isApproved));
        return "redirect:approve";
    }

    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    String setupApprovalPage(Model model) {
        List<Task> outstandingTasks = taskService.createTaskQuery()
                .taskCandidateGroup("photoReviewers")
                .list();
        if (0 < outstandingTasks.size()) {
            Task task = outstandingTasks.iterator().next();
            model.addAttribute("task", task);
            List<Photo> photos = (List<Photo>) taskService.getVariable(task.getId(), "photos");
            model.addAttribute("photos", photos);
        }
        return "approve";
    }
    @RequestMapping(value = "/triggerEmailService",method = RequestMethod.POST)
    String triggerEmail(@RequestParam String triggerEmail ){
        System.out.println("Triggering Email Task");
        emailService.sendEmail();
        return "success";

    }
}
