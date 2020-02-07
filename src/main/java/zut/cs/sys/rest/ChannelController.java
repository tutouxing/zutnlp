package zut.cs.sys.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.base.rest.GenericController;
import zut.cs.sys.domain.Channel;
import zut.cs.sys.domain.User;
import zut.cs.sys.service.ChannelManager;
import zut.cs.sys.service.UserManager;
import zut.cs.sys.util.UpdateUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/Channel")
@Api(tags = "栏目接口")
public class ChannelController extends GenericController<Channel, Long, ChannelManager> {
    ChannelManager channelManager;

    @Autowired
    UserManager userManager;

    @Autowired
    public void setChannelManager(ChannelManager channelManager) {
        this.channelManager = channelManager;
        this.manager = this.channelManager;
    }

    @ApiOperation(value = "通过用户ID添加栏目")
    @PostMapping("add/")
    public Channel addByUserId(@RequestBody Channel model, String userId) {
        model.setDateCreated(new Date());
        model.setUser(userManager.findById(Long.valueOf(userId)));
        channelManager.save(model);
        return model;
    }

    @ApiOperation(value = "更新今日浏览量")
    @PutMapping("update/today")
    public String updateToday(@RequestBody Channel[] params) {
        for (Channel param : params) {
            Channel channel = channelManager.findById(param.getId());
            channel.setToday(param.getToday());
            channel.setHistory(param.getToday() + channel.getHistory());
            channelManager.save(channel);
        }
        return "success";
    }

    @ApiOperation(value = "更改栏目")
    @PutMapping("update/")
    public Channel update(@RequestBody Channel channel, String channelId, String userId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        channel.setDateModified(new Date());
        channel.setUser(userManager.findById(Long.valueOf(userId)));
        Channel channel1 = channelManager.findById(Long.valueOf(channelId));
        Channel channel2 = (Channel) UpdateUtil.get(channel, channel1);
        channel2.setId(Long.valueOf(channelId));
        channelManager.save(channel2);
        return channel2;
    }


    @GetMapping("userid")
    public Set<Channel> getChannelById(@RequestParam long id) {
        User user = new User();
        user.setId(id);
        return channelManager.findUsers(user);
    }

    @ApiOperation(value = "得到当前用户下的所有栏目")
    @GetMapping("/list")
    public Set<Channel> getAll(String userId) {
        return channelManager.findUsers(userManager.findById(Long.valueOf(userId)));
    }
}

