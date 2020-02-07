package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericManager;
import zut.cs.sys.domain.User;

public interface UserManager extends GenericManager<User, Long> {
    User findByUsername(String username);
}
