package zut.cs.sys.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.result.UpdateResult;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
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
                if (DocExtractLibray.Instance.DE_Init("E:\\java\\workspace\\platform\\src\\main\\resources\\DocExtractor", 1, "") == 0) {
                    System.out.println("初始化失败："
                            + DocExtractLibray.Instance.DE_GetLastErrMsg());
                    System.exit(1);
                }
                System.out.println("初始化成功");

                String content = doc.getContent();
                int score=DocExtractLibray.Instance.DE_ComputeSentimentDoc(content);
                System.out.println("--->score--->"+score);
                NativeLong handle = DocExtractLibray.Instance.DE_ParseDocE(content, "mgc#ngd",
                        true, DocExtractLibray.ALL_REQUIRED);
                System.out.println("抽取的人名为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_PERSON));
                System.out.println("抽取的地名为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_LOCATION));
                System.out.println("抽取的机构名为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_ORGANIZATION));
                System.out.println("抽取的关键词为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_KEYWORD));
                System.out.println("抽取的文章作者为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_AUTHOR));
                System.out.println("抽取的媒体为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_MEDIA));
                System.out.println("抽取的文章对应的所在国别为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_COUNTRY));
                System.out.println("抽取的文章对应的所在省份为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_PROVINCE));
                System.out.println("抽取的文章摘要为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_ABSTRACT));
                System.out.println("输出文章的正面情感词为-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_POSITIVE));
                System.out.println("输出文章的副面情感词-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_NEGATIVE));
                System.out.println("输出文章原文-->" + content);
                System.out.println("输出文章去除网页等标签后的正文-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_DEL_HTML));
                System.out.println("去除空格:" + DocExtractLibray.Instance.DE_GetResult(handle, 11).replaceAll("[　*| *| *|//s*]*", ""));

                System.out.println("自定义词(mgc)-->"
                        + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_USER_DEFINED + 1));
                System.out.println("情感值---->" + DocExtractLibray.Instance.DE_GetSentimentScore(handle));
                DocExtractLibray.Instance.DE_ReleaseHandle(handle);

                System.out.println("是否安全退出-->"+DocExtractLibray.Instance.DE_Exit());
            }
            CNLPIRLibrary.Instance.NLPIR_Exit();
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
        /*//1 分类过程--初始化
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
        DeepClassifierLibrary.Instance.DC_Exit();*/

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
        String result=DeepClassifierLibrary.Instance.DC_Classify(content);
        System.out.println("分类结果：" + result);

        //5、分类过程--退出
        DeepClassifierLibrary.Instance.DC_Exit();
        return result;
    }

    @Override
    public String getDocExtractor(String doc_id,String annotator) {
        /*
        测试导入用户自定义词
        if ( DocExtractLibray.Instance.DE_Init("E:\\java\\workspace\\platform\\src\\main\\resources\\DocExtractor", 1, "") == 0 ) {
            System.out.println("DocExtractor初始化失败：" + DocExtractLibray.Instance.DE_GetLastErrMsg());
            System.exit(1);
        }
        System.out.println("DocExtractor初始化成功");
        System.out.println("成功导入的自定义词个数：" + DocExtractLibray.Instance.DE_ImportUserDict("dict/userdic.txt"));

        System.out.println("是否安全退出-->"+DocExtractLibray.Instance.DE_Exit());
         */

        /*//测试导入自定义的情感词
        //1、初始化
		if ( DocExtractLibray.Instance.DE_Init("E:\\java\\workspace\\platform\\src\\main\\resources\\DocExtractor", 1, "") == 0 ) {
			System.out.println("DocExtractor初始化失败：" + DocExtractLibray.Instance.DE_GetLastErrMsg());
			System.exit(1);
		}
		System.out.println("DocExtractor初始化成功");

		//2、导入自定义词典
		System.out.println("成功导入的自定义词个数：" + DocExtractLibray.Instance.DE_ImportUserDict("dict/userdic.txt"));
		//3、导入自定义情感词典
		System.out.println("成功导入的情感词个数：" + DocExtractLibray.Instance.DE_ImportSentimentDict("dict/mySentimentDict.txt"));

		//4、退出
		System.out.println("是否安全退出-->"+DocExtractLibray.Instance.DE_Exit());

        //测试导入黑名单，注意：黑名单中的词不会出现在关键词中。
        if ( DocExtractLibray.Instance.DE_Init("E:\\java\\workspace\\platform\\src\\main\\resources\\DocExtractor", 1, "") == 0 ) {
			System.out.println("DocExtractor初始化失败：" + DocExtractLibray.Instance.DE_GetLastErrMsg());
			System.exit(1);
		}
		System.out.println("DocExtractor初始化成功");
		System.out.println("成功导入的黑名单词个数：" + DocExtractLibray.Instance.DE_ImportKeyBlackList("dict/myKeyBlackList.txt"));

		System.out.println("是否安全退出-->"+DocExtractLibray.Instance.DE_Exit());
*/
        /*
        测试文章实体抽取
         */
//        AnnotateTask task=new AnnotateTask();
//        task.setAnnotator(annotator);
//        task.setUpdate_time(DateGenerate.getDate());
//        task.setPublisher(annotator);
//        task.setCreated_time(DateGenerate.getDate());
//        task.setDoc_id(doc_id);
        if (DocExtractLibray.Instance.DE_Init("E:\\java\\workspace\\platform\\src\\main\\resources\\DocExtractor", 1, "") == 0) {
            System.out.println("初始化失败："
                    + DocExtractLibray.Instance.DE_GetLastErrMsg());
            System.exit(1);
        }
        System.out.println("初始化成功");

        String content = "新华社北京2月10日电 （记者隋笑飞）中共中央政治局常委、中央书记处书记刘云山2月8日和9日，代表习近平总书记和党中央看望文化界知名人士，向他们致以诚挚问候，向广大文化工作者致以新春祝福。\n" +
                "\n" +
                "　　刘云山首先来到中国人民大学教授、著名马克思主义哲学专家陈先达家中，关切询问陈先达的生活和工作情况，对他为党的思想理论建设作出的贡献给予肯定，陈先达就深化马克思主义理论研究、加强哲学社会科学教材教学工作提出建议。在中国文联荣誉委员、著名书法家沈鹏家中，刘云山悉心了解书法艺术传承发展情况，希望老一辈书法家继续发挥传帮带作用、为弘扬中华优秀传统文化贡献力量。在看望中国舞协名誉主席、著名芭蕾舞表演艺术家白淑湘时，刘云山赞赏她为芭蕾舞民族化进行的探索，白淑湘建议加强青年艺术人才培养、加大对代表国家水准的艺术门类扶持力度。在看望原新闻出版署署长、著名出版家宋木文时，刘云山与他就出版业现状和前景进行交流，认真听取他关于提高出版质量、加强版权保护、重视社会效益等建议。在中国作协名誉委员、著名少数民族作家玛拉沁夫家中，刘云山赞扬玛拉沁夫为民族文学发展做出的成绩，并与他就加强少数民族文艺创作、繁荣中华民族文艺园地进行探讨。\n" +
                "\n" +
                "　　文化界知名人士对习近平总书记和党中央的亲切关怀表示感谢，对党的十八大以来党治国理政的新举措新局面高度赞誉，对党和政府重视弘扬优秀传统文化、提升国家文化软实力等部署深表赞同，一致认为文化工作者赶上了好时代，文化发展展示出更加美好的前景。刘云山指出，国运兴、文运兴，文化是民族生存和发展的重要力量，实现中华民族伟大复兴的中国梦需要文化的繁荣兴盛。推进“四个全面”战略布局，赋予当代文化工作者重要责任和使命。希望广大文化工作者深入学习贯彻习近平总书记在文艺工作座谈会上的重要讲话精神，增强文化自信，坚守文化追求，树立正确创作导向，用更多更好的文化作品讲好中国故事、反映时代进步。要强化精品意识，学习老一辈文化工作者的优良传统，努力在扎根生活、扎根群众中丰富生活积淀，在深化艺术实践、积极探索创新中提高文艺表现力，为推动文化繁荣发展、建设社会主义文化强国作出积极贡献。各级党委、政府和有关部门要重视文化建设、关心文化人才，加强扶持引导、多办实事好事，为文化工作者施展才华创造良好条件。\n" +
                "\n" +
                "　　中共中央政治局委员、中宣部部长刘奇葆陪同看望。中宣部、教育部、文化部、新闻出版广电总局、中国文联、中国作协有关负责同志参加看望活动。\n" +
                "\n" +
                "\n" +
                "　　《 人民日报 》（ 2015年02月11日 01 版）";
//		String content = "王石是呼市中共代表团万科的";
        int score=DocExtractLibray.Instance.DE_ComputeSentimentDoc(content);
        System.out.println("--->score--->"+score);
        NativeLong handle = DocExtractLibray.Instance.DE_ParseDocE(content, "mgc#ngd",
                true, DocExtractLibray.ALL_REQUIRED);
        System.out.println("抽取的人名为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_PERSON));
        System.out.println("抽取的地名为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_LOCATION));
        System.out.println("抽取的机构名为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_ORGANIZATION));
        System.out.println("抽取的关键词为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_KEYWORD));
        System.out.println("抽取的文章作者为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_AUTHOR));
        System.out.println("抽取的媒体为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_MEDIA));
        System.out.println("抽取的文章对应的所在国别为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_COUNTRY));
        System.out.println("抽取的文章对应的所在省份为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_PROVINCE));
        System.out.println("抽取的文章摘要为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_ABSTRACT));
        System.out.println("输出文章的正面情感词为-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_POSITIVE));
        System.out.println("输出文章的副面情感词-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_NEGATIVE));
        System.out.println("输出文章原文-->" + content);
        System.out.println("输出文章去除网页等标签后的正文-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_DEL_HTML));
        System.out.println("去除空格:" + DocExtractLibray.Instance.DE_GetResult(handle, 11).replaceAll("[　*| *| *|//s*]*", ""));

        System.out.println("自定义词(mgc)-->"
                + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_USER_DEFINED + 1));
        System.out.println("情感值---->" + DocExtractLibray.Instance.DE_GetSentimentScore(handle));
        DocExtractLibray.Instance.DE_ReleaseHandle(handle);

        System.out.println("是否安全退出-->"+DocExtractLibray.Instance.DE_Exit());
        return null;
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
