package zut.cs.sys.service.impl;

import com.mongodb.client.result.UpdateResult;
import com.sun.jna.Native;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import zut.cs.sys.domain.Doc;
import zut.cs.sys.service.CNLPIRLibrary;
import zut.cs.sys.service.DocManager;
import zut.cs.sys.util.UUIDUtils;


import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
//public class DocManagerImpl extends GenericManagerImpl<Doc,Long> implements DocManager {
public class DocManagerImpl implements DocManager{
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/2/5$ 0:50$

     */
    DocManagerImpl(){
    }
//    DocDao docDao;
    @Autowired
    private MongoTemplate mongoTemplate;
//    @Autowired
//    public void setDocDao(DocDao docDao){
//        this.docDao = docDao;
//        this.dao = this.docDao;
//    }

    //通过姓名查找doc
    @Override
    public List<Doc> findByAnnotator(String annotator) {
        Query query = new Query(Criteria.where("name").is(annotator));
        return (List<Doc>) mongoTemplate.findOne(query,Doc.class);
//        return docDao.findDocByAnnotator(annotator);
    }

    //增加doc
    @Override
    public String saveObj(Doc doc) {
//        doc.setUpdate_time(new Date());
//        docDao.save(doc);
//        mongoTemplate.insert(doc);
        String uuid=UUIDUtils.getUUID();
        doc.setDoc_id(uuid);
        mongoTemplate.save(doc);
        return "insert success!";
    }

    //通过id删除doc
    @Override
    public Boolean delDoc(Doc doc) {
        mongoTemplate.remove(doc);
        return true;
    }

    //通过id查询doc
    @Override
    public Doc findDocById(String id) {
        Query query = new Query(Criteria.where("doc_id").is(id));
        return mongoTemplate.findById(query,Doc.class);
//        return mongoTemplate.find(query,Doc.class);
    }
    //通过doc更新
    @Override
    public Boolean updateDoc(Doc doc) {
        try {
            Query query = new Query(Criteria.where("doc_id").is(doc.getDoc_id()));
            Update update = new Update().set("name", doc.getName())
                    .set("annotation_type", doc.getAnnotation_type())
                    .set("update_time", new Date())
                    .set("annotator",doc.getAnnotator())
                    .set("content",doc.getContent())
                    .set("phrase",doc.getPhrase())
                    .set("status",doc.getStatus());
            //updateFirst 更新查询返回结果集的第一条
            mongoTemplate.updateFirst(query, update, Doc.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        //updateMulti 更新查询返回结果集的全部
//        mongoTemplate.updateMulti(query,update,Book.class);
        //upsert 更新对象不存在则去添加
//        mongoTemplate.upsert(query,update,Book.class);
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
    public List<Doc> findAllTask() {
        Query query = new Query(Criteria.where("phrase").ne("未标注"));
        return mongoTemplate.find(query,Doc.class);
    }

    @Override
    public Boolean publishTask(Doc doc) {
        try{
            String uuid= UUIDUtils.getUUID();
            Query query = new Query(Criteria.where("doc_id").is(doc.getDoc_id()));
            Update update = new Update().set("publish","true")
                    .set("annotator", doc.getAnnotator())
                    .set("update_time", new Date())
                    .set("phrase",doc.getPhrase())
                    .set("task_id",uuid);
            UpdateResult ur=mongoTemplate.updateFirst(query,update,Doc.class);
            return true;
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
    }

    @Override
    public Boolean processDoc(Doc doc) {
        //初始化E:\java\workspace\platform\resources\NLPIR.dll
        CNLPIRLibrary instance = (CNLPIRLibrary) Native.loadLibrary("E:\\java\\workspace\\platform\\resources\\NLPIR", CNLPIRLibrary.class);
        Boolean init_flag = instance.NLPIR_Init("", 1, "0");
        String resultString = null;
        if (false == init_flag) {
            resultString = instance.NLPIR_GetLastErrorMsg();
            System.err.println("初始化失败！\n"+resultString);
            return false;
        }
        String sInput = doc.getContent();
        try{
            resultString = instance.NLPIR_ParagraphProcess(sInput, 1);
            //将分词结果存储到doc
            String[] segmentWord=resultString.split(" ");
            Query query = new Query(Criteria.where("doc_id").is(doc.getDoc_id()));
            Update update = new Update().set("word",segmentWord)
                    .set("annotator", "admin")
                    .set("update_time", new Date())
                    .set("phrase","词性分析");
            UpdateResult ur=mongoTemplate.updateFirst(query,update,Doc.class);
            System.out.println("分词结果为：\n " + resultString);
            for (int i=0;i<segmentWord.length;i++){
                System.out.println(segmentWord[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

}
