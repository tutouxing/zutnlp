package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
public class Picture extends BaseEntity {
    private String url;
    @JsonIgnoreProperties(value = "pictures")
    @ManyToOne
    @JoinColumn(name = "contentId")
    Content content;
}