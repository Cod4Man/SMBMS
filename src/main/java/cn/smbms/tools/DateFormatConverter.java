package cn.smbms.tools;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 张鸿杰
 * Date：2019-03-30
 * Time:13:20
 */
public class DateFormatConverter implements Converter<String, Date> {
    private String datePattern;

    public DateFormatConverter(String datePattern) {
        this.datePattern = datePattern;
    }
    @Override
    public Date convert(String dateString) {
        System.out.println("===================DateFormatConverter convert===========");
        Date date = null;
        try {
            date = new SimpleDateFormat(datePattern).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
