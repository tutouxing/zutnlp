package zut.cs.sys.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.result.UpdateResult;
import com.sun.jna.Native;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zut.cs.sys.dao.DocDao;
import zut.cs.sys.domain.AnnotateTask;
import zut.cs.sys.domain.Doc;
import zut.cs.sys.service.CNLPIRLibrary;
import zut.cs.sys.service.DocManager;
import zut.cs.sys.util.DateGenerate;
import zut.cs.sys.util.UUIDUtils;


import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
//public class DocManagerImpl extends GenericManagerImpl<Doc,Long> implements DocManager {
public class DocManagerImpl implements DocManager{
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/2/5$ 0:50$

     */
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DocDao docDao;

    //增加doc
    @Override
    public Boolean save(String name, MultipartFile file) throws IOException {
//        List<Doc> docs=new ArrayList<>();
        Doc doc=new Doc();
//        doc.setDoc_id(id.toString());
        doc.setDoc_id(UUIDUtils.getUUID());
        doc.setCreated_time(DateGenerate.getDate());
        doc.setName(name);
        InputStream in=file.getInputStream();
        byte[] buffer = new byte[1024];
        int len;
        String content="";
        try {
            while(-1!=(len=in.read(buffer))){
                String str = new String(buffer, 0, len);
                content+=str+"\n";
            }
        }catch (Exception e){
            e.printStackTrace();
            in.close();
        }
        in.close();
        doc.setContent(content);
        doc.setLen(file.getSize());
        doc.setPublisher("admin");
        doc.setType(file.getContentType());
        mongoTemplate.save(doc);
        return true;
//        DBObject metaData=new BasicDBObject();
//        ((BasicDBObject) metaData).put("name",name);
//        ((BasicDBObject) metaData).put("created_time",DateGenerate.getDate());
////        ((BasicDBObject) metaData).put("detail",content);
////        operations.store(file.getInputStream(),file.getName(),file.getContentType(),metaData);
//        ObjectId id=gridFsTemplate.store(
//                file.getInputStream(),file.getOriginalFilename(),file.getContentType(),metaData
//        );
//
//        return id.toString();
    }

    //通过id删除doc
    @Override
    public Boolean delDocById(String id) {
        mongoTemplate.remove(new Query(Criteria.where("doc_id").is(id)),Doc.class);
        return true;
    }

    //通过doc更新
    @Override
    public Boolean updateDoc(Doc doc) {
        /*try {
            Query query = new Query(Criteria.where("doc_id").is(doc.getDoc_id()));
            Update update = new Update().set("name", doc.getName());
//                    .set("annotation_type", doc.getAnnotation_type())
//                    .set("update_time", DateGenerate.getDate())
//                    .set("annotator",doc.getAnnotator())
//                    .set("content",doc.getContent())
//                    .set("phrase",doc.getPhrase())
//                    .set("status",doc.getStatus());
            //updateFirst 更新查询返回结果集的第一条
            mongoTemplate.updateFirst(query, update, Doc.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }*/
        return true;
        //updateMulti 更新查询返回结果集的全部
//        mongoTemplate.updateMulti(query,update,Book.class);
        //upsert 更新对象不存在则去添加
//        mongoTemplate.upsert(query,update,Book.class);
    }

    //通过id查询doc
    @Override
    public Doc findDocById(String id) {
        Doc doc= mongoTemplate.findOne(new Query(Criteria.where("doc_id").is(id)),Doc.class);
//        Optional<Doc> doc=docDao.findById(id);
//        GridFSFile file=gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
//        Doc doc=new Doc();
//        doc.setContent(file.getMetadata().get("detail").toString());
//        doc.setName(file.getMetadata().get("name").toString());
//        doc.setCreated_time(file.getMetadata().get("created_time").toString());
        return doc;
    }

    //通过姓名查找doc
    @Override
    public Doc findByName(String name)  {
        Doc doc=mongoTemplate.findOne(new Query(Criteria.where("name").is(name)),Doc.class);
        return doc;
    }


    //获取所有docs
    @Override
    public List<Doc> findAllDocs() {
        return mongoTemplate.findAll(Doc.class);
    }
    @Override
    public List<Doc> findAllDocsByMulti(){
        return null;
    }

    @Override
    public ArrayList<AnnotateTask> getAllTasks() {
        ArrayList<AnnotateTask> tasks=new ArrayList<>();
        ArrayList<Doc> docs= (ArrayList<Doc>) mongoTemplate.findAll(Doc.class);
        System.out.println(docs.size()+"doc");
        for (Doc doc:docs){
            if(doc.getTasks()!=null){
                tasks.addAll(doc.getTasks());
            }
        }
        System.out.println(tasks.size()+"task");
        return tasks;
    }

