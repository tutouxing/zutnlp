package zut.cs.sys.dao;

import zut.cs.sys.base.dao.GenericDao;
import zut.cs.sys.domain.Component;
import zut.cs.sys.domain.User;

import java.util.Set;

public interface ComponentDao extends GenericDao<Component, Long> {
    Set<Component> findByUser(User user);
}
