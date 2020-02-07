package zut.cs.sys.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zut.cs.sys.base.dao.GenericDao;
import zut.cs.sys.domain.Props;

@Repository
public interface PropsDao extends GenericDao<Props, Long> {
    @Query("select o from Props o  where o.prtysName=?1")
    public Props findByProps_name(@Param("prtysName") String prtysName);

}
