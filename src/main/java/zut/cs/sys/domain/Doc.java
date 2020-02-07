package zut.cs.sys.domain;

import lombok.Getter;
import lombok.Setter;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
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
    private Long doc_id;
    private String name;
    private String status;
    private String content;
    private String annotation_type;
    private String phrase;
    private String word;
    private String annotator;
    private Date update_time;
}
