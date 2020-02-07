package zut.cs.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zut.cs.sys.base.service.impl.GenericManagerImpl;
import zut.cs.sys.dao.ComponentDao;
import zut.cs.sys.domain.Component;
import zut.cs.sys.domain.User;
import zut.cs.sys.service.ComponentManager;

import java.util.Set;

@Service
public class ComponentManagerImpl extends GenericManagerImpl<Component, Long> implements ComponentManager {
    ComponentDao componentDao;

    @Autowired
    public void setComponentDao(ComponentDao componentDao) {
        this.componentDao = componentDao;
        this.dao = this.componentDao;
    }

    @Override
    public Set<Component> findByUser(User user) {
        return componentDao.findByUser(user);
    }
}
