package zut.cs.sys.util;

public class FileNameUtils {
    /**
     * @Description: java类作用描述
     * @Author: wastelands
     * @CreateDate: 2019/5/21$ 15:08$
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     *
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName) {
        return UUIDUtils.getUUID() + FileNameUtils.getSuffix(fileOriginName);
    }
}
