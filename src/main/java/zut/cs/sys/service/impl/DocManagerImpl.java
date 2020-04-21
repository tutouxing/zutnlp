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
import zut.cs.sys.service.DeepClassifierLibrary;
import zut.cs.sys.service.DocManager;
import zut.cs.sys.util.DateGenerate;
import zut.cs.sys.util.UUIDUtils;
import zut.cs.sys.util.fileutil.FileOperateUtils;
import org.apache.commons.io.FileUtils;


import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    public Boolean save(String name, MultipartFile file,String user) throws IOException {
//        List<Doc> docs=new ArrayList<>();
        System.out.println("user="+user);
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
        doc.setPublisher(user);
        Set<String> set=new HashSet<>();
        set.add("无");
        doc.setDone(set);
        doc.setType(file.getContentType());
        try {
            mongoTemplate.save(doc);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

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

    @Override
    public Boolean saveReAnnotateByUser(String annotator, ArrayList<String> words,String doc_id, String task_id) {
        System.out.println("annotator="+annotator);
        Query query=new Query(Criteria.where("doc_id").is(doc_id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if (doc==null)return false;
        ArrayList<AnnotateTask> tasks=doc.getTasks();
        //遍历寻找要更新的task
        for (AnnotateTask task:tasks){
            if (task.getTask_id().equals(task_id)){
                task.setAnnotator(annotator);
                task.setUpdate_time(DateGenerate.getDate());
                if (task.getAnnotation_type().equals("中文分词")){
                    ArrayList<ArrayList<String>> segmentWords=new ArrayList<>();
                    for (String s:words){
                        ArrayList<String> word= new ArrayList<>(Arrays.asList(s.split("#")));// Arrays.asList(s.split("#"));
                        word.removeIf(
                                s1 -> s1.equals("")
                        );
//                        System.out.println("word:");
                        segmentWords.add(new ArrayList<String>(word));
                    }
                    task.setSegmentWord(segmentWords);
                    task.setPhrase("二标");
                }else if (task.getAnnotation_type().equals("词性标注")){
                    ArrayList<ArrayList<String>> propertyWords=new ArrayList<>();
                    for (String s:words){
                        String[] word=s.split("#");
                        propertyWords.add(new ArrayList<String>(Arrays.asList(word)));
                    }
                    task.setPropertyWord(propertyWords);
                    task.setPhrase("二标");
                }
            }
        }
        doc.setTasks(tasks);
        Update update=new Update().set("tasks",tasks);
        try {
            mongoTemplate.updateFirst(query,update,Doc.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean mergeAnnotation(ArrayList<String> words,String doc_id, String task1_id, String task2_id,String annotator) {
        Query query=new Query(Criteria.where("doc_id").is(doc_id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if (doc==null)return false;
        ArrayList<AnnotateTask> tasks=doc.getTasks();
        tasks.removeIf(
                task -> task.getTask_id().equals(task2_id)
        );
        //更新tasks
        for (AnnotateTask task:tasks){
            if (task.getTask_id().equals(task1_id)){
                task.setUpdate_time(DateGenerate.getDate());
                task.setAnnotator(annotator);
                //处理words
                if (task.getAnnotation_type().equals("中文分词")){
                    ArrayList<ArrayList<String>> segmentWords=new ArrayList<>();
                    for (String s:words){
                        ArrayList<String> word= new ArrayList<>(Arrays.asList(s.split("#")));// Arrays.asList(s.split("#"));
                        word.removeIf(
                                s1 -> s1.equals("")
                        );
                        segmentWords.add(new ArrayList<String>(word));
                    }
                    task.setSegmentWord(segmentWords);
                    task.setPhrase("三标");
                }else if (task.getAnnotation_type().equals("词性标注")){
                    ArrayList<ArrayList<String>> propertyWords=new ArrayList<>();
                    for (String s:words){
                        String[] word=s.split("#");
                        propertyWords.add(new ArrayList<String>(Arrays.asList(word)));
                    }
                    task.setPropertyWord(propertyWords);
                    task.setPhrase("三标");
                }
            }
        }
        Update update=new Update().set("tasks",tasks);
        try {
            mongoTemplate.updateFirst(query,update,Doc.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
    public Boolean segmentWord(String id,String annotate_type,String username) {
        //初始化E:\java\workspace\platform\resources\NLPIR.dll
        Query query=new Query(Criteria.where("doc_id").is(id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if(doc==null)return false;
        //使用接口分词
//        CNLPIRLibrary instance = (CNLPIRLibrary) Native.loadLibrary("E:\\java\\workspace\\platform\\resources\\NLPIR", CNLPIRLibrary.class);
        Boolean init_flag = CNLPIRLibrary.Instance.NLPIR_Init("E:\\java\\workspace\\platform\\src\\main\\resources\\ICTCLAS", 1, "0");
        String resultString = null;
        if (!init_flag) {
            resultString = CNLPIRLibrary.Instance.NLPIR_GetLastErrorMsg();
            System.err.println("初始化失败！\n"+resultString);
            return false;
        }

        String sInputs[] = doc.getContent().split("\n|\r");
        AnnotateTask task=new AnnotateTask();

        try{
            if (annotate_type.equals("中文分词")){
                ArrayList<ArrayList<String>> segmentWord = new ArrayList<>();
//                if (segmentWord.getClass().isArray())
                for (String sInput : sInputs){
                    resultString = CNLPIRLibrary.Instance.NLPIR_ParagraphProcess(sInput, 0);
                    String[] words=resultString.split(" ");
                    segmentWord.add(new ArrayList<String>(Arrays.asList(words)));
                }
                task.setSegmentWord(segmentWord);
            }else if (annotate_type.equals("词性标注")){
                ArrayList<ArrayList<String>> propertyWord=new ArrayList<>();
                for (String sInput : sInputs){
                    resultString = CNLPIRLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
                    String[] words=resultString.split(" ");
                    for (String word:words){
                        word=word.replace("/","");
                    }
                    propertyWord.add(new ArrayList<>(Arrays.asList(words)));
                }
                task.setPropertyWord(propertyWord);
//                System.out.println("词性标注结果为：\n " + resultString);
            }else if (annotate_type.equals("关键词提取")){
                String sInput=doc.getContent();
                resultString=CNLPIRLibrary.Instance.NLPIR_GetKeyWords(sInput,3,false);
                String[] keyWords=resultString.split(" ");
                for (String word:keyWords){
                    System.out.println(word);
                }
                System.out.println("关键词：\n "+resultString);
            }
            //将分词结果存为task并加入doc的tasks中
            //如果tasks中有这项任务则更新  没有则新建任务并加入
            //待修改
            task.setAnnotation_type(annotate_type);
            task.setPublisher(username);
            task.setTask_id(UUIDUtils.getUUID());
            task.setPhrase("一标");
            task.setTask_name(doc.getName());
            task.setStatus("待审核");
            task.setDoc_id(id);
            task.setCreated_time(DateGenerate.getDate());
            task.setUpdate_time(DateGenerate.getDate());
            ArrayList<AnnotateTask> tasks=new ArrayList<>();
            if (doc.getTasks()==null){
                tasks.add(task);
            }else {
                tasks.addAll(doc.getTasks());
                tasks.add(task);
            }
            doc.setTasks(tasks);
            //更新已发布任务
            Set<String> newTasks=new HashSet<>();
            if (doc.getPublish()==null){
                newTasks.add(annotate_type);
            }else {
                newTasks=doc.getPublish();
                if (!newTasks.contains(annotate_type))newTasks.add("/"+annotate_type);
            }
            Update update = new Update().set("tasks",tasks)
                    .set("publish",newTasks)
                    .set("last_modified",DateGenerate.getDate());
            UpdateResult ur=mongoTemplate.updateFirst(query,update,Doc.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String[] reAnnotation(String str, String annotation_type) {
        Boolean init_flag = CNLPIRLibrary.Instance.NLPIR_Init("E:\\java\\workspace\\platform", 1, "0");
        String resultString = null;
        if (!init_flag) {
            resultString = CNLPIRLibrary.Instance.NLPIR_GetLastErrorMsg();
            System.err.println("初始化失败！\n"+resultString);
            return null;
        }
        resultString = CNLPIRLibrary.Instance.NLPIR_ParagraphProcess(str, 1);
        String[] words=resultString.split(" ");
        return words;
    }

    @Override
    public String textClassify(String doc_id) throws IOException {
        //1 分类过程--初始化
        if (DeepClassifierLibrary.Instance.DC_Init("E:\\java\\workspace\\platform\\src\\main\\resources\\DeepClassifier", 1, 800, "")) {
            System.out.println("deepClassifier初始化成功");
        } else {
            System.out.println("deepClassifier初始化失败" + DeepClassifierLibrary.Instance.DC_GetLastErrorMsg());
            System.exit(1);
        }

        //2、训练过程--遍历训练分类文本的文件夹，添加所有的训练分类文本
        ArrayList list = FileOperateUtils.getAllFilesPath(new File("训练分类用文本"));
        for (int i = 0; i < list.size(); i++) {
            File f = new File(list.get(i).toString());
            String className = f.getParent();
            className = className
                    .substring(className.lastIndexOf("\\") + 1);
            //将训练分类文本加载到内存中
            String contentText = FileUtils.readFileToString(f, "utf-8");
            boolean dc_AddTrain = DeepClassifierLibrary.Instance.DC_AddTrain(
                    className, contentText);
            if(!dc_AddTrain){
                System.out.println(DeepClassifierLibrary.Instance.DC_GetLastErrorMsg());
            }
        }
        //3、训练过程--开始训练
        boolean dc_Train = DeepClassifierLibrary.Instance.DC_Train();
        //4、训练过程--训练结束，退出
        DeepClassifierLibrary.Instance.DC_Exit();

        //查找doc
        Query query=new Query(Criteria.where("doc_id").is(doc_id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if (doc==null)return "失败";

        //1、分类过程--初始化
        if (DeepClassifierLibrary.Instance.DC_Init("E:\\java\\workspace\\platform\\src\\main\\resources\\DeepClassifier", 1, 800, "")) {
            System.out.println("deepClassifier初始化成功");
        } else {
            System.out.println("deepClassifier初始化失败：" + DeepClassifierLibrary.Instance.DC_GetLastErrorMsg());
            System.exit(1);
        }

//		Long DC_Handle = DeepClassifierLibrary.Instance.DC_NewInstance((long)800);
        //2、分类过程--加载训练结果
        DeepClassifierLibrary.Instance.DC_LoadTrainResult();

        //3、分类过程--读取待分类的文本
        String content = doc.getContent();
                //FileOperateUtils.getFileContent("test.txt", "utf-8");

        //4、分类过程--输出分类结果
        System.out.println("分类结果：" + DeepClassifierLibrary.Instance.DC_Classify(content));

        //5、分类过程--退出
        DeepClassifierLibrary.Instance.DC_Exit();
        return "成功";
    }

    @Override
    public Boolean recallPublish(String doc_id,String annotation_type) {
        try{
            System.out.println(doc_id+"?");
//            Query query=new Query(Criteria.where("doc_id").is(id));
            Query query=new Query(Criteria.where("doc_id").is(doc_id));
            Doc doc=mongoTemplate.findOne(query,Doc.class);
//            System.out.println(doc.getTasks().size()+"task.size");
            if (doc==null)return false;
            if(doc.getTasks()==null)return false;
            ArrayList<AnnotateTask> tasks=doc.getTasks();
            tasks.removeIf(
                    annotateTask -> annotateTask.getAnnotation_type().equals(annotation_type)
            );
            Set<String> publish=new HashSet<>();
            for (AnnotateTask task:tasks){
                if (publish.size()==0){
                    publish.add(task.getAnnotation_type());
                }else publish.add("/"+task.getAnnotation_type());
            }
            Update update=new Update().set("tasks",tasks)
                    .set("publish",publish)
                    .set("last_modified",DateGenerate.getDate());
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

    @Override
    public Boolean passInitialReview(String doc_id, String task_id) {
        Query query=new Query(Criteria.where("doc_id").is(doc_id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if (doc==null)return false;
        ArrayList<AnnotateTask> tasks=doc.getTasks();
        for (AnnotateTask task:tasks){
            if (task.getTask_id().equals(task_id)){
                task.setPhrase("二标");
            }
        }
        Update update=new Update().set("tasks",tasks);
        try {
            mongoTemplate.updateFirst(query,update,Doc.class);
            return true;
        }catch (Exception e){
//            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean passFinalReview(String doc_id, String task_id) {
        Query query=new Query(Criteria.where("doc_id").is(doc_id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if (doc==null)return false;
        ArrayList<AnnotateTask> tasks=doc.getTasks();
        Set<String> done=new HashSet<>();
        Iterator<String> it=doc.getDone().iterator();
        while (it.hasNext()){
            if (it.next().equals("无")){
                continue;
            }else if (done.size()==0)done.add(it.next());
            else done.add("/"+it.next());
        }
        Update update=new Update().set("tasks",tasks)
                .set("done",done);
        try {
            mongoTemplate.updateFirst(query,update,Doc.class);
            return true;
        }catch (Exception e){
//            e.printStackTrace();
            return false;
        }
    }


}
