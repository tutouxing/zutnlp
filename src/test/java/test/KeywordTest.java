package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import zut.cs.sys.SysRunner;
import zut.cs.sys.dao.Key_wordsDao;
import zut.cs.sys.service.impl.tfidf_from_python;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SysRunner.class)
public class KeywordTest {

    @Autowired
     private Key_wordsDao key_wordsDao;

    @Test
    public void test_get_keyword(){
        String[] str=new String[1];
        str[0]="4月初，在海南三亚师部农场的南繁基地稻田里，袁隆平杂交水稻创新团队带头人、湖南省农业科学院副院长邓华凤正在进行新品种选种。每年都有来自不同省份的近7000名科研人员来到这里从事南繁工作。新中国成立以来，有超过19950个农作物新品种出自这里，占到全国育成新品种的70%。\n" +
                "\n" +
                "作为国家重大战略服务保障区，两年来，海南为航天发射、深海科考、南繁育种等全力提供硬核支撑。长征七号、长征五号成功飞向太空，中科院“探索一号”走向深海。如今，三亚崖州湾科技城正在对南山港进行升级改造，提升科研和海上实验服务保障能力。同时搭建科研公共平台，建设深海科技公共创新平台、南繁公共创新中心、中国种子创新研究院等，为科研项目提供保障。";
        tfidf_from_python.main(str);
    }

    @Test
    public void test(){
        key_wordsDao.getkeywords("2e6b8d8630e34657aee2bf0a15d6c11c");
    }
}
