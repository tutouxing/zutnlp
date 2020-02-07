package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericManager;
import zut.cs.sys.domain.Channel;
import zut.cs.sys.domain.User;

import java.util.Set;

/*
    Authod：dd

*/

public interface ChannelManager extends GenericManager<Channel, Long> {
    Set<Channel> findUsers(User user);
}