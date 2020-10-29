package com.example.saierclient.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MyUtils {
    public static String convertDate(Date publishDate) {
        // TODO Auto-generated method stub
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return sdf.format(publishDate);
    }
}
