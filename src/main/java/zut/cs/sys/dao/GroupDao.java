package zut.cs.sys.dao;

import zut.cs.sys.base.dao.GenericTreeDao;
import zut.cs.sys.domain.Group;

import java.util.List;

public interface GroupDao extends GenericTreeDao<Group, Long> {
    List<Group> findGroupsByParentIsNull();
}
