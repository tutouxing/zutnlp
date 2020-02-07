package ${TablePackageNameLoad}.${tablePackageName}.impl;

import zut.cs.sys.base.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

import ${TablePackageNameLoad}.dao.${tablePackageName}.dao.${tableName}Dao;
import ${TablePackageNameLoad}.dao.${tablePackageName}.domain.${tableName};
import ${TablePackageNameLoad}.${tablePackageName}.${tableName}Manager;
<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
import ${TablePackageNameLoad}.dao.${tablePackageName}.dao. ${connection.ConnectionSonTableName}Dao;
import ${TablePackageNameLoad}.dao.${tablePackageName}.domain.${connection.ConnectionSonTableName};
    </#if>
</#list>

<#if TableType=="TREE">
import zut.cs.sys.base.service.impl.GenericTreeManagerImpl;
</#if>
/*
<#if tableAuthod??>
    Authod：${tableAuthod}
<#else>
    Authod：NoOne！
</#if>

*/
@Service
public class ${tableName}ManagerImpl  <#if TableType=="ALONE">extends GenericManagerImpl<${tableName}, Long> implements ${tableName}Manager<#else >extends GenericTreeManagerImpl<${tableName}, Long> implements ${tableName}Manager</#if>{

${tableName}Dao ${tableLowName}Dao;
<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
        ${connection.ConnectionSonTableName}Dao ${connection.ConnectionLowSonTableName}Dao;
    @Autowired
    public void set${connection.ConnectionSonTableName}Dao(${connection.ConnectionSonTableName}Dao  ${connection.ConnectionLowSonTableName}Dao) {
        this. ${connection.ConnectionLowSonTableName}Dao =  ${connection.ConnectionLowSonTableName}Dao;
    }
    </#if>
</#list>

    @Autowired
    public void set${tableName}Dao(${tableName}Dao ${tableLowName}Dao) {
        this.${tableLowName}Dao = ${tableLowName}Dao;
        this.dao = this.${tableLowName}Dao;
    }
    @Override
    public ${tableName} findBy${tableName}Name(String ${tableName}Name) {
${tableName} ${tableLowName}=this.${tableLowName}Dao.findBy${tableName}_name(${tableName}Name);
        return ${tableLowName};
    }

    @Override
    public Boolean updata(${tableName} ${tableLowName}) {
        this. ${tableLowName}Dao.save( ${tableLowName});
        return true;
    }

<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
        @Override//删除中间表的一条记录
        public Boolean deleteIntermediateTable${tableName}To${connection.ConnectionSonTableName}(${tableName} ${tableLowName},${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName}) {
        ${tableName} ${tableLowName}1=this.${tableLowName}Dao.findBy${tableName}_name(${tableLowName}.get${tableName}Name());//检查${tableName}已经存在
        if (${tableLowName}1==null){//确定${tableLowName}已经存在
            return false;
        }
        ${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName}1=this.${connection.ConnectionLowSonTableName}Dao.findBy${connection.ConnectionSonTableName}_name(${connection.ConnectionLowSonTableName}.get${connection.ConnectionSonTableName}Name());//检查${connection.ConnectionLowSonTableName}已经存在
        if (${connection.ConnectionLowSonTableName}==null){
            return false;
        }
        List<${connection.ConnectionSonTableName}> ${connection.ConnectionLowSonTableName}s=new ArrayList<>();
        ${connection.ConnectionLowSonTableName}s=${tableLowName}1.get${connection.ConnectionSonTableName}s();
        Boolean isExist=false;
        for(int i=0;i
    <${connection.ConnectionLowSonTableName}s.size();i++){//确定原关系存在
            if(${connection.ConnectionLowSonTableName}.getId()==${connection.ConnectionLowSonTableName}s.get(i).getId()){
                isExist=true;
                break;
            }
        }
        if (isExist==false){
            System.out.println("不存在");
            return false;
        }
        for(int i=0;i
    <${connection.ConnectionLowSonTableName}s.size();i++){//删除已建立关系
            if(${connection.ConnectionLowSonTableName}.getId()==${connection.ConnectionLowSonTableName}s.get(i).getId()){
        ${connection.ConnectionLowSonTableName}s.remove(i);
            }
        }
        ${tableLowName}1.set${connection.ConnectionSonTableName}s(${connection.ConnectionLowSonTableName}s);
        this.${tableLowName}Dao.save(${tableLowName}1);
        return true;
    }


     @Override//添加中间表的一条记录
    public Boolean addIntermediateTable${tableName}To${connection.ConnectionSonTableName}(${tableName} ${tableLowName},${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName}) {
        ${tableName} ${tableLowName}1=this.${tableLowName}Dao.findBy${tableName}_name(${tableLowName}.get${tableName}Name());//检查${tableLowName}已经存在
        if (${tableLowName}1==null){//确定${tableLowName}已经存在
            return false;
        }
        ${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName}1=this.${connection.ConnectionLowSonTableName}Dao.findBy${connection.ConnectionSonTableName}_name(${connection.ConnectionLowSonTableName}.get${connection.ConnectionSonTableName}Name());
        if (${connection.ConnectionLowSonTableName}1==null){
            return false;
        }
        List<${connection.ConnectionSonTableName}> ${connection.ConnectionLowSonTableName}s=new ArrayList<>();
        ${connection.ConnectionLowSonTableName}s=${tableLowName}1.get${connection.ConnectionSonTableName}s();
        for(int i=0;i
    <${connection.ConnectionLowSonTableName}s.size();i++){//确定未建立关系
            if(${connection.ConnectionLowSonTableName}.getId()==${connection.ConnectionLowSonTableName}s.get(i).getId()){
                return false;
            }
        }
        ${connection.ConnectionLowSonTableName}s.add(${connection.ConnectionLowSonTableName});
        ${tableLowName}1.set${connection.ConnectionSonTableName}s(${connection.ConnectionLowSonTableName}s);
        this.${tableLowName}Dao.save(${tableLowName}1);
        return true;
    }
    </#if>
</#list>
}