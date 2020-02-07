package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
public class Content extends BaseEntity {
    private String href;
    private String title;
    private String text;
    private String textHref;
    private String type;
    private Integer rank;
    private String publish;
    private Integer click_count;
    private String status;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "channel_id")
    Channel channel;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @OneToMany
    Set<Picture> pictures;
//    @OneToMany(cascade = CascadeType.REMOVE)
//    Set<Comment> comments;
}
