package zut.cs.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zut.cs.sys.base.service.impl.GenericManagerImpl;
import zut.cs.sys.dao.ConnectionDao;
import zut.cs.sys.domain.Connection;
import zut.cs.sys.service.ConnectionManager;

/*
    Authod：NoOne！

*/
@Service
public class ConnectionManagerImpl extends GenericManagerImpl<Connection, Long> implements ConnectionManager {

    ConnectionDao connectionDao;

    @Autowired
    public void setConnectionDao(ConnectionDao connectionDao) {
        this.connectionDao = connectionDao;
        this.dao = this.connectionDao;
    }

    @Override
    public Connection findByConnectionName(String ConnectionName) {
        Connection connection = this.connectionDao.findByConnection_name(ConnectionName);
        return connection;
    }

    @Override
    public Boolean updata(Connection connection) {
        this.connectionDao.save(connection);
        return true;
    }
}