package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.base.rest.GenericTreeController;
import zut.cs.sys.domain.Group;
import zut.cs.sys.domain.Menu;
import zut.cs.sys.domain.User;
import zut.cs.sys.service.GroupManager;
import zut.cs.sys.service.MenuManager;
import zut.cs.sys.service.UserManager;

import java.util.List;
import java.util.Set;

/**
 * @author:caochaoqiang
 * @date:2018/11/18
 * @email:1959724905@qq.com
 * @description:
 */
@RestController
@RequestMapping("group")
@Api(tags = "用户组接口")
public class GroupController extends GenericTreeController<Group, Long, GroupManager> {
    GroupManager groupManager;
    @Autowired
    UserManager userManager;
    @Autowired
    MenuManager menuManager;

    @Autowired
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
        this.treeManager = this.groupManager;
        this.manager = this.treeManager;
    }

    @ApiOperation(value = "根据组ID添加用户")
    @PostMapping("addUsers")
    public void addUser(@RequestParam String groupId, @RequestParam List<String> usersId) {
        if (usersId.size() > 0) {
            this.groupManager.addUsers(groupId, usersId);
        }
    }

    @ApiOperation(value = "通过组ID查找用户(return 数组)")
    @GetMapping("getUsers")
    public Set<User> getUsers(@RequestParam String groupId) {
        return this.groupManager.getUsers(groupId);
    }

    @ApiOperation(value = "删除多个用户")
    @DeleteMapping("removeUsers")
    public void removeUsers(@RequestParam List<String> usersId) {
        this.groupManager.removeUsers(usersId);
    }

    @ApiOperation(value = "根据组ID得到所有菜单")
    @GetMapping("getMenus")
    public Set<Menu> getMenus(@RequestParam String groupId) {
        return this.groupManager.getMenus(groupId);
    }

    @ApiOperation(value = "添加多个菜单")
    @PostMapping("addMenus")
    public void addMenus(@RequestParam String groupId, @RequestParam List<String> menusId) {
        this.groupManager.addMenus(groupId, menusId);
    }

    @ApiOperation(value = "更新多个菜单")
    @PutMapping("updateMneus")
    public void updateMneus(@RequestParam String groupId, @RequestParam List<String> menusId) {
        this.groupManager.updateMenus(groupId, menusId);
    }

    @ApiOperation(value = "得到所有用户组")
    @GetMapping("list")
    public List<Group> getList() {
        return groupManager.getAllGroup();
    }
}
