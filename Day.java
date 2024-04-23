public class Day implements Cloneable{

    private int year;
    private int month;
    private int day;

    private static final String MonthNames = "JanFebMarAprMayJunJulAugSepOctNovDec";

    //Constructor
    public Day(int y, int m, int d) {
        this.year=y;
        this.month=m;
        this.day=d;
    }

    public void set(String sDay) //Set year,month,day based on a string like 01-Mar-2019
    {
        String[] sDayParts = sDay.split("-");
        this.day = Integer.parseInt(sDayParts[0]); //Apply Integer.parseInt for sDayParts[0];
        this.year = Integer.parseInt(sDayParts[2]);
        this.month = MonthNames.indexOf(sDayParts[1])/3+1;
    }

    public Day(String sDay) {set(sDay);} //Constructor

    // Return a string for the day like dd MMM yyyy
    @Override
    public String toString()
    {
        return day+"-"+ MonthNames.substring((month-1)*3,(month)*3) + "-"+ year; // (month-1)*3,(month)*3
    }

    @Override
    public Day clone()  // clone the Day object
    {
        Day copy=null;
        try {
            copy = (Day) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return copy;
    }

    @Override
    public int hashCode() {
        return year*10000+month*100+day;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        }

        if (this.getClass() != otherObject.getClass()) {
            return false;
        }

        Day otherDay = (Day) otherObject;

        if (this.year != otherDay.year) {
            return false;
        }
        if (this.month != otherDay.month) {
            return false;
        }
        return this.day == otherDay.day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return day;
    }

    // compare which day is the earlier day
    public int compareTo(Day d) {
        if (year - d.year == 0) {
            if (month - d.month == 0) {
                return day - d.day;
            }
            return month - d.month;
        }
        return year - d.year;
    }
}
