package zut.cs.sys.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.result.UpdateResult;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesResponse;
import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.*;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;
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
import zut.cs.sys.service.DocExtractLibray;
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
        try {
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
                    ArrayList<ArrayList<PosToken>> segmentWords=new ArrayList<>();
                    for (String word:words){
                        String[] ss=word.split("#");
                        ArrayList<PosToken> posTokens=new ArrayList<>();
                        for (String s:ss){
                            if (s.equals(""))continue;
                            PosToken posToken=new PosToken();
                            posToken.setWord(s);
                            posTokens.add(posToken);
                        }
                        posTokens.removeIf(
                                posToken -> posToken.getWord().equals("")
                        );
                        segmentWords.add(new ArrayList<PosToken>(posTokens));
                    }
                    task.setSegmentWord(segmentWords);
                    task.setPhrase("二标");
                }else if (task.getAnnotation_type().equals("词性标注")){
                    ArrayList<ArrayList<PosToken>> propertyWords=new ArrayList<>();
                    for (String phrase:words){
                        String[] word=phrase.split("#");
                        ArrayList<PosToken> posTokens=new ArrayList<>();
                        for (String w:word){
                            if (w.equals(""))continue;
                            String[] pos=w.split("/");
                            PosToken posToken=new PosToken();
                            posToken.setWord(pos[0]);
                            posToken.setPos(pos[1]);
                            posTokens.add(posToken);
                        }
                        propertyWords.add(new ArrayList<PosToken>(posTokens));
//                        propertyWords.add(new ArrayList<String>(Arrays.asList(word)));
                    }
                    task.setPropertyWord(propertyWords);
                    task.setPhrase("二标");
                }
            }
        }
        doc.setTasks(tasks);
        Update update=new Update().set("tasks",tasks);

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
                    ArrayList<ArrayList<PosToken>> segmentWords=new ArrayList<>();
                    for (String word:words){
                        String[] ss=word.split("#");
                        ArrayList<PosToken> posTokens=new ArrayList<>();
                        for (String s:ss){
                            if (s.equals(""))continue;
                            PosToken posToken=new PosToken();
                            posToken.setWord(s);
                            posTokens.add(posToken);
                        }
                        posTokens.removeIf(
                                posToken -> posToken.getWord().equals("")
                        );
                        segmentWords.add(new ArrayList<PosToken>(posTokens));
                    }
                    task.setSegmentWord(segmentWords);
                    task.setPhrase("三标");
                }else if (task.getAnnotation_type().equals("词性标注")){
                    ArrayList<ArrayList<PosToken>> propertyWords=new ArrayList<>();
                    for (String phrase:words){
                        String[] word=phrase.split("#");
                        ArrayList<PosToken> posTokens=new ArrayList<>();
                        for (String w:word){
                            if (w.equals(""))continue;
                            PosToken posToken=new PosToken();
                            posToken.setWord(w.split("/")[0]);
                            posToken.setPos(w.split("/")[1]);
                            posTokens.add(posToken);
                        }
                        propertyWords.add(new ArrayList<PosToken>(posTokens));
//                        propertyWords.add(new ArrayList<String>(Arrays.asList(word)));
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
        if (doc==null)return null;
        return doc.getTasks();
    }


    @Override
    public Boolean segmentWord(String id,String annotate_type,String username) {
        Query query=new Query(Criteria.where("doc_id").is(id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if(doc==null)return false;

        String sInputs[] = doc.getContent().split("\n|\r");
        AnnotateTask task=new AnnotateTask();

        try{
            if (annotate_type.equals("中文分词")){
                ArrayList<ArrayList<PosToken>> segmentWord = new ArrayList<>();
//                if (segmentWord.getClass().isArray())
                for (String sInput : sInputs){
                    if(sInput.equals(""))continue;
                    System.out.println("sInput:"+sInput);

                    // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
                    Credential cred = new Credential("AKIDk25XdVjpKgqncs5jLbfdKtEJDrXtJwe8", "NUIKyHJDuLXE0bykV2JLzhGbBdrqX1e6");

                    // 实例化要请求产品的client对象
                    ClientProfile clientProfile = new ClientProfile();
                    clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
                    NlpClient nlpClient=new NlpClient(cred,"ap-guangzhou",clientProfile);
                    // 实例化一个请求对象
                    LexicalAnalysisRequest request=new LexicalAnalysisRequest();//命名实体
                    request.setText(sInput);
                    // 通过client对象调用想要访问的接口，需要传入请求对象
                    LexicalAnalysisResponse response=nlpClient.LexicalAnalysis(request);
                    PosToken[] posTokens=response.getPosTokens();
                    segmentWord.add(new ArrayList<PosToken>(Arrays.asList(posTokens)));
                }
                task.setSegmentWord(segmentWord);
            }else if (annotate_type.equals("词性标注")){
                ArrayList<ArrayList<PosToken>> propertyWord=new ArrayList<>();
                for (String sInput : sInputs){
                    if (sInput.equals(""))continue;
                    // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
                    Credential cred = new Credential("AKIDk25XdVjpKgqncs5jLbfdKtEJDrXtJwe8", "NUIKyHJDuLXE0bykV2JLzhGbBdrqX1e6");

                    // 实例化要请求产品的client对象
                    ClientProfile clientProfile = new ClientProfile();
                    clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
                    NlpClient nlpClient=new NlpClient(cred,"ap-guangzhou",clientProfile);
                    // 实例化一个请求对象
                    LexicalAnalysisRequest request=new LexicalAnalysisRequest();//命名实体
                    request.setText(sInput);
                    // 通过client对象调用想要访问的接口，需要传入请求对象
                    LexicalAnalysisResponse response=nlpClient.LexicalAnalysis(request);
                    PosToken[] posTokens=response.getPosTokens();
                    propertyWord.add(new ArrayList<PosToken>(Arrays.asList(posTokens)));
                }
                task.setPropertyWord(propertyWord);
//                System.out.println("词性标注结果为：\n " + resultString);
            }else if (annotate_type.equals("命名实体")){
                // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
                Credential cred = new Credential("AKIDk25XdVjpKgqncs5jLbfdKtEJDrXtJwe8", "NUIKyHJDuLXE0bykV2JLzhGbBdrqX1e6");

                ArrayList<NerToken> tokens=new ArrayList<>();
                for (String sInput:sInputs){
                    if (sInput==null||sInput.equals(""))continue;
                    // 实例化要请求产品的client对象
                    ClientProfile clientProfile = new ClientProfile();
                    clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
                    NlpClient nlpClient=new NlpClient(cred,"ap-guangzhou",clientProfile);
                    // 实例化一个请求对象
                    LexicalAnalysisRequest request=new LexicalAnalysisRequest();//命名实体
                    request.setText(sInput);
                    // 通过client对象调用想要访问的接口，需要传入请求对象
                    LexicalAnalysisResponse response=nlpClient.LexicalAnalysis(request);
                    NerToken[] token=response.getNerTokens();
//                    输出json回调包
//                    System.out.println(LexicalAnalysisRequest.toJsonString(response));
                    if (token==null)continue;
                    tokens.addAll(Arrays.asList(token));
                }
                if (tokens.size()==0)return false;
                task.setTokens(tokens);

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
    public Boolean saveClassifyResult(String doc_id, String result){
        System.out.println(doc_id+result);
        Query query=new Query(Criteria.where("doc_id").is(doc_id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if (doc==null)return false;
        try {
            Update update=new Update().set("classifyResult",result)
                    .set("last_modified",DateGenerate.getDate());
            UpdateResult ur=mongoTemplate.updateFirst(query,update,"docs");
            System.out.println(ur.getModifiedCount());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean recallClassifyResult(String doc_id) {
        Query query=new Query(Criteria.where("doc_id").is(doc_id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if (doc==null)return false;
        try {
            Update update=new Update().set("classifyResult",null)
                    .set("last_modified",DateGenerate.getDate());
            UpdateResult result=mongoTemplate.updateFirst(query,update,Doc.class);
            System.out.println(result.getModifiedCount());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String textClassify(String doc_id) {
        //查找doc
        Query query=new Query(Criteria.where("doc_id").is(doc_id));
        Doc doc=mongoTemplate.findOne(query,Doc.class);
        if (doc==null)return "失败";
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
            Credential cred = new Credential("AKIDk25XdVjpKgqncs5jLbfdKtEJDrXtJwe8", "NUIKyHJDuLXE0bykV2JLzhGbBdrqX1e6");

            // 实例化要请求产品(以nlp为例)的client对象
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
            NlpClient nlpClient=new NlpClient(cred,"ap-guangzhou",clientProfile);

            // 实例化一个请求对象
            TextClassificationRequest classificationRequest=new TextClassificationRequest();//文本分类
            classificationRequest.setText(doc.getContent());
            TextClassificationResponse classificationResponse=nlpClient.TextClassification(classificationRequest);
            System.out.println(classificationResponse.getClasses()[0].getFirstClassName());
            return classificationResponse.getClasses()[0].getFirstClassName();
        }catch (TencentCloudSDKException e){
            System.out.println(e.toString());
            return null;
        }
    }

    @Override
    public String machineTranslate(String text, String targetLaug) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
            Credential cred = new Credential("AKIDk25XdVjpKgqncs5jLbfdKtEJDrXtJwe8", "NUIKyHJDuLXE0bykV2JLzhGbBdrqX1e6");

            // 实例化要请求产品(以cvm为例)的client对象
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
            TmtClient tmtClient=new TmtClient(cred,"ap-guangzhou",clientProfile);

            // 实例化一个请求对象
            TextTranslateRequest textTranslateRequest=new TextTranslateRequest();
            textTranslateRequest.setSourceText(text);
            textTranslateRequest.setSource("auto");
            textTranslateRequest.setTarget(targetLaug);
            textTranslateRequest.setProjectId((long) 1181226);

            // 通过client对象调用想要访问的接口，需要传入请求对象
            TextTranslateResponse response=tmtClient.TextTranslate(textTranslateRequest);

            // 输出json格式的字符串回包
            System.out.println(response.getTargetText());
            return response.getTargetText();
        }catch (Exception e){
            e.printStackTrace();
            return "失败";
        }
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
