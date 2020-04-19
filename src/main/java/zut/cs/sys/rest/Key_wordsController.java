package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.dao.Key_wordsDao;
import zut.cs.sys.domain.Key_words;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要分析每篇文章的controller
 */
@RestController
@RequestMapping("/word")
@Api(tags = "文档关键词",description = "getWord Controller")
public class Key_wordsController {

    @Autowired
    Key_wordsDao keyWordsDao;

    @ApiOperation(value = "根据id得到文档的关键词")
    @GetMapping("/{id}")
    public List<Key_words> getWord(@PathVariable String id){

        String str=id;
        System.out.println("词云展示");
        /* str = str.substring(0,str.length() - 1);*/
        List<Key_words> ss=keyWordsDao.getkeywords(str);
        return ss;
     /* return lists_keyword;*/
    }

/*    @ApiOperation(value="测试请求的body类型")
    @PostMapping("/getword")
    public List<Key_words> gettest(@RequestBody String id){
        System.out.println("词云展示");
        String str=id;
        *//* str = str.substring(0,str.length() - 1);*//*
        List<Key_words> ss=keyWordsDao.getkeywords(str);
        return ss;
    }*/

    @ApiOperation(value = "简单进行前后端可是")
    @PostMapping("/test")
    public List<Key_words> test(){
        List<Key_words> lists_keyword = new ArrayList<>();
        Key_words sa1 = new Key_words("邓钰琪", 100.0);
        Key_words sa2 = new Key_words("牛文涛", 1.0);
        Key_words sa3 = new Key_words("钟娟", 1.0);
        Key_words sa4 = new Key_words("杨立光", 1.0);
        Key_words sa5 = new Key_words("赵师柳", 1.0);
        Key_words sa6 = new Key_words("李博文", 1.0);
        Key_words sa7 = new Key_words("彭瑶瑶", 1.0);
        lists_keyword.add(sa1);
        lists_keyword.add(sa2);
        lists_keyword.add(sa3);
        lists_keyword.add(sa4);
        lists_keyword.add(sa5);
        lists_keyword.add(sa6);
        lists_keyword.add(sa7);
        System.out.println(lists_keyword);
        return lists_keyword;
    }
}
