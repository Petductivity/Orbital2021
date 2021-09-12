package com.example.petductivity.planner;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A class containing various methods and fields used by the Planner activity.
 *
 * @author Team Petductivity
 */
public class PlannerUtils {

    /**
     *  The date selected by the user.
     */
    public static LocalDate selectedDate;

    /**
     * Formats the date given to a standard form.
     *
     * @param date The date to be formatted.
     * @return A string containing the formatted date.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String dateFormat(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");

        return date.format(formatter);
    }

    /**
     * Formats the given date into a month and year format.
     *
     * @param date The date to be formatted.
     * @return A string containing the formatted date.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String monthYearDateFormat(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");

        return date.format(formatter);
    }

    /**
     * Sets up the monthly calendar.
     *
     * @param date The selected date.
     * @return A calendar list.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<LocalDate> setUpMonthArray(LocalDate date) {
        ArrayList<LocalDate> array = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int days = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = PlannerUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > days + dayOfWeek)
                array.add(null);
            else
                array.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }

        return array;
    }

    /**
     * Sets up the weekly calendar.
     *
     * @param selectedDate The selected date.
     * @return A calendar list.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<LocalDate> setUpWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> array = new ArrayList<>();
        LocalDate current = getCurrentDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate)) {
            array.add(current);
            current = current.plusDays(1);
        }

        return array;
    }

    /**
     * Retrieves the current date.
     *
     * @param current The selected date
     * @return A LocalDate object describing the current date.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDate getCurrentDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }
}
