package zut.cs.sys.dao;

import zut.cs.sys.base.dao.GenericDao;
import zut.cs.sys.domain.Doc;

import java.util.List;

public interface DocDao  {
//public interface DocDao extends GenericDao<Doc,Long> {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/2/4$ 4:46$

     */
    List<Doc> findDocByAnnotator(String annotator);
//    List<Doc> findDocByAnnotation_type(String annotation_type);

}
