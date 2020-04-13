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
    public void test() throws Exception {
        Doc doc=new Doc();
//        docManager.recallPublish("e0c60675952e4d71be2336ae60fdfe3b","中文分词");
//        doc.setContent("哎~那个金刚圈尺寸太差，前重后轻，左宽右窄，他戴上去很不舒服");
//        boolean flag=docManager.segmentWord("f2789f22cead4a69b781af26fd44a739","中文分词");
//        boolean flag2=docManager.segmentWord("f2789f22cead4a69b781af26fd44a739","词性标注");
        boolean flag1=docManager.segmentWord("f2789f22cead4a69b781af26fd44a739","关键词提取");
//        System.out.println(flag);
    }
}
