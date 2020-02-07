package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericManager;
import zut.cs.sys.domain.Connection;

/*
    Authod：NoOne！

*/

public interface ConnectionManager extends GenericManager<Connection, Long> {
    Connection findByConnectionName(String ConnectionName);

    Boolean updata(Connection connection);
}