package zut.cs.sys.service;

import java.util.HashMap;

public interface GenMessageManage {
    //HashMap<String, Object> getPropsList(String tableName);
    void genCodeAll(String tableName, String choosePath);

    void genCodeEntity(String tableName, String choosePath);

    //     void getPath(String tablename);
//void GenCodeZip(String tableName);
    HashMap<String, Object> getPropsList(String tableName);

    HashMap<String, String> getPath(String tableName, String choosePath);
}
