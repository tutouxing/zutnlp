package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Channel extends BaseEntity {
    private Integer today;
    private String chName;
    private Integer history;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @OneToMany(mappedBy = "channel", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    Set<Content> contents;
}
