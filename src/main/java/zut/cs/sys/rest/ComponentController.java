package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zut.cs.sys.base.rest.GenericController;
import zut.cs.sys.domain.Component;
import zut.cs.sys.service.ComponentManager;

@RestController
@RequestMapping("component/")
@Api(tags = "组件接口")
public class ComponentController extends GenericController<Component, Long, ComponentManager> {
    ComponentManager componentManager;

    @Autowired
    public void setComponentManager(ComponentManager componentManager) {
        this.componentManager = componentManager;
        this.manager = this.componentManager;
    }
}
