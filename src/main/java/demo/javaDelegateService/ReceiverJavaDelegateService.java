package demo.javaDelegateService;

import demo.entity.Photo;
import demo.service.PhotoService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sudhir
 *         Date:3/11/16
 *         Time:6:19 PM
 *         Project:demo
 */
@Component(value = "receiverJavaDelegateService")
public class ReceiverJavaDelegateService implements JavaDelegate {
    @Autowired
    private PhotoService photoService;
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("**************** Executing ReceiverJavaDelgate ************");
        List<String> modifiedCities= (List<String>) delegateExecution.getVariable("modifiedCities");
        modifiedCities.parallelStream().forEach(System.out::println);
        List<Photo> photos= (List<Photo>) delegateExecution.getVariable("photos");
        photos.parallelStream().forEach(photo->{
            System.out.println("******** Dogifying Photo with photoId {"+photo.getId()+"}");
            photoService.dogifyPhoto(photo);
            System.out.println("******** Dogifyed Photo with photoId {"+photo.getId()+"}");
        });
    }
}
