package zut.cs.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import zut.cs.sys.base.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "Props")
public class Props extends BaseEntity {
    @Column(name = "Props_name", unique = true, nullable = false)//lieming
    private String prtysName;

    @Column(name = "Prtys_Instruct", nullable = false)//shuoming
    private String PrtysInstruct;

    @Column(name = "PropertyType", nullable = false)//shuxingleixing
    private String propertyType;

    @Column(name = "Prtys_IsKey", nullable = false)//zhujian
    private Boolean prtysIsKey;

    @Column(name = "Prtys_isnull", nullable = false)//kekong
    private Boolean PrtysIsnull;

    @Column(name = "Prtys_Long", nullable = false)//zifuchangdu
    private String prtysLong;

    public Boolean getPrtys_IsUnique() {
        return prtys_IsUnique;
    }

    public void setPrtys_IsUnique(Boolean prtys_IsUnique) {
        this.prtys_IsUnique = prtys_IsUnique;
    }

    @Column(name = "Prtys_Isindex", nullable = false)
    private Boolean prtys_Isindex;

    @Column(name = "Prtys_IsUnique", nullable = false)
    private Boolean prtys_IsUnique;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}, optional = true, fetch = FetchType.LAZY)
    private TableMessage tableMessage;

    public String getPrtysName() {
        return prtysName;
    }

    public void setPrtysName(String prtysName) {
        this.prtysName = prtysName;
    }

    public String getPrtysInstruct() {
        return PrtysInstruct;
    }

    public void setPrtysInstruct(String prtysInstruct) {
        PrtysInstruct = prtysInstruct;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Boolean getPrtysIsKey() {
        return prtysIsKey;
    }

    public void setPrtysIsKey(Boolean prtysIsKey) {
        this.prtysIsKey = prtysIsKey;
    }

    public Boolean getPrtysIsnull() {
        return PrtysIsnull;
    }

    public void setPrtysIsnull(Boolean prtysIsnull) {
        PrtysIsnull = prtysIsnull;
    }

    public Boolean getPrtys_Isindex() {
        return prtys_Isindex;
    }

    public void setPrtys_Isindex(Boolean prtys_Isindex) {
        this.prtys_Isindex = prtys_Isindex;
    }

    public String getPrtysLong() {
        return prtysLong;
    }

    public void setPrtysLong(String prtysLong) {
        this.prtysLong = prtysLong;
    }

    @JsonIgnore
    public TableMessage getTableMessage() {
        return tableMessage;
    }

    public void setTableMessage(TableMessage tableMessage) {
        this.tableMessage = tableMessage;
    }
}
