package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zut.cs.sys.base.rest.GenericTreeController;
import zut.cs.sys.domain.Menu;
import zut.cs.sys.service.MenuManager;

import java.util.List;

@RestController
@RequestMapping("menu")
@Api(tags = "菜单接口")
public class MenuController extends GenericTreeController<Menu, Long, MenuManager> {
    MenuManager menuManager;

    @Autowired
    public void setMenuManager(MenuManager menuManager) {
        this.menuManager = menuManager;
        this.treeManager = this.menuManager;
        this.manager = this.treeManager;
    }

    @ApiOperation(value = "得到所有菜单")
    @GetMapping("list")
    public List<Menu> getAll() {
        return menuManager.findAllMneus();
    }
}
