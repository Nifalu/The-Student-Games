package gameLogic;

/**
 * Every Game starts in Sep 2021 and each Round takes 1 month or 6 weeks
 * (6 weeks due to the expected value of 3.5 of the dice and 30 playing fields per year).
 * Will be used for the high score
 */

public class Calendar {

    int year;
    int month;
    int day;

    /**
     * Sets a new calendar
     * @param year year of the start
     * @param month month of the start
     * @param day day of the start
     */
    public Calendar(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Updates the calendar in an one-month interval
     */
    public void newRound4() {
        this.month += 1;
        if (this.month > 12) {
            this.year += 1;
            this.month = 1;
        }
    }

    /**
     * Updates the calendar in a 6 weeks interval
     */
    public void newRound6() {
        if (this.day == 15) {
            this.day = 1;
            this.month += 2;
        } else {
            this.day = 15;
            this.month += 1;
        }
        if (this.month == 13) {
            this.year = this.year + 1;
            this.month = 1;
        } else if (this.month == 14) {
            this.year = this.year + 1;
            this.month = 2;
        }
    }

    /**
     * Returns the date (dd.mm.yyyy) as a String
     * @return current Date
     */
    public String getCurrentDate() {
        return String.format("%02d", this.day) + "." + String.format("%02d", this.month) + "." + this.year;
    }
}
