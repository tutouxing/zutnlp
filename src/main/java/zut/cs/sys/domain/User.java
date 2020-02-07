package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * @author:caochaoqiang
 * @date:2018/11/16
 * @email:1959724905@qq.com
 * @description:
 */
@Entity
@Getter
@Setter
public class User extends BaseEntity {

    private static final long serialVersionUID = -4376674977047164L;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "group_id")
    Group group;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    Set<Channel> channels;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    Set<Content> contents;

    private String userAccount;
    private String gender;
    private String password;
    private String username;
    private String email;
    private String theme;
    private String page1;
    private String page2;
    private String page3;
    private String page4;
    private String page5;
}
