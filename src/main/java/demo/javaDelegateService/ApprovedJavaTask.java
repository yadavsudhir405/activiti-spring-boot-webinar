package demo.javaDelegateService;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * @author sudhir
 *         Date:3/11/16
 *         Time:7:05 PM
 *         Project:demo
 */
@Component(value = "approvedUserTask")
public class ApprovedJavaTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("*********************Photos got Received******************");
    }
}
