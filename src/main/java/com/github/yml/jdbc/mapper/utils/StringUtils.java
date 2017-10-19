package com.github.yml.jdbc.mapper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yml on 2017/9/5.
 */
public class StringUtils {

    public static String getLineInfo(){
        StackTraceElement ste=new Throwable().getStackTrace()[1];
        return ste.getFileName()+": Line "+ste.getLineNumber();
    }

    public static boolean isEmpty(String str){
        if(str==null || str.isEmpty()){
            return true;
        }
        return false;
    }
    public static boolean equalsNoEmpty(String str1,String str2){
        if(isEmpty(str1) || isEmpty(str2)){
            return false;
        }
        return str1.trim().equals(str2.trim());
    }

    /**
     * 下划线转驼峰
     * @param line 源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰,首字母是否小写
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line,boolean smallCamel){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
    /**
     * 驼峰转下划线,转换后全部大写
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end()==line.length()?"":"_");
        }
        return sb.toString();
    }
    /**
     * 驼峰转下划线,转换后全部小写
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2UnderlineForLowerCase(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end()==line.length()?"":"_");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String line="id";
        String camel=underline2Camel(line,true);
        System.out.println(camel);
        camel=underline2Camel(line,false);
        System.out.println(camel);
        System.out.println(camel2Underline(camel));
    }


}
