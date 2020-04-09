package zut.cs.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import zut.cs.sys.dao.AnnotateTaskDao;
import zut.cs.sys.dao.DocDao;
import zut.cs.sys.domain.AnnotateTask;
import zut.cs.sys.domain.Doc;
import zut.cs.sys.service.AnnotateTaskManager;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnnotateTaskManagerImpl implements AnnotateTaskManager {

    @Autowired
    private AnnotateTaskDao annotateTaskDao;
    @Autowired
    private DocDao docDao;

    @Override
    public Boolean save(String doc_id,AnnotateTask task) {
        try{
            task.setDoc_id(doc_id);
            annotateTaskDao.save(task);
//            docDao.addTask(task,doc_id);
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean delTaskById(String doc_id,String task_id) {
        annotateTaskDao.deleteById(task_id);

        return null;
    }

    @Override
    public Boolean updateTask(String task_id) {
        return null;
    }

    @Override
    public List<AnnotateTask> findAllTask() {
        return null;
    }

    @Override
    public AnnotateTask findTaskByType(String type) {
        return null;
    }

    @Override
    public AnnotateTask findTaskByStatus(String status) {
        return null;
    }
    /**
     * @Description: java类作用描述

     * @Author: yc

     * @CreateDate: 2020/4/7$ 3:12$

     */
}
