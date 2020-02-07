package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.base.rest.GenericController;
import zut.cs.sys.domain.User;
import zut.cs.sys.service.UserManager;

import java.util.List;

/**
 * @author:caochaoqiang
 * @date:2018/11/18
 * @email:1959724905@qq.com
 * @description:
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController extends GenericController<User, Long, UserManager> {
    UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
        this.manager = this.userManager;
    }

    @ApiOperation(value = "通过用户名查找")
    @GetMapping("getByName")
    public User getUser(@RequestParam String username) {
        return this.userManager.findByUsername(username);
    }

    //设置用户页面
    @ApiOperation(value = "设置用户页面")
    @PostMapping("setPages")
    public void setPages(@RequestParam String userId, @RequestParam List<String> pages) {
        User user = userManager.findById(Long.valueOf(userId));
        user.setTheme(pages.get(2));
        user.setPage1(pages.get(0));
        user.setPage2(pages.get(1));
        userManager.save(user);
    }
}
