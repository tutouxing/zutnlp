package zut.cs.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zut.cs.sys.base.service.impl.GenericManagerImpl;
import zut.cs.sys.dao.ChannelDao;
import zut.cs.sys.domain.Channel;
import zut.cs.sys.domain.User;
import zut.cs.sys.service.ChannelManager;

import java.util.Set;


/*
    Authod：dd

*/
@Service
class ChannelManagerImpl extends GenericManagerImpl<Channel, Long> implements ChannelManager {

    ChannelDao channelDao;

    @Autowired
    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
        this.dao = this.channelDao;
    }

    @Override
    public Set<Channel> findUsers(User user) {
        return channelDao.findByUser(user);
    }
}