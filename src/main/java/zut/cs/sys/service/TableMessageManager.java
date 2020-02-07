package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericTreeManager;
import zut.cs.sys.domain.Connection;
import zut.cs.sys.domain.Props;
import zut.cs.sys.domain.TableMessage;

import java.util.List;

public interface TableMessageManager extends GenericTreeManager<TableMessage, Long> {
    TableMessage findByTableName(String TableName);

    List<Props> findAllprops(String TableName);

    Boolean updata(TableMessage tableMessage);

    List<Connection> findAllConnections(String TableName);
}
