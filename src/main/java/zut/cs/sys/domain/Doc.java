package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.*;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


@Getter
@Setter
@Document(collection = "docs")
//public class Doc extends BaseEntity {
public class Doc  implements Serializable {
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
    //文档内容
    private String content;
    //文档类型
    private String type;
    //发布者
    private String publisher;
    //字数
    private Long len;
    //创建时间
    private String  created_time;
    //最新修改时间
    private String last_modified;
    //已发布
    private ArrayList<String> publish;
    //已完成
    private ArrayList<String> done;
    //任务列表
    @DBRef
    private ArrayList<AnnotateTask> tasks;
}
