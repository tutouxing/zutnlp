package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.domain.AnnotateTask;
import zut.cs.sys.service.AnnotateTaskManager;

@RestController
@RequestMapping("/task")
@Api(tags = "任务接口")
public class AnnotateTaskController {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/4/9$ 0:11$

     */
    @Autowired
    private AnnotateTaskManager taskManager;

    @ApiOperation(value = "增加任务")
    @PostMapping(value = "/saveTask")
    public Boolean save(@RequestParam String doc_id, @RequestBody AnnotateTask task){
        try {
            taskManager.save(doc_id,task);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @ApiOperation(value = "删除任务")
    @PutMapping(value = "/delTask")
    public Boolean delTaskById(@RequestParam String doc_id,@RequestParam String task_id){
        taskManager.delTaskById(doc_id,task_id);
        return true;
    }
}
