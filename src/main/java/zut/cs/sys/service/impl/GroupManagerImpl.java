package zut.cs.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zut.cs.sys.base.service.impl.GenericTreeManagerImpl;
import zut.cs.sys.dao.GroupDao;
import zut.cs.sys.dao.MenuDao;
import zut.cs.sys.dao.UserDao;
import zut.cs.sys.domain.Group;
import zut.cs.sys.domain.Menu;
import zut.cs.sys.domain.User;
import zut.cs.sys.service.GroupManager;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * @author:caochaoqiang
 * @date:2018/11/18
 * @email:1959724905@qq.com
 * @description:
 */
@Service
@Transactional
public class GroupManagerImpl extends GenericTreeManagerImpl<Group, Long> implements GroupManager {
    GroupDao groupDao;
    @Autowired
    UserDao userDao;
    @Autowired
    MenuDao menuDao;

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
        this.treeDao = this.groupDao;
        this.dao = this.treeDao;
    }

    /**
     * @para groupId
     * @para usersId
     * @description: 将usersId添加到group
     */
//    @CachePut(value = "group",key = "#groupId")
    public void addUsers(String groupId, List<String> usersId) {
        Group group = groupDao.getOne(Long.valueOf(groupId));
        for (String userId : usersId) {
            User user = userDao.getOne(Long.valueOf(userId));
            user.setGroup(group);
        }
    }

    /**
     * @para groupId
     * @description: 通过groupId查找其下所有用户
     */
//    @Cacheable(value = "group",key = "groupId")
    public Set<User> getUsers(String groupId) {
        Group group = groupDao.getOne(Long.valueOf(groupId));
        return userDao.findByGroup(group);
    }

    /**
     * @para groupId
     * @para usersId
     * @description: 移除该组下的users
     */
//    @CacheEvict(value = "group",key = "#usersId")
    @Override
    public void removeUsers(List<String> usersId) {
        for (String userId : usersId) {
            User user = userDao.getOne(Long.valueOf(userId));
            user.setGroup(null);
        }
    }

//    @CachePut(value = "group",key = "#groupId.concat(#menusId)")
    @Override
    public void addMenus(String groupId, List<String> menusId) {
        Group group = groupDao.getOne(Long.valueOf(groupId));
        Set<Menu> menus = group.getMenus();
        for (String menuId : menusId) {
            Menu menu = menuDao.getOne(Long.valueOf(menuId));
            menus.add(menu);
        }
        group.setMenus(menus);
        groupDao.save(group);
    }

//    @Cacheable(value = "group",key = "groupId")
    @Override
    public Set<Menu> getMenus(String groupId) {
        Group group = groupDao.getOne(Long.valueOf(groupId));
        return group.getMenus();
    }

//    @CachePut(value = "group",key = "#groupId.concat(#menusId)")
    @Override
    public void updateMenus(String grouId, List<String> menusId) {
        addMenus(grouId, menusId);
    }


//    @Cacheable(value = "groups")
    public List<Group> getAllGroup() {
        return groupDao.findGroupsByParentIsNull();
    }

}
