import java.util.Comparator;

public class SorterGuestName implements Comparator<Reservation> {  // sort the reservations by guest name
    // compare the guest names
    public int compare(Reservation r1, Reservation r2) {
        return r1.getGuestName().compareTo(r2.getGuestName());
    }
}
