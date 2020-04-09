package test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import zut.cs.sys.SysRunner;
import zut.cs.sys.domain.Doc;
import zut.cs.sys.service.DocManager;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SysRunner.class)
public class DocTest {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/2/6$ 2:42$

     */
    @Autowired
    private DocManager docManager;
    @Test
    public void test(){
        Doc doc=new Doc();
//        doc.setContent("哎~那个金刚圈尺寸太差，前重后轻，左宽右窄，他戴上去很不舒服");
//        boolean flag=docManager.processDoc(doc);
//        System.out.println(flag);
    }
}
