package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericManager;
import zut.cs.sys.domain.Content;

import java.util.List;

/*
    Authod：dd

*/

public interface ContentManager extends GenericManager<Content, Long> {
    List<Content> findByTitle(String title);

    List<Content> findAll(Long userId, Long channelId);

    Content addByChannelAndUser(Content content, String channelId, String userId);


}