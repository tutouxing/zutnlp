package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import zut.cs.sys.base.domain.BaseTreeEntity;

import javax.persistence.*;
import java.util.List;

@Table(name = "TableMessage")
@Entity
@NamedQueries({@NamedQuery(name = "TableMessage.getRoot", query = "select g from TableMessage g where g.parent is null")})
public class TableMessage extends BaseTreeEntity<TableMessage> {
    private static final long serialVersionUID = -1751952224371998469L;

    @Column(name = "Table_Name", unique = true, nullable = false)//biaoming
    private String tablename;

    @Column(name = "Table_Instruction", nullable = false)//shuoming
    private String tableInstruction;

    public Boolean getHave_Connect() {
        return have_Connect;
    }

    public void setHave_Connect(Boolean have_Connect) {
        this.have_Connect = have_Connect;
    }

    @Column(name = "have_Connect", unique = true, nullable = true)//shifouyoufubiao
    private Boolean have_Connect;

    @Column(name = "TableType", nullable = false)//ALONE代表单表或者一对多，TREE代表属性结构
    private String tableType;

    @Column(name = "Authod")
    private String authod;

    @Column(name = "PackageName", nullable = false)//包名
    private String packageName;

    @Column(name = "PackageNameLoad", nullable = false)//包路径
    private String packageNameLoad;

    @Column(name = "ProjectName", nullable = false)//项目名
    private String projectName;

    @Column(name = "ModuleName", nullable = false)//模块名
    private String moduleName;

    @Column(name = "SonNumber", columnDefinition = "INT default 0", nullable = false)
    private Integer sonNumber;

    public Integer getSonNumber() {
        return sonNumber;
    }

    public void setSonNumber(int sonNumber) {
        this.sonNumber = sonNumber;
    }

    public String getAuthod() {
        return authod;
    }

    public void setAuthod(String authod) {
        this.authod = authod;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "tableMessage", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<zut.cs.sys.domain.Props> Props;

    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "tableMessage", cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Connection> connections;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getTableInstruction() {
        return tableInstruction;
    }

    public void setTableInstruction(String tableInstruction) {
        this.tableInstruction = tableInstruction;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public List<zut.cs.sys.domain.Props> getProps() {
        return Props;
    }

    public void setProps(List<zut.cs.sys.domain.Props> props) {
        this.Props = props;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPackageNameLoad() {
        return packageNameLoad;
    }

    public void setPackageNameLoad(String packageNameLoad) {
        this.packageNameLoad = packageNameLoad;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
