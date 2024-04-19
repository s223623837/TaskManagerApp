package com.example.taskplanner.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    // Utility method to convert a Date object to a formatted string
    public static String formatDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    // Utility method to convert a string to a Date object
    public static Date parseStringToDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Utility method to calculate the difference in days between two dates
    public static long daysBetweenDates(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long differenceMillis = endTime - startTime;
        return differenceMillis / (1000 * 60 * 60 * 24); // Convert milliseconds to days
    }
    public static String convertDateFormat(String dateString) {
        String inputFormat = "EEE MMM dd HH:mm:ss zzz yyyy"; // Input date format
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat, Locale.US);

        try {
            // Parse the input date string into a Date object
            Date date = inputFormatter.parse(dateString);

            // Create a SimpleDateFormat for the desired output format (yyyy-MM-dd)
            SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            // Set the time zone to GMT+11:00 if needed (optional)
            outputFormatter.setTimeZone(TimeZone.getTimeZone("GMT+11:00"));

            // Format the Date object into the desired output format
            return outputFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing exception
            return null; // Return null or an error message as needed
        }
    }
}

