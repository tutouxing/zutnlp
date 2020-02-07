package zut.cs.sys.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.base.rest.GenericController;
import zut.cs.sys.domain.Connection;
import zut.cs.sys.domain.TableMessage;
import zut.cs.sys.service.ConnectionManager;
import zut.cs.sys.service.TableMessageManager;

import java.util.ArrayList;
import java.util.List;

/*
    Authod：NoOne！

*/

@Controller
@RequestMapping("/Connection")
public class ConnectionController extends GenericController<Connection, Long, ConnectionManager> {
    ConnectionManager connectionManager;

    @Autowired
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.manager = this.connectionManager;
    }

    //+++++++++
    @Autowired
    TableMessageManager tableMessageManager;

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Connection> list() {
        List<Connection> connectionList = new ArrayList<Connection>();
        connectionList = this.connectionManager.findAll();
        return connectionList;
    }

    @PostMapping(value = "/add", consumes = "application/json;charset=UTF-8", produces = {"application/json"})
    @ResponseBody
    public Boolean add(@RequestBody Connection connection,
                       //++++++++
                       @RequestParam("TableMessageName") String TableMessageName) {
        TableMessage tableMessage = this.tableMessageManager.findByTableName(TableMessageName);
        if (tableMessage != null) {
            System.out.println("hhhhh，找到了tableMessage");
            if (this.connectionManager.findByConnectionName(connection.getConnectionName()) == null) {
                System.out.println("hhhhh，connectionManager不为空");
                connection.setTableMessage(tableMessage);
                this.connectionManager.save(connection);
                //
                int a = tableMessage.getSonNumber();
                if (connection.getTableConnectionSonTableName() != null) {
                    a = a + 1;
                    System.out.println("hhhhh，这里更改了son" + a);
                }
                tableMessage.setSonNumber(a);
                this.tableMessageManager.save(tableMessage);
                if (this.connectionManager.findByConnectionName(connection.getConnectionName()) != null) {
                    return true;
                }
            }
        } else {
            System.out.println(",表为空，不能建立属性在找到之后返回空");
            return false;
        }
        return false;
    }

    @ResponseBody
    @PostMapping(value = "/updata", consumes = "application/json")
    public boolean updata(@RequestBody Connection connection,
                          @RequestParam("ConnectionName") String ConnectionName) {
        if (this.connectionManager.findByConnectionName(ConnectionName) == null) {
            return false;
        } else {
            this.connectionManager.updata(connection);
            return true;
        }
    }

    @ResponseBody
    @PostMapping(value = "/check/{Connectionname}")
    public Connection findConnection(@PathVariable @RequestParam("ConnectionName") String ConnectionName) {
        if (this.connectionManager.findByConnectionName(ConnectionName) != null) {
            return this.connectionManager.findByConnectionName(ConnectionName);
        }
        return null;
    }

    @ResponseBody
    @PostMapping(value = "/delete")
    public boolean delete(@PathVariable @RequestParam("ConnectionName") String ConnectionName) {
        if (this.connectionManager.findByConnectionName(ConnectionName) == null) {
            return false;
        } else {
            Connection connection = this.connectionManager.findByConnectionName(ConnectionName);
            Long id = connection.getId();
            if (connection.getTableConnectionPropsOneOrTwo() == true) {
                TableMessage tableMessage = this.tableMessageManager.findByTableName(connection.getTableConnectionParentTableName());
                if (tableMessage != null) {
                    int a = tableMessage.getSonNumber() - 1;
                    tableMessage.setSonNumber(a);
                    this.tableMessageManager.save(tableMessage);
                } else {
                    return false;
                }
            }
            this.connectionManager.delete(id);
            if ((this.connectionManager.findByConnectionName(ConnectionName) == null) == true) {
                return true;
            }
        }
        return false;
    }


}

