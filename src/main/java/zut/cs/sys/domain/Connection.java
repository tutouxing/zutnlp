package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.*;
/*
    Authod：NoOne！

*/

@Table(name = "Connection")
@Entity
public class Connection extends BaseEntity {
    private static final long serialVersionUID = -1751952224371998469L;

    @Column(name = "ConnectionName", nullable = false, unique = true)
    private String connectionName;

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getConnectionName() {
        return connectionName;
    }

    @Column(name = "TableConnectionInstruction", nullable = false, unique = false, length = 1000)
    private String tableConnectionInstruction;

    //false是多端

    public Boolean getTableConnectionPropsOneOrTwo() {
        return tableConnectionPropsOneOrTwo;
    }

    public void setTableConnectionPropsOneOrTwo(Boolean tableConnectionPropsOneOrTwo) {
        this.tableConnectionPropsOneOrTwo = tableConnectionPropsOneOrTwo;
    }

    @Column(name = "TableConnectionPropsOneOrTwo", nullable = false, unique = false, length = 1000)
    private Boolean tableConnectionPropsOneOrTwo;

    @Column(name = "TableConnectionPropsCaseCadeType", unique = false, length = 1000)
    private String tableConnectionPropsCaseCadeType;

    @Column(name = "TableConnectionParentTableName", nullable = true, unique = false, length = 1000)
    private String tableConnectionParentTableName;

    @Column(name = "TableConnectionSonTableName", nullable = true, unique = false, length = 1000)
    private String tableConnectionSonTableName;

    @Column(name = "TableConnectionType", nullable = false, unique = false, length = 1000)
    private String tableConnectionType;//ONE_TO_MANY,//MANY_TO_ONE,//ONE_TO_ONE,//MANY_TO_MANY

    public String getTableConnectionType() {
        return tableConnectionType;
    }

    public void setTableConnectionType(String tableConnectionType) {
        this.tableConnectionType = tableConnectionType;
    }

    @JsonIgnore
    public TableMessage getTableMessage() {
        return tableMessage;
    }

    public void setTableMessage(TableMessage tableMessage) {
        this.tableMessage = tableMessage;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}, optional = true, fetch = FetchType.LAZY)
    private TableMessage tableMessage;

    public void setTableConnectionInstruction(String tableConnectionInstruction) {
        this.tableConnectionInstruction = tableConnectionInstruction;
    }

    public String getTableConnectionInstruction() {
        return tableConnectionInstruction;
    }


//
//     public void setTableConnectionPropsOneOrTwo ( Boolean tableConnectionPropsOneOrTwo) { this.tableConnectionPropsOneOrTwo = tableConnectionPropsOneOrTwo; }
//     public Boolean getTableConnectionPropsOneOrTwo() {return tableConnectionPropsOneOrTwo;}


    public void setTableConnectionPropsCaseCadeType(String tableConnectionPropsCaseCadeType) {
        this.tableConnectionPropsCaseCadeType = tableConnectionPropsCaseCadeType;
    }

    public String getTableConnectionPropsCaseCadeType() {
        return tableConnectionPropsCaseCadeType;
    }


    public void setTableConnectionParentTableName(String tableConnectionParentTableName) {
        this.tableConnectionParentTableName = tableConnectionParentTableName;
    }

    public String getTableConnectionParentTableName() {
        return tableConnectionParentTableName;
    }


    public void setTableConnectionSonTableName(String tableConnectionSonTableName) {
        this.tableConnectionSonTableName = tableConnectionSonTableName;
    }

    public String getTableConnectionSonTableName() {
        return tableConnectionSonTableName;
    }


}