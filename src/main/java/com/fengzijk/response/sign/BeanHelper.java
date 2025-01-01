

package com.fengzijk.response.sign;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class BeanHelper {


    
    private static Boolean needFilterField(Field field) {
        // 过滤静态属性
        if (Modifier.isStatic(field.getModifiers())) {
            return true;
        }
        // 过滤transient 关键字修饰的属性
        return Modifier.isTransient(field.getModifiers());

    }


    
    public static String getSignType(Object bean) {
        if (bean != null) {
            //获取所有的字段包括public,private,protected,private
            List<Field> fields = getFieldList(bean);
            /// Field[] fields = bean.getClass().getDeclaredFields();
            for (Field f : fields) {
                //获取字段名
                String key = f.getName();
                f.setAccessible(true);
                Object value = null;
                try {
                    value = f.get(bean);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (value == null) {
                    continue;
                }
                if (SignConstant.SIGN_SIGN_TYPE_KEY.equals(key)) {
                    return value.toString();
                }


            }

        }
        return "";
    }


    
    public static String getValue(Object bean, String objKey) {
        List<Field> fields = getFieldList(bean);

        //获取所有的字段包括public,private,protected,private
        /// Field[] fields = ObjectUtils.getFieldList(bean);

        for (Field f : fields) {
            //获取字段名
            String key = f.getName();
            f.setAccessible(true);
            Object value = null;
            try {
                value = f.get(bean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (value != null && objKey.equals(key)) {
                return value.toString();
            }
        }
        return "";
    }

    
    public static List<Field> getFieldList(Object obj) {
        List<Field> fieldList = new LinkedList<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (needFilterField(field)) {
                continue;
            }
            fieldList.add(field);
        }
        if (!(obj instanceof Map)) {
            getParentField(clazz, fieldList);
        }
        return fieldList;
    }


    
    private static void getParentField(Class<?> clazz, List<Field> fieldList) {
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            Field[] superFields = superClazz.getDeclaredFields();
            for (Field field : superFields) {
                if (needFilterField(field)) {
                    continue;
                }
                fieldList.add(field);
            }
            if (!clazz.isInstance(Map.class)) {
                getParentField(superClazz, fieldList);
            }
        }
    }
}
