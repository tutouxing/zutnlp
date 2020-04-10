package zut.cs.sys.service;

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

    Boolean save(String title, MultipartFile file) throws IOException;

    Boolean delDocById(String id);

    Boolean updateDoc(Doc doc);

    Doc findDocById(String id) throws IOException;
    Doc findByName(String annotator) throws IOException;
    List<Doc> findAllDocs();
    List<Doc> findAllDocsByMulti();
    ArrayList<AnnotateTask> getAllTasks();
    List<AnnotateTask> findAllTaskByDocId(String id);

    Boolean segmentWord(String id,String annotate_type) throws Exception;
    Boolean recallPublish(String doc_id,String annotation_type);
}
