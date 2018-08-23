package com.gamerequirements.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Created by v3rt1ag0 on 7/23/18.
 */

public class DateTimeUtil
{
    public static String formatToYesterdayOrToday(String date) {
        //date = "2018-07-17T11:15:23";
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").parseDateTime(date);
        DateTime today = new DateTime();
        DateTime yesterday = today.minusDays(1);
       // DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("hh:mma");
        String daySuffix = getLastDigitSufix(dateTime.getDayOfMonth());
        //DateTimeFormatter timeFormatter2 = DateTimeFormat.forPattern("dd'"+daySuffix+" '"+"MMMM");
        DateTimeFormatter timeFormatter2 = DateTimeFormat.forPattern("dd'"+daySuffix+" '"+"MMMM");

        if (dateTime.toLocalDate().equals(today.toLocalDate())) {
            return "Today "; //+ timeFormatter.print(dateTime);
        } else if (dateTime.toLocalDate().equals(yesterday.toLocalDate())) {
            return "Yesterday " ;//+ timeFormatter.print(dateTime);
        } else {
            return timeFormatter2.print(dateTime);
        }
    }

    public static String getLastDigitSufix(int number) {
        switch( (number<20) ? number : number%10 ) {
            case 1 : return "st";
            case 2 : return "nd";
            case 3 : return "rd";
            default : return "th";
        }
    }


    /*
    * Symbol  Meaning                      Presentation  Examples
------  -------                      ------------  -------
 G       era                          text          AD
 C       century of era (>=0)         number        20
 Y       year of era (>=0)            year          1996

 x       weekyear                     year          1996
 w       week of weekyear             number        27
 e       day of week                  number        2
 E       day of week                  text          Tuesday; Tue

 y       year                         year          1996
 D       day of year                  number        189
 M       month of year                month         July; Jul; 07
 d       day of month                 number        10

 a       halfday of day               text          PM
 K       hour of halfday (0~11)       number        0
 h       clockhour of halfday (1~12)  number        12

 H       hour of day (0~23)           number        0
 k       clockhour of day (1~24)      number        24
 m       minute of hour               number        30
 s       second of minute             number        55
 S       fraction of second           number        978

 z       time zone                    text          Pacific Standard Time; PST
 Z       time zone offset/id          zone          -0800; -08:00; America/Los_Angeles

 '       escape for text              delimiter
 ''      single quote                 literal       '

Number: The minimum number of digits. Shorter numbers are zero-padded to this amount. Thus, "HH" might output "09" whereas "H" might output "9" (for the hour-of-day of 9 in the morning).

Year: Numeric presentation for year and weekyear fields are handled specially. For example, if the count of 'y' is 2, the year will be displayed as the zero-based year of the century, which is two digits.

Month: 3 or over, use text, otherwise use number. Thus, "MM" might output "03" whereas "MMM" might output "Mar" (the short form of March) and "MMMM" might output "March".

 */
}
