package zut.cs.sys.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.base.rest.GenericController;
import zut.cs.sys.domain.Props;
import zut.cs.sys.domain.TableMessage;
import zut.cs.sys.service.PropsManager;
import zut.cs.sys.service.TableMessageManager;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Pros")
public class PropsController extends GenericController<Props, Long, PropsManager> {
    PropsManager propsManager;

    @Autowired
    public void setPropsManager(PropsManager propsManager) {
        this.propsManager = propsManager;
        this.manager = this.propsManager;
    }

    @Autowired
    TableMessageManager tableMessageManager;

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Props> list() {
        List<Props> propsList = new ArrayList<Props>();
        propsList = this.propsManager.findAll();
        return propsList;
    }

    @PostMapping(value = "/add", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public Boolean add(@RequestBody Props props,
                       @RequestParam("TableMessageName") String TableMessageName) {
        TableMessage tableMessage = this.tableMessageManager.findByTableName(TableMessageName);
        if (tableMessage != null) {
            props.setTableMessage(tableMessage);
            this.propsManager.save(props);
        } else {
            System.out.println(",表为空，不能建立属性在找到之后返回空");
            return false;
        }
        if (this.propsManager.findByPropsName(props.getPrtysName()) != null) {
            return true;
        }
        return false;
    }

    @PostMapping(value = "/addList", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public Boolean add(@RequestBody List<Props> props,
                       @RequestParam("TableMessageName") String TableMessageName) {
        TableMessage tableMessage = this.tableMessageManager.findByTableName(TableMessageName);
        if (tableMessage != null) {
            for (int i = 0; i < props.size(); i++) {
                if (this.propsManager.findByPropsName(props.get(i).getPrtysName()) != null) {
                    System.out.println("在一处返回");
                    return false;
                }
                props.get(i).setTableMessage(tableMessage);
                this.propsManager.save(props.get(i));
            }
        } else {
            System.out.println(",表为空，不能建立属性在找到之后返回空");
            return false;
        }
        return true;
    }

    @ResponseBody
    @PostMapping(value = "/delete")
    public boolean delete(@RequestParam("PropsName") String PropsName) {

        if (this.propsManager.findByPropsName(PropsName) == null) {
            return false;
        } else {
            Long id = this.propsManager.findByPropsName(PropsName).getId();
            this.propsManager.delete(id);
            if ((this.propsManager.findByPropsName(PropsName) == null) == true) {
                return true;
            }
        }
        return false;
    }

    @ResponseBody
    @PostMapping(value = "/updata", consumes = "application/json")
    public boolean updata(@RequestBody Props props,
                          @RequestParam("PropsName") String PropsName) {
        if (this.propsManager.findByPropsName(PropsName) == null) {
            return false;
        } else {
            this.propsManager.updata(props);
            return true;
        }
    }

    @ResponseBody
    @PostMapping(value = "/check/{PropsName}")
    public Props findProps(@PathVariable @RequestParam("PropsName") String PropsName) {
        if (this.propsManager.findByPropsName(PropsName) != null) {
            return this.propsManager.findByPropsName(PropsName);
        }
        return null;
    }
}


