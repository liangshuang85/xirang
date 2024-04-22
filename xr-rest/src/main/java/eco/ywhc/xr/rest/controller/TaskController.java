package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.core.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar.crud.model.OperationResult;

/**
 * 任务接口
 */
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * 发起任务
     */
    @PostMapping("/tasks/{id}:start")
    public OperationResult sendLarkTask(@PathVariable long id) {
        int affected = taskService.startLarkTask(id);
        return OperationResult.of(affected);
    }

}
