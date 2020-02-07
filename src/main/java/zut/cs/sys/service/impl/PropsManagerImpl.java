package zut.cs.sys.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zut.cs.sys.base.service.impl.GenericManagerImpl;
import zut.cs.sys.dao.PropsDao;
import zut.cs.sys.domain.Props;
import zut.cs.sys.service.PropsManager;

@Service
public class PropsManagerImpl extends GenericManagerImpl<Props, Long> implements PropsManager {

    PropsDao propsDao;

    @Autowired
    public void setPropsDao(PropsDao propsDao) {
        this.propsDao = propsDao;
        this.dao = this.propsDao;
    }

    @Override
    public Props findByPropsName(String PropsName) {
        Props props = this.propsDao.findByProps_name(PropsName);
        return props;
    }

    @Override
    public Boolean updata(Props props) {
        this.propsDao.save(props);
        return true;
    }
}
