package zut.cs.sys.util;

import java.util.UUID;

public class UUIDUtils {
    /**
     * @Description: java类作用描述
     * @Author: wastelands
     * @CreateDate: 2019/5/21$ 15:08$
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
