package test;

import java.util.Date;

/**
 * Created by pc on 08/09/2016.
 */
public class T {
    public static void main(String args[]){
        Date date= new Date();
        System.out.println("time: "+date.getTime());
        System.out.println("day: "+date.getDate());
        System.out.println("month: "+date.getMonth());
        System.out.println("hours: "+date.getHours());
    }
}
