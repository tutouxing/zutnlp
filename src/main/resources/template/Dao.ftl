package ${TablePackageNameLoad}.dao.${tablePackageName}.dao;

import zut.cs.sys.base.dao.GenericDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
<#if TableType=="TREE">
import zut.cs.sys.base.dao.GenericTreeDao;
</#if>
import ${TablePackageNameLoad}.dao.${tablePackageName}.domain.${tableName};

/*
<#if tableAuthod??>
    Authod：${tableAuthod}
<#else>
    Authod：NoOne！
</#if>

*/
@Repository
public interface ${tableName}Dao <#if TableType=="ALONE">extends GenericDao<${tableName},Long> <#else >extends GenericTreeDao<${tableName},Long></#if>{
    @Query("select o from ${tableName} o  where o.${tableLowName}Name=?1")
    public ${tableName} findBy${tableName}_name(@Param("${tableLowName}Name") String ${tableLowName}Name);
}