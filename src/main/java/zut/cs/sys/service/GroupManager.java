package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericTreeManager;
import zut.cs.sys.domain.Group;
import zut.cs.sys.domain.Menu;
import zut.cs.sys.domain.User;

import java.util.List;
import java.util.Set;

public interface GroupManager extends GenericTreeManager<Group, Long> {
    public void addUsers(String groupId, List<String> usersId);

    public Set<User> getUsers(String groupId);

    public void removeUsers(List<String> usersId);

    public void addMenus(String groupId, List<String> menusId);

    public Set<Menu> getMenus(String groupId);

    public void updateMenus(String grouId, List<String> menusId);

    public List<Group> getAllGroup();
}
