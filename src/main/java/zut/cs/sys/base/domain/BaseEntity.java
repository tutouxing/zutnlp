package zut.cs.sys.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler","deleted","entityName"})
public class BaseEntity extends BaseDomain {

    private static final long serialVersionUID = -6163675075289529459L;


    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date dateCreated = new Date();

    /**
     * 实体修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date dateModified = new Date();

    /**
     * 实体是否被删除
     */
    protected Boolean deleted;

    protected String entityName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public String getEntityName() {
        return entityName;
    }

    public Long getId() {
        return id;
    }


    public boolean equals(Object obj) {
        if (null != obj) {
            if (obj instanceof BaseEntity) {
                BaseEntity domain = (BaseEntity) obj;
                if (this.id == domain.id) {
                    return true;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        if (this.id == null) {
            this.id = Long.valueOf(0);
        }
        return HashCodeBuilder.reflectionHashCode(this.id);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
