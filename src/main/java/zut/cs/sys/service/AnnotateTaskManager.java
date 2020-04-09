package zut.cs.sys.service;

import zut.cs.sys.domain.AnnotateTask;
import zut.cs.sys.domain.Doc;

import java.util.List;

public interface AnnotateTaskManager {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/4/7$ 3:03$

     */
    Boolean save(String doc_id,AnnotateTask task);//增加一个任务
    Boolean delTaskById(String doc_id,String task_id);//通过id删除
    Boolean updateTask(String task_id);//通过id更新
    List<AnnotateTask> findAllTask();//查找所有
    AnnotateTask findTaskByType(String type);//通过标注类型
    AnnotateTask findTaskByStatus(String status);//通过状态

}
