package br.com.catapan.testbitcoin.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {

    public static DecimalFormat getDecimalFormat(){
        return new DecimalFormat(".##");
    }

    public static SimpleDateFormat getSimpleDateFormat(){
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }


    public static int getLast15Days() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        c.add(Calendar.DAY_OF_YEAR, -15);
        return Integer.parseInt(sdf.format(c.getTime()));
    }

}
