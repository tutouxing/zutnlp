package zut.cs.sys.dao;

import zut.cs.sys.base.dao.GenericDao;
import zut.cs.sys.domain.Group;
import zut.cs.sys.domain.User;

import java.util.Set;

public interface UserDao extends GenericDao<User, Long> {
    User findByUsername(String username);

    Set<User> findByGroup(Group group);
}
