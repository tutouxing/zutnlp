package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zut.cs.sys.domain.Channel;
import zut.cs.sys.domain.Component;
import zut.cs.sys.domain.Group;
import zut.cs.sys.domain.User;
import zut.cs.sys.service.ChannelManager;
import zut.cs.sys.service.ComponentManager;
import zut.cs.sys.service.GroupManager;
import zut.cs.sys.service.UserManager;

import java.util.Set;

/**
 * @author:caochaoqiang
 * @date:2018/11/18
 * @email:1959724905@qq.com
 * @description:
 */
@RestController
@Api(tags = "登录接口")
public class LoginController {
    @Autowired
    UserManager userManager;
    @Autowired
    GroupManager groupManager;
    @Autowired
    ChannelManager channelManager;
    @Autowired
    ComponentManager componentManager;

    @GetMapping("balance")
    public String test(){
        return "我来自port:8848";
    }

    @PostMapping("/login")
    public UserInfo login(@RequestBody User user) {
        UserInfo userInfo = new UserInfo();
        User user1 = userManager.findByUsername(user.getUsername());
        if (user.getPassword().equals(user1.getPassword())) {
            //如果用户未分配组，进行分配至基础组(基本权限)
            if (user1.getGroup() == null) {
                Set<Channel> channels = channelManager.findUsers(user1);
                Set<Component> components = componentManager.findByUser(user1);
                Group group = groupManager.findById(Long.valueOf(5));
                user1.setGroup(group);
                userManager.save(user1);
                group.setTheme(user1.getTheme());
                group.setPage1(user1.getPage1());
                group.setPage2(user1.getPage2());
                group.setUserId(String.valueOf(user1.getId()));
                groupManager.save(group);
                userInfo.setGroup(group);
                userInfo.setChannel(channels);
                userInfo.setComponents(components);
                return userInfo;
            } else {
                Set<Channel> channels = channelManager.findUsers(user1);
                Set<Component> components = componentManager.findByUser(user1);
                Group group = user1.getGroup();
                group.setTheme(user1.getTheme());
                group.setPage1(user1.getPage1());
                group.setPage2(user1.getPage2());
                group.setUserId(String.valueOf(user1.getId()));
                groupManager.save(group);
                userInfo.setGroup(group);
                userInfo.setChannel(channels);
                userInfo.setComponents(components);
                return userInfo;
            }
        }
        return null;
    }
}
