package ${TablePackageNameLoad}.web.${tablePackageName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import zut.cs.sys.base.rest.GenericController;
import ${TablePackageNameLoad}.dao.${tablePackageName}.domain.${tableName};
import ${TablePackageNameLoad}.${tablePackageName}.${tableName}Manager;
import ${TablePackageNameLoad}.dao.${tablePackageName}.dao.${tableName}Dao;
<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
import ${TablePackageNameLoad}.dao.${tablePackageName}.domain.${connection.ConnectionSonTableName};
import ${TablePackageNameLoad}.${tablePackageName}.${connection.ConnectionSonTableName}Manager;
    </#if>
</#list>

<#if TableType=="TREE">
import zut.cs.sys.rest.GenericTreeController;
</#if>

<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionPropsOneOrTwo==false>
    import ${TablePackageNameLoad}.dao.${tablePackageName}.domain.${connection.ConnectionParentTableName};
    import ${TablePackageNameLoad}.${tablePackageName}.${connection.ConnectionParentTableName}Manager;
    </#if>
</#list>


/*
<#if tableAuthod??>
    Authod：${tableAuthod}
<#else>
    Authod：NoOne！
</#if>

*/

@Controller
@RequestMapping("/${tableName}")
public class ${tableName}Controller <#if TableType=="ALONE">extends GenericController<${tableName},Long,${tableName}Manager> <#else >extends GenericTreeController<${tableName}, Long, ${tableName}Manager></#if>{
${tableName}Manager ${tableLowName}Manager;

    @Autowired
    public void set${tableName}Manager(${tableName}Manager ${tableLowName}Manager) {
        this.${tableLowName}Manager = ${tableLowName}Manager;
        this.manager = this.${tableLowName}Manager;
    }
//ManyToMany
<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
        ${connection.ConnectionSonTableName}Manager  ${connection.ConnectionLowSonTableName}Manager;
        @Autowired
        public void set${connection.ConnectionSonTableName}Manager(${connection.ConnectionSonTableName}Manager  ${connection.ConnectionLowSonTableName}Manager) {
            this. ${connection.ConnectionLowSonTableName}Manager =  ${connection.ConnectionLowSonTableName}Manager;
        }
    </#if>
</#list>
<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionPropsOneOrTwo==false>
    @Autowired
        ${connection.ConnectionParentTableName}Manager ${connection.ConnectionLowParentTableName}Manager;
    </#if>
</#list>

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<${tableName}> list(){
    List<${tableName}> ${tableLowName}List=new ArrayList<${tableName}>();
${tableLowName}List=this.${tableLowName}Manager.findAll();
        return ${tableLowName}List;
        }

    @PostMapping(value = "/add",consumes = "application/json;charset=UTF-8",produces = {"application/json"})
    @ResponseBody
    public Boolean add(@RequestBody ${tableName}  ${tableLowName}
            ){
            if(this.${tableLowName}Manager.findBy${tableName}Name(${tableLowName}.get${tableName}Name())!=null) {
                return false;
            }
<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionPropsOneOrTwo==false&&connection.ConnectionParentTableName??==false>
        ${connection.ConnectionParentTableName} ${connection.ConnectionLowParentTableName}=this.${connection.ConnectionLowParentTableName}Manager.findBy${connection.ConnectionParentTableName}Name(${connection.ConnectionParentTableName}Name);
                    if(${connection.ConnectionLowParentTableName}==null){
                        return false;
                    }
                    else{
        ${tableLowName}.set${connection.ConnectionParentTableName}(${connection.ConnectionLowParentTableName});
                    }
    </#if>
</#list>
            this.${tableLowName}Manager.save(${tableLowName});
            if(this.${tableLowName}Manager.findBy${tableName}Name(${tableLowName}.get${tableName}Name())!=null) {
                return true;
            }
        return false;
    }


    @ResponseBody
    @PostMapping(value = "/updata",consumes = "application/json")
    public boolean updata(@RequestBody ${tableName} ${tableLowName},
                             @RequestParam("${tableName}Name") String ${tableName}Name){
      if(this.${tableLowName}Manager.findBy${tableName}Name(${tableName}Name)==null)
        {
            return false;
        }else {
            this.${tableLowName}Manager.updata(${tableLowName});
            return true;
        }
    }

    @ResponseBody
    @PostMapping(value = "/check")
    public ${tableName} find${tableName}( @RequestParam("${tableName}Name")  String ${tableName}Name){
        if(this.${tableLowName}Manager.findBy${tableName}Name(${tableName}Name)!=null){
            return this.${tableLowName}Manager.findBy${tableName}Name(${tableName}Name);
        }
        return null;
    }

    @ResponseBody
    @PostMapping(value = "/delete")
    public boolean delete(@PathVariable @RequestParam("${tableName}Name") String ${tableName}Name){
          if(this.${tableLowName}Manager.findBy${tableName}Name(${tableName}Name)==null){
            return false;
        }
        else {
            Long id=this.${tableLowName}Manager.findBy${tableName}Name(${tableName}Name).getId();
            this.${tableLowName}Manager.delete(id);
            if((this.${tableLowName}Manager.findBy${tableName}Name(${tableName}Name)==null)==true){
                return true;
            }
        }
        return false;
    }

    @ResponseBody
    @PostMapping(value = "/updataById",consumes = "application/json")
    public boolean updataById(@RequestBody ${tableName} ${tableLowName},
                          @RequestParam("Id") Long id){
        if(this.${tableLowName}Manager.findById(id)==null)
        {
            return false;
        }else {
            this.${tableLowName}Manager.updata(${tableLowName});
            return true;
        }
    }

    @ResponseBody
    @PostMapping(value = "/deleteById")
    public boolean deletebyId(@PathVariable @RequestParam("Id") Long id){
        if(this.${tableLowName}Manager.findById(id)==null){
            return false;
        }else {
            Long getedId=this.${tableLowName}Manager.findById(id).getId();
            this.${tableLowName}Manager.delete(getedId);
            return true;
        }
    }



<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
        @ResponseBody//更新中间表的关系
        @PostMapping(value = "/updataIntermediateTable${tableName}To${connection.ConnectionSonTableName}")
        public boolean updataIntermediateTable${tableName}To${connection.ConnectionSonTableName}(@RequestParam("${tableName}Name") String ${tableLowName}Name, @RequestParam("${connection.ConnectionSonTableName}OldName") String ${connection.ConnectionLowSonTableName}OldName,@RequestBody ${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName}New){
        ${tableName} ${tableLowName}=this.${tableLowName}Manager.findBy${tableName}Name(${tableLowName}Name);
        if (${tableLowName}==null){
            return false;
        }
        ${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName}=this.${connection.ConnectionLowSonTableName}Manager.findBy${connection.ConnectionSonTableName}Name(${connection.ConnectionLowSonTableName}OldName);
        if (${connection.ConnectionLowSonTableName}==null){
            return false;
        }
        if (this.${tableLowName}Manager.deleteIntermediateTable${tableName}To${connection.ConnectionSonTableName}(student,${connection.ConnectionLowSonTableName})!=true){
            return false;
        }else {
            if (this.${tableLowName}Manager.addIntermediateTable${tableName}To${connection.ConnectionSonTableName}(${tableLowName},${connection.ConnectionLowSonTableName}New)!=true){
                return false;
            }
        }
        return true;
    }

        @ResponseBody//删除中间表的关系
        @PostMapping(value = "/deleteIntermediateTable${tableName}To${connection.ConnectionSonTableName}")
        public Boolean deleteIntermediateTable${tableName}To${connection.ConnectionSonTableName}(@RequestParam("${tableName}Name") String ${tableLowName}Name, @RequestParam("${connection.ConnectionSonTableName}Name") String ${connection.ConnectionLowSonTableName}Name){
        ${tableName} ${tableLowName}=this.${tableLowName}Manager.findBy${tableName}Name(${tableLowName}Name);
            if (${tableLowName}==null){
                return false;
            }
        ${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName}=this.${connection.ConnectionLowSonTableName}Manager.findBy${connection.ConnectionSonTableName}Name(${connection.ConnectionLowSonTableName}Name);
            if (${connection.ConnectionLowSonTableName}==null){
                return false;
            }
            if (this.${tableLowName}Manager.deleteIntermediateTable${tableName}To${connection.ConnectionSonTableName}(${tableLowName},${connection.ConnectionLowSonTableName})!=true){
                return false;
            }else return true;
    }

    @ResponseBody//添加中间表的关系
    @PostMapping(value = "/addIntermediateTable${tableName}To${connection.ConnectionSonTableName}")
    public Boolean addIntermediateTable${tableName}To${connection.ConnectionSonTableName}(@RequestParam("${tableName}Name") String ${tableLowName}Name,
                                                        @RequestParam("${connection.ConnectionSonTableName}Name") String ${connection.ConnectionLowSonTableName}Name){
        ${tableName} ${tableLowName}=this.${tableLowName}Manager.findBy${tableName}Name(${tableLowName}Name);
            if (${tableLowName}==null){
                return false;
            }
        ${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName}=this.${connection.ConnectionLowSonTableName}Manager.findBy${connection.ConnectionSonTableName}Name(${connection.ConnectionLowSonTableName}Name);
            if (${connection.ConnectionLowSonTableName}==null){
                return false;
            }
            if (this.${tableLowName}Manager.addIntermediateTable${tableName}To${connection.ConnectionSonTableName}(${tableLowName},${connection.ConnectionLowSonTableName})!=true){
                return false;
            }else return true;
    }
    </#if>
</#list>
}

