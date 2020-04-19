package zut.cs.sys.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Service;
import zut.cs.sys.dao.Key_wordsDao;
import zut.cs.sys.domain.Doc;
import zut.cs.sys.domain.Key_words;
import zut.cs.sys.service.DocManager;
import zut.cs.sys.service.impl.tfidf_from_python;

import java.io.IOException;
import java.util.List;

@Service
@Component
public class Key_wordsDaoImpl implements Key_wordsDao {

    @Autowired
    DocManager docManager;

    @Override
    public List<Key_words> getkeywords(String id) {
        String content= new String();
        try {
            Doc result = docManager.findDocById(id);
            content = result.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] str = new String[1];
        str[0]=content;
        System.out.println("打印得到的content的内容");
        System.out.println(str);
        List<Key_words> ss= tfidf_from_python.main(str);
        System.out.println("打印keywords的数组");
        System.out.println(ss);
        return ss;
    }
}
