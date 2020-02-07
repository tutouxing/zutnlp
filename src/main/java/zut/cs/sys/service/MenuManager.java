package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericTreeManager;
import zut.cs.sys.domain.Menu;

import java.util.List;

public interface MenuManager extends GenericTreeManager<Menu, Long> {
    //返回所有菜单（parent_id=null）
    List<Menu> findAllMneus();
}
