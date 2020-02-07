package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericManager;
import zut.cs.sys.domain.Component;
import zut.cs.sys.domain.User;

import java.util.Set;

public interface ComponentManager extends GenericManager<Component, Long> {
    Set<Component> findByUser(User user);
}
