package zut.cs.sys.service;

import zut.cs.sys.domain.Doc;

import java.util.List;


//public interface DocManager extends GenericManager<Doc, Long> {
public interface DocManager  {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/2/4$ 22:06$

     */
    List<Doc> findByAnnotator(String annotator);
    String saveObj(Doc doc);

    Boolean delDoc(Doc doc);
    Doc findDocById(String id);
    Boolean updateDoc(Doc doc);
    List<Doc> findAllDocs();
    List<Doc> findAllDocsByMulti();
    List<Doc> findAllTask();
    Boolean publishTask(Doc doc);
    Boolean processDoc(Doc doc);
}
