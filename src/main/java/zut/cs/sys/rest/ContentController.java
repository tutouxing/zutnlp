package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.base.rest.GenericController;
import zut.cs.sys.domain.Content;
import zut.cs.sys.service.ChannelManager;
import zut.cs.sys.service.ContentManager;
import zut.cs.sys.service.PictureManager;
import zut.cs.sys.service.UserManager;

import java.util.Date;
import java.util.List;
/*
    Authod：dd
*/

@Controller
@RequestMapping("/Content")
@Api(tags = "内容接口")
public class ContentController extends GenericController<Content, Long, ContentManager> {
    ContentManager contentManager;
    @Autowired
    ChannelManager channelManager;
    @Autowired
    UserManager userManager;
    @Autowired
    PictureManager pictureManager;

    @Autowired
    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
        this.manager = this.contentManager;
    }

    @PostMapping("add/")
    public Content addByChannelAndUser(@RequestBody Content content, String channelId, String userId) {
        return contentManager.addByChannelAndUser(content, channelId, userId);
    }

    @PutMapping("update/")
    public Content updateByChannelAndUserId(@RequestBody Content content, String contentId, String channelId, String userId) {
        content.setDateModified(new Date());
        content.setId(Long.valueOf(contentId));
        content.setChannel(channelManager.findById(Long.valueOf(channelId)));
        content.setUser(userManager.findById(Long.valueOf(userId)));
        contentManager.save(content);
        return content;
    }

    @ApiOperation(value = ("通过标题查找内容(return 数组)"))
    @GetMapping("getByTitle/")
    public List<Content> getByTitle(String title) {
        return contentManager.findByTitle(title);
    }

    @ApiOperation(value = "得到当前用户和栏目下的所有内容包括图片")
    @GetMapping("/list")
    public List<Content> getAll(Long userId, Long channelId) {
        return contentManager.findAll(userId, channelId);
    }
}
