package zut.cs.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zut.cs.sys.base.service.impl.GenericTreeManagerImpl;
import zut.cs.sys.dao.MenuDao;
import zut.cs.sys.domain.Menu;
import zut.cs.sys.service.MenuManager;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author:caochaoqiang
 * @date:2018/11/18
 * @email:1959724905@qq.com
 * @description:
 */
@Service
@Transactional
//@CacheConfig(cacheNames = "menu")
public class MenuManagerImpl extends GenericTreeManagerImpl<Menu, Long> implements MenuManager {
    MenuDao menuDao;

    @Autowired
    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
        this.treeDao = this.menuDao;
        this.dao = this.treeDao;
    }

//    @Cacheable(value = "menus")
    @Override
    public List<Menu> findAllMneus() {
        return menuDao.findMenusByParentIsNull();
    }

}
