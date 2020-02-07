package zut.cs.sys.dao;

import zut.cs.sys.base.dao.GenericTreeDao;
import zut.cs.sys.domain.Menu;

import java.util.List;

public interface MenuDao extends GenericTreeDao<Menu, Long> {
    List<Menu> findMenusByParentIsNull();
}
