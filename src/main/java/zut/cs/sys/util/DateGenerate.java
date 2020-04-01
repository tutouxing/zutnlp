package zut.cs.sys.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateGenerate {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/4/2$ 2:25$

     */
    public static String getDate(){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS" );
        Date d= new Date();
        String str = sdf.format(d);
        return str;
    }
}
