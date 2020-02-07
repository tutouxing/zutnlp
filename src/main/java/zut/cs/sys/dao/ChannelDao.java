package zut.cs.sys.dao;

import zut.cs.sys.base.dao.GenericDao;
import zut.cs.sys.domain.Channel;
import zut.cs.sys.domain.User;

import java.util.Set;

public interface ChannelDao extends GenericDao<Channel, Long> {
    Set<Channel> findByUser(User user);
}
