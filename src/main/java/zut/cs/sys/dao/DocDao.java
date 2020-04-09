package zut.cs.sys.dao;

import org.bson.BsonValue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import zut.cs.sys.domain.AnnotateTask;
import zut.cs.sys.domain.Doc;

public interface DocDao extends MongoRepository<Doc,String> {

    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/4/8$ 18:21$

     */
//    Boolean addTask(AnnotateTask task,String id);
//
//    @Query(value = "")
//    Doc findByDoc_id(String id);
}
