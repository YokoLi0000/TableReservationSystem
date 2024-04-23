import java.util.Comparator;

public class SorterPhoneNumber implements Comparator<Reservation> {  // sort the reservations by phone number
    // compare the phone number
    public int compare(Reservation r1, Reservation r2) {
        return r1.getPhoneNumber().compareTo(r2.getPhoneNumber());
    }
}
