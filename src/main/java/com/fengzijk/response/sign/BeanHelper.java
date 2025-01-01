/*
 *   All rights Reserved, Designed By ZTE-ITS
 *   Copyright:    Copyright(C) 2019-2025
 *   Company       FENGZIJK LTD.
 *   @Author:    fengzijk
 *   @Email: guozhifengvip@gmail.com
 *   @Version    V1.0
 *   date:   2022年08月28日 03时33分
 *   Modification       History:
 *   ------------------------------------------------------------------------------------
 *   Date                  Author        Version        Description
 *   -----------------------------------------------------------------------------------
 *  2022-08-28 03:33:35    fengzijk         1.0         Why & What is modified: <修改原因描述>
 *
 *
 */

package com.fengzijk.response.sign;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 *
 * @author guozhifeng
 * @since 2022/8/28
 */

public class BeanHelper {


    /**
     * 过滤不需要属性
     *
     * @param field 字段
     * @return java.lang.Boolean
     */
    private static Boolean needFilterField(Field field) {
        // 过滤静态属性
        if (Modifier.isStatic(field.getModifiers())) {
            return true;
        }
        // 过滤transient 关键字修饰的属性
        return Modifier.isTransient(field.getModifiers());

    }


    /**
     * 获取签名类型
     *
     * @param bean 对象
     * @return java.lang.String
     */
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


    /**
     * 获取值
     *
     * @param bean   对象
     * @param objKey 值
     * @return java.lang.String
     */
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

    /**
     * 利用Java反射根据类的名称获取属性信息和父类的属性信息
     *
     * @param obj 对象
     * @return List
     */
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


    /**
     * 递归所有父类属性
     *
     * @param clazz     类
     * @param fieldList 字段集合
     */
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
