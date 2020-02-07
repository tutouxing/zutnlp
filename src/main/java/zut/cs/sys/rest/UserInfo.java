package zut.cs.sys.rest;

import lombok.Getter;
import lombok.Setter;
import zut.cs.sys.domain.Channel;
import zut.cs.sys.domain.Component;
import zut.cs.sys.domain.Group;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class UserInfo implements Serializable {
    private Group group;
    private Set<Channel> channel;
    private Set<Component> components;
}
