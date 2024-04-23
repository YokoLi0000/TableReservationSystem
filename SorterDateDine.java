import java.util.Comparator;

public class SorterDateDine implements Comparator<Reservation> {  // sort the reservations by dining date
    // compare which day is earlier day
    public int compare(Reservation r1, Reservation r2) {
        if (r1.getDateDine().getYear() == r2.getDateDine().getYear()) {
            if (r1.getDateDine().getMonth() == r2.getDateDine().getMonth()) {
                return r1.getDateDine().getDate() - r2.getDateDine().getDate();
            }
            return r1.getDateDine().getMonth() - r2.getDateDine().getMonth();
        }
        return r1.getDateDine().getYear() - r2.getDateDine().getYear();
    }
}
