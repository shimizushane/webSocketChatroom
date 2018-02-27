package com.example.apple.mysocket1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 2018/2/7.
 */

public class removeTag {
    public static String remove(String str)
    {
        Pattern p_style, p_html;
        Matcher m_style, m_html;

        //style
        /*
        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        m_style = p_style.matcher(str);
        str = m_style.replaceAll("");
        */
        //HTML
        String regEx_html = "<[^>]+>";
        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        m_html = p_html.matcher(str);
        str = m_html.replaceAll("");

        return  str;
    }
}
