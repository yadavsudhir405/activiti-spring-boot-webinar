package demo.javaDelegateService;

import demo.entity.Photo;
import demo.repository.PhotoRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sudhir
 *         Date:3/11/16
 *         Time:5:54 PM
 *         Project:demo
 */
@Component(value = "javaDelegateService")
public class JavaDelegateService implements JavaDelegate {
    @Autowired
    private PhotoRepository photoRepository;
    @Override
    public void execute(DelegateExecution delegateExecution) {
        List<String> citieis=(List<String>) delegateExecution.getVariable("greetingMessage");
        System.out.println("************************* Started Executing Java Delegate Service ********************");
        citieis.parallelStream().forEach(System.out::println);
        List<String> modifiedCities=citieis.stream().map(city->city.concat("-modified")).collect(Collectors.toList());
        delegateExecution.setVariable("modifiedCities",modifiedCities);
        delegateExecution.setVariable("photos",getAllPhoto());

    }
    private List<Photo> getAllPhoto(){
        return  photoRepository.findAll();
    }
}
