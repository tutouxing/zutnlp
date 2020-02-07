package ${TablePackageNameLoad}.${tablePackageName};

import zut.cs.sys.base.service.GenericManager;
<#if TableType=="TREE">
import zut.cs.sys.base.service.GenericTreeManager;
</#if>
<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
import ${TablePackageNameLoad}.dao.${tablePackageName}.domain.${connection.ConnectionSonTableName};
    </#if>
</#list>


import ${TablePackageNameLoad}.dao.${tablePackageName}.domain.${tableName};

/*
<#if tableAuthod??>
    Authod：${tableAuthod}
<#else>
    Authod：NoOne！
</#if>

*/

public interface ${tableName}Manager <#if TableType=="ALONE">extends GenericManager<${tableName}, Long> <#else >extends GenericTreeManager<${tableName},Long></#if>{
${tableName} findBy${tableName}Name(String ${tableName}Name);
    Boolean updata(${tableName} ${tableLowName});
<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
    Boolean deleteIntermediateTable${tableName}To${connection.ConnectionSonTableName}(${tableName} ${tableLowName},${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName});
    Boolean addIntermediateTable${tableName}To${connection.ConnectionSonTableName}(${tableName} ${tableLowName},${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName});
    </#if>
</#list>
}