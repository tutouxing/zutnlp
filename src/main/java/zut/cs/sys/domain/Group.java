package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import zut.cs.sys.base.domain.BaseTreeEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * @author:caochaoqiang
 * @date:2018/11/18
 * @email:1959724905@qq.com
 * @description:
 */
@Entity
@Table(name = "admin_group")
@Getter
@Setter
@NamedQueries({@NamedQuery(name = "Group.getRoot", query = "select g from Group g where g.parent is null")})
public class Group extends BaseTreeEntity<Group> {
    private static final long serialVersionUID = -175195222437199846L;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    Set<User> users;
    @ManyToMany
    @JoinColumn(name = "menus")
    Set<Menu> menus;
    private String theme;
    private String page1;
    private String page2;
    private String userId;
}