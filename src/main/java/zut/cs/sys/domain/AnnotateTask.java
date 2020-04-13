package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.ArrayList;

@Document(collection = "tasks")
@Getter
@Setter
public class AnnotateTask implements Serializable {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/4/7$ 2:40$

     */
    private static final long serialVersionUID = -4376674977047164L;

    @Id
    private String task_id;
    //档案名称
    private String task_name;
    //任务类型：中文分词、词性分析、命名实体、专业术语、依存关系、语义角色、新词发现
    private String annotation_type;
    //阶段：初审、终审
    private String phrase;
    //分词结果
    private ArrayList<ArrayList<String>> segmentWord;
    //词性标注结果
    private ArrayList<ArrayList<String>> propertyWord;
    //标注者：初审专家、终审专家/admin
    private String annotator;
    //任务创建时间
    private String created_time;
    //最后更新时间
    private String update_time;
    //状态：待审核、待初审、待终审
    private String status;
    //对应文档id
    private String doc_id;
}
