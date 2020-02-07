package zut.cs.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import zut.cs.sys.base.service.impl.GenericManagerImpl;
import zut.cs.sys.dao.DocDao;
import zut.cs.sys.domain.Doc;
import zut.cs.sys.service.DocManager;


import javax.transaction.Transactional;
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
//    DocDao docDao;
    @Autowired
    private MongoTemplate mongoTemplate;
//    @Autowired
//    public void setDocDao(DocDao docDao){
//        this.docDao = docDao;
//        this.dao = this.docDao;
//    }

    @Override
    public List<Doc> findByAnnotator(String annotator) {
        Query query = new Query(Criteria.where("name").is(annotator));
        return (List<Doc>) mongoTemplate.findOne(query,Doc.class);
//        return docDao.findDocByAnnotator(annotator);
    }

    @Override
    public String setObj(Doc doc) {
//        doc.setUpdate_time(new Date());
//        docDao.save(doc);
//        mongoTemplate.insert(doc);
        mongoTemplate.save(doc);
        return "insert success!";
    }
}
