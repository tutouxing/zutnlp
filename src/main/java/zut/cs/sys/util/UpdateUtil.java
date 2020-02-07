package zut.cs.sys.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UpdateUtil {
    private static List<Method> methodList = new ArrayList<>();

    //    利用反射动态过滤出值不为null的参数 obj:传入的body，obj2:旧的body
    public static Object get(Object obj, Object obj2) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get") && method.invoke(obj, null) != null) {
                methodList.add(method);
            }
        }
        for (Method method : methodList) {
            //得到每个参数不为null的结果
            Object o = method.invoke(obj, null);
            for (Method method1 : methods) {
                //  利用反射调用set方法局部更新结果
                if (method1.getName().equals("set" + method.getName().substring(3))) {
                    method1.invoke(obj2, o);
                }
            }
        }
        return obj2;
    }
}
