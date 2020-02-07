package ${TablePackageNameLoad}.dao.${tablePackageName}.domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import zut.cs.sys.base.domain.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import com.fasterxml.jackson.annotation.JsonIgnore;


<#if TableType=="TREE">
import zut.cs.sys.base.domain.BaseTreeEntity;
</#if>

import javax.persistence.*;
import java.util.List;

import java.math.BigDecimal;
/*
<#if tableAuthod??>
    Authod：${tableAuthod}
<#else>
    Authod：NoOne！~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
</#if>

*/

@Table(name = "${tableName}")
@Entity
<#if  TableType=="TREE">
@NamedQueries({@NamedQuery(name = "${tableName}.getRoot", query = "select g from ${tableName} g where g.parent is null")})
</#if>
public class ${tableName} <#if TableType=="ALONE">extends BaseEntity <#else > extends BaseTreeEntity<${tableName}></#if>{
     private static final long serialVersionUID = -1751952224371998469L;

          @Column(name = "${tableName}Name",nullable=false,unique=true)
          private String ${tableLowName}Name;

          public void set${tableName}Name(String ${tableLowName}Name ){
            this.${tableLowName}Name=${tableLowName}Name;
          }
         public String get${tableName}Name(){
            return ${tableLowName}Name;
         }

<#list propsMapList as prop>
          @Column(  name = "${prop.propsName}" ,nullable=${prop.propsIsNull?c},unique = ${prop.propsIsUnique?c},length = ${prop.propsLong})
          private ${prop.propsType} ${prop.propsLowName};

</#list>
<#list propsMapList as prop>

     public void set${prop.propsName} ( ${prop.propsType} ${prop.propsLowName}) { this.${prop.propsLowName} = ${prop.propsLowName}; }
     public ${prop.propsType} get${prop.propsName}() {return ${prop.propsLowName};}
</#list>

<#list ConnectionMapList as connection>
    <#if connection.ConnectionTableConnectionType=="OneToMany">
        @Fetch(FetchMode.SELECT)
        @OneToMany(mappedBy = "${tableLowName}", <#if connection.ConnectionPropsCaseCadeType??> cascade ={${connection.ConnectionPropsCaseCadeType}}, </#if>  fetch = FetchType.EAGER)
        @JsonIgnore
        private List<${connection.ConnectionSonTableName}> ${connection.ConnectionLowSonTableName}s;

        public List<${connection.ConnectionSonTableName}> get${connection.ConnectionSonTableName}s() {
            return ${connection.ConnectionLowSonTableName}s;
        }

        public void set${connection.ConnectionSonTableName}s(List<${connection.ConnectionSonTableName}> ${connection.ConnectionLowSonTableName}s) {
            this.${connection.ConnectionLowSonTableName}s = ${connection.ConnectionLowSonTableName}s;
        }
    </#if>
    <#if connection.ConnectionTableConnectionType=="ManyToOne">
        @ManyToOne(<#if connection.ConnectionPropsCaseCadeType??> cascade ={${connection.ConnectionPropsCaseCadeType}},</#if> optional = true,fetch =FetchType.LAZY)
        private ${connection.ConnectionParentTableName} ${connection.ConnectionLowParentTableName};

        @JsonIgnore
        public ${connection.ConnectionParentTableName} get${connection.ConnectionParentTableName}(){
            return ${connection.ConnectionLowParentTableName};
        }

        public void set${connection.ConnectionParentTableName}(${connection.ConnectionParentTableName} ${connection.ConnectionLowParentTableName}) {
            this.${connection.ConnectionLowParentTableName} = ${connection.ConnectionLowParentTableName};
        }
    </#if>
    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
    <#--执行了父是维护端，子是被维护端，需要加如果是persist，
    <#--manytomany的单表的被维护端，如果加关系，是在维护段新建的关系，然后呢就会是
    <#--@JoinTable，而没有mappedBy，然后如果建立父关系，就一定会有为1
    -----------主控方-->
        @ManyToMany(fetch = FetchType.EAGER,<#if connection.ConnectionPropsCaseCadeType??> cascade ={${connection.ConnectionPropsCaseCadeType}} </#if>)
        @JoinTable()
        @JsonIgnore
        private List<${connection.ConnectionSonTableName}> ${connection.ConnectionLowSonTableName}s;

        public List<${connection.ConnectionSonTableName}> get${connection.ConnectionSonTableName}s() { return ${connection.ConnectionLowSonTableName}s; }

        public void set${connection.ConnectionSonTableName}s(List<${connection.ConnectionSonTableName}> ${connection.ConnectionLowSonTableName}s) { this.${connection.ConnectionLowSonTableName}s = ${connection.ConnectionLowSonTableName}s; }
    </#if>

    <#if connection.ConnectionTableConnectionType=="ManyToMany"&&connection.ConnectionTableConnectionPropsOneOrTwo==false>
            @ManyToMany(fetch = FetchType.EAGER,<#--<#if connection.ConnectionPropsCaseCadeType??> cascade ={${connection.ConnectionPropsCaseCadeType}},</#if>-->mappedBy="${tableLowName}s")
            @JsonIgnore
            private List<${connection.ConnectionParentTableName}> ${connection.ConnectionLowParentTableName}s;
            public List<${connection.ConnectionParentTableName}> get${connection.ConnectionParentTableName}s() {return ${connection.ConnectionLowParentTableName}s;}
            public void set${connection.ConnectionParentTableName}s(List<${connection.ConnectionParentTableName}> ${connection.ConnectionLowParentTableName}s) { this.${connection.ConnectionLowParentTableName}s = ${connection.ConnectionLowParentTableName}s; }
    </#if>

    <#if connection.ConnectionTableConnectionType=="OneToOne"&&connection.ConnectionTableConnectionPropsOneOrTwo==true>
        //执行了子one
        @OneToOne(<#if connection.ConnectionPropsCaseCadeType??> cascade ={${connection.ConnectionPropsCaseCadeType}}</#if>)
        private ${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName};

        public ${connection.ConnectionSonTableName} get${connection.ConnectionSonTableName}(){
            return ${connection.ConnectionLowSonTableName};
        }

        public void set${connection.ConnectionSonTableName}(${connection.ConnectionSonTableName} ${connection.ConnectionLowSonTableName}) {
            this.${connection.ConnectionLowSonTableName} = ${connection.ConnectionLowSonTableName};
        }
    </#if>

    <#if connection.ConnectionTableConnectionType=="OneToOne"&&connection.ConnectionTableConnectionPropsOneOrTwo==false>
        //执行了父one
        @OneToOne(<#if connection.ConnectionPropsCaseCadeType??> cascade ={${connection.ConnectionPropsCaseCadeType}} </#if>)
        private ${connection.ConnectionParentTableName} ${connection.ConnectionLowParentTableName};

        public ${connection.ConnectionParentTableName} get${connection.ConnectionParentTableName}(){
            return ${connection.ConnectionLowParentTableName};
        }

        public void set${connection.ConnectionParentTableName}(${connection.ConnectionParentTableName} ${connection.ConnectionLowParentTableName}) {
            this.${connection.ConnectionLowParentTableName} = ${connection.ConnectionLowParentTableName};
        }
    </#if>
</#list>



}