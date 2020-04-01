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
    //任务类型：中文分词、词性分析、命名实体、专业术语、依存关系、语义角色、新词发现
    private String annotation_type;
    //阶段：初审、终审
    private String phrase;
    //分词结果
    private String[] word;
    //标注者：初审专家、终审专家
    private String annotator;
    //创建时间
    private String created_time;
    //最后更新时间
    private String update_time;
    //是否发布
    private String publish;
    //
    private String done;
    //任务id
    private String task_id;

}
