package zut.cs.sys.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.base.rest.GenericController;
import zut.cs.sys.domain.Connection;
import zut.cs.sys.domain.Props;
import zut.cs.sys.domain.TableMessage;
import zut.cs.sys.service.TableMessageManager;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/TableMessage")
public class TableMessageController extends GenericController<TableMessage, Long, TableMessageManager> {
    TableMessageManager tableMessageManager;

    @Autowired
    public void setTableMessageManager(TableMessageManager tableMessageManager) {
        this.tableMessageManager = tableMessageManager;
        this.manager = this.tableMessageManager;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<TableMessage> list() {
        List<TableMessage> TableList = new ArrayList<TableMessage>();
        TableList = this.tableMessageManager.findAll();
        return TableList;
    }

    @PostMapping(value = "/add", consumes = "application/json;charset=UTF-8", produces = {"application/json"})
    @ResponseBody
    public Boolean add(@RequestBody TableMessage tableMessage) {
        if (this.tableMessageManager.findByTableName(tableMessage.getTablename()) == null) {
            this.tableMessageManager.save(tableMessage);
            return true;
        }
        return false;
    }

    @ResponseBody
    @PostMapping(value = "/delete")
    public boolean delete(@PathVariable @RequestParam("Tablename") String Tablename) {
        if (this.tableMessageManager.findByTableName(Tablename) == null) {
            return false;
        } else {
            Long id = this.tableMessageManager.findByTableName(Tablename).getId();
            this.tableMessageManager.delete(id);
            if ((this.tableMessageManager.findByTableName(Tablename) == null) == true) {
                return true;
            }
        }
        return false;
    }

    @ResponseBody
    @PostMapping(value = "/deleteById")
    public boolean deletebyId(@PathVariable @RequestParam("Id") Long id) {
        if (this.tableMessageManager.findById(id) == null) {
            return false;
        } else {
            Long getedId = this.tableMessageManager.findById(id).getId();
            this.tableMessageManager.delete(getedId);
            return true;
        }
    }

    @ResponseBody
    @PostMapping(value = "/updataById", consumes = "application/json")
    public boolean updataById(@RequestBody TableMessage tableMessage,
                              @RequestParam("Id") Long id) {
        if (this.tableMessageManager.findById(id) == null) {
            return false;
        } else {
            this.tableMessageManager.updata(tableMessage);
            return true;
        }
    }

    @ResponseBody
    @PostMapping(value = "/updata", consumes = "application/json")
    public boolean updata(@RequestBody TableMessage tableMessage,
                          @RequestParam("TableMessageName") String TableMessageName) {
        if (this.tableMessageManager.findByTableName(TableMessageName) == null) {
            return false;
        } else {
            this.tableMessageManager.updata(tableMessage);
            return true;
        }
    }

    @ResponseBody
    @PostMapping(value = "/check/{Tablename}")
    public TableMessage findTableMessage(@PathVariable @RequestParam("Tablename") String Tablename) {
        if (this.tableMessageManager.findByTableName(Tablename) != null) {
            return this.tableMessageManager.findByTableName(Tablename);
        }
        return null;
    }

    @ResponseBody
    @PostMapping(value = "/findMyProps")
    public List<Props> findAllProps(@RequestParam("TableMessageName") String Tablename) {
        TableMessage tableMessage = this.tableMessageManager.findByTableName(Tablename);
        if (tableMessage != null) {
            List<Props> propsList = tableMessage.getProps();
            return propsList;
        }
        return null;
    }

    @ResponseBody
    @PostMapping(value = "/findMyConnection")
    public List<Connection> findAllConnections(@RequestParam("ConnectionName") String Tablename) {
        TableMessage tableMessage = this.tableMessageManager.findByTableName(Tablename);
        if (tableMessage != null) {
            List<Connection> connectionsList = tableMessage.getConnections();
            return connectionsList;
        }
        return null;
    }

    @ResponseBody
    @PostMapping(value = "/addList")
    public Boolean addTableList(@RequestBody List<TableMessage> tableMessages) {
        for (int i = 0; i < tableMessages.size(); i++) {
            if (this.tableMessageManager.findByTableName(tableMessages.get(i).getTablename()) == null) {
                this.tableMessageManager.save(tableMessages.get(i));

            } else {
                return false;
            }
        }
        return true;
    }

    @ResponseBody
    @GetMapping(value = "GenALLProjectName")
    public List<String> genAllProjectName() {
        List<String> projectNameList = new ArrayList<>();
        List<TableMessage> tableMessages = this.tableMessageManager.findAll();
        if (tableMessages.size() != 0) {
            for (int i = 0; i < tableMessages.size(); i++) {
                String transProjectName = tableMessages.get(i).getProjectName();
                if (projectNameList.contains(transProjectName) == false) {
                    projectNameList.add(transProjectName);
                }
            }
        } else {
            return null;
        }
        return projectNameList;

    }
}
