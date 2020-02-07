package zut.cs.sys.service;

import zut.cs.sys.base.service.GenericManager;
import zut.cs.sys.domain.Props;

public interface PropsManager extends GenericManager<Props, Long> {
    Props findByPropsName(String PropsName);

    Boolean updata(Props props);
}
