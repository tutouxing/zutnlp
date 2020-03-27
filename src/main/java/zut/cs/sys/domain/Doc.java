package zut.cs.sys.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
//public class Doc extends BaseEntity {
public class Doc  {
    /**
     * @Description: doc which contains annotated and unannotated

     * @Author: wastelands

     * @CreateDate: 2020/2/4$ 4:46$

     */
    private static final long serialVersionUID = -4376674977047164L;
    @Id
    //文档标识
    private String doc_id;
    //文档名
    private String name;
    //状态：已标注，未标注
    private String status;
    //文档内容
    private String content;
    //标注类型：词性分析、关键字提取
    private String annotation_type;
    //阶段：初审、终审
    private String phrase;
    //分词结果
    private String[] word;
    //标注者：初审专家、终审专家
    private String annotator;
    //最后更新时间
    private String update_time;
    //是否发布
    private String publish;
    //
    private String done;
    //任务id
    private String task_id;

}