    @Override
    public List<AnnotateTask> findAllTaskByDocId(String id) {
        Doc doc=mongoTemplate.findOne(new Query(Criteria.where("doc_id").is(id)),Doc.class);
        return doc.getTasks();
    }

//    @Override
//    public Boolean processDoc(Doc doc, String annotation_type) {
        /*try{
            String uuid= UUIDUtils.getUUID();
            Query query = new Query(Criteria.where("doc_id").is(doc.getDoc_id()));
            Update update = new Update().set("publish","true")
//                    .set("annotator", doc.getAnnotator())
                    .set("update_time", DateGenerate.getDate())
//                    .set("phrase",doc.getPhrase())
                    .set("task_id",uuid);
            UpdateResult ur=mongoTemplate.updateFirst(query,update,Doc.class);
            return true;
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }*/
//        return true;
//    }

    @Override
    public Boolean segmentWord(String id,String annotate_type) {
        //初始化E:\java\workspace\platform\resources\NLPIR.dll
        Query query=new Query(Criteria.where("doc_id").is(id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);

        //使用接口分词
        CNLPIRLibrary instance = (CNLPIRLibrary) Native.loadLibrary("E:\\java\\workspace\\platform\\resources\\NLPIR", CNLPIRLibrary.class);
        Boolean init_flag = instance.NLPIR_Init("", 1, "0");
        String resultString = null;
        if (!init_flag) {
            resultString = instance.NLPIR_GetLastErrorMsg();
            System.err.println("初始化失败！\n"+resultString);
            return false;
        }
        String sInput = doc.getContent();
        try{
            resultString = instance.NLPIR_ParagraphProcess(sInput, 1);
            System.out.println("分词结果为：\n " + resultString);
            //将分词结果存为task并加入doc的tasks中
            //如果tasks中有这项任务则更新  没有则新建任务并加入
            //待修改
            String[] segmentWord=resultString.split(" ");
            System.out.println(segmentWord);
            AnnotateTask task=new AnnotateTask();
            task.setAnnotation_type(annotate_type);
            task.setAnnotator("admin");
            task.setTask_id(UUIDUtils.getUUID());
            task.setSegmentWord(segmentWord);
            task.setPhrase("初审");
            task.setTask_name(doc.getName());
            task.setStatus("待审核");
            task.setDoc_id(id);
            task.setCreated_time(DateGenerate.getDate());
            ArrayList<AnnotateTask> tasks=new ArrayList<>();
            if (doc.getTasks()==null){
                tasks.add(task);
            }else {
                tasks.addAll(doc.getTasks());
                tasks.add(task);
            }
            doc.setTasks(tasks);
            //更新已发布任务
            ArrayList<String> newTasks=new ArrayList<>();
            if (doc.getPublish()==null){
                newTasks.add(annotate_type);
            }else {
                newTasks=doc.getPublish();
                newTasks.add(annotate_type+"/");
            }
            Update update = new Update().set("tasks",tasks)
                    .set("publish",newTasks);
            UpdateResult ur=mongoTemplate.updateFirst(query,update,Doc.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }



    @Override
    public Boolean recallPublish(String doc_id,String task_id) {
        try{
            Query query=new Query(Criteria.where("doc_id").is(doc_id));
            Doc doc=mongoTemplate.findOne(query,Doc.class);
            ArrayList<AnnotateTask> tasks=doc.getTasks();
            for (AnnotateTask task:tasks){
                if (task.getTask_id()==task_id){
                    tasks.remove(task);
                }
            }
            Update update=new Update().set("tasks",tasks);
            mongoTemplate.updateFirst(query,update,Doc.class);
//            String status=null;
//            String temp=doc.getAnnotation_type();
//            if (temp==null)return false;
//            temp=temp.replace(annotation_type,"/");
//            temp=temp.replace("//","/");
//            if(temp!=null){
//                status=doc.getStatus();
//            }else status=null;
//            //提交修改
//            Query query = new Query(Criteria.where("doc_id").is(doc.getDoc_id()));
//            Update update = new Update().set("update_time", DateGenerate.getDate())
//                    .set("annotation_type",temp)
//                    .set("status",status)
//                    .set("word",null);
//            UpdateResult ur=mongoTemplate.updateFirst(query,update,Doc.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
