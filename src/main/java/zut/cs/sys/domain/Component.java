package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class Component extends BaseEntity {
    private String name;
    private String modeId;
    @JsonIgnore
    @OneToOne
    Channel channel;
    @JsonIgnore
    @OneToOne
    User user;
}
