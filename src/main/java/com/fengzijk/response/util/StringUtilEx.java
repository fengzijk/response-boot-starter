package com.fengzijk.response.util;


public class StringUtilEx {
    public static boolean isNumeric(final CharSequence cs) {
        if (isBlank(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static boolean isBlank(final CharSequence cs) {

        if (cs == null){
            return true;
        }

        final int strLen = cs.length();
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
