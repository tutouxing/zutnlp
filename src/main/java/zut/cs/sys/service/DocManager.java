package zut.cs.sys.service;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.web.multipart.MultipartFile;
import zut.cs.sys.domain.AnnotateTask;
import zut.cs.sys.domain.Doc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//public interface DocManager extends GenericManager<Doc, Long> {
public interface DocManager  {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/2/4$ 22:06$

     */

    Boolean save(String title, MultipartFile file,String user) throws IOException;

    Boolean delDocById(String id);

    Boolean updateDoc(Doc doc);
    Boolean saveReAnnotateByUser(String annotator,ArrayList<String> words,String doc_id,String task_id);
    Boolean mergeAnnotation(ArrayList<String> words,String doc_id,String task1_id,String task2_id,String annotator);

    Doc findDocById(String id) throws IOException;
    Doc findByName(String annotator) throws IOException;
    List<Doc> findAllDocs();
    List<Doc> findAllDocsByMulti();
    ArrayList<AnnotateTask> getAllTasks();
    List<AnnotateTask> findAllTaskByDocId(String id);

/*业务处理*/
    //分词词性标注命名实体抽取
    Boolean segmentWord(String id,String annotate_type,String username) throws Exception;
    Boolean recallPublish(String doc_id,String annotation_type);
    Boolean passInitialReview(String doc_id,String task_id);
    Boolean passFinalReview(String doc_id,String task_id);
    String[] reAnnotation(String str, String annotation_type);

    //文本分类
    String textClassify(String doc_id) throws IOException;
    Boolean saveClassifyResult(String doc_id, String result);
    Boolean recallClassifyResult(String doc_id);

    //机器翻译
    String machineTranslate(String text, String targetLaug);
}
