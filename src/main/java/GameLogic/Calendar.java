package GameLogic;

/**
 * Every Game starts in Sep 2021 and each Round takes 1 month or 6 weeks
 * (6 weeks due to the expected value of 3.5 of the dice and 30 playing fields per year).
 * Will be used for the high score
 */

public class Calendar {

    int year;
    int month;
    int day;

    public Calendar(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    //1 month interval
    public void newRound4() {
        this.month += 1;
        if (this.month > 12) {
            this.year += 1;
            this.month = 1;
        }
    }

    //6 weeks interval
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


    public String getCurrentDate() {
        return "" + this.day + "." + this.month + "." + this.year;
    }
}
