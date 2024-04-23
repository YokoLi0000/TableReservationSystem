import java.util.ArrayList;
import java.util.Collections;

public class BookingOffice {

    // singleton
    private ArrayList<Reservation> allReservations;
    private static BookingOffice instance = new BookingOffice();
    private BookingOffice() { allReservations = new ArrayList<>();}
    public static BookingOffice getInstance(){ return instance; }

    // add a new Reservation object to allReservation ArrayList
    public Reservation addReservation(String name, String phone, int per, String diningDate) throws ExDateHasAlreadyPassed, ExBookingAlreadyExists
    {
        Reservation r = new Reservation(name, phone, per, diningDate);
        //throw Exceptions
        if (r.getDateDine().compareTo(r.getDateRequest()) < 0) {
            throw new ExDateHasAlreadyPassed();
        }
        for (Reservation r1: allReservations) {
            if (r1.getDateDine().equals(r.getDateDine()) && r1.getGuestName().equals(r.getGuestName())) {
                throw new ExBookingAlreadyExists();
            }
        }
        allReservations.add(r);
        sorting();
        return r;
    }

    // sort allReservation ArrayList
    public void sorting() {
        // sort Guest Name, then sort Phone Number, then sort Dining Date
        Collections.sort(allReservations, new SorterGuestName().thenComparing(new SorterPhoneNumber()).thenComparing(new SorterDateDine()));
    }

    // For CmdListReservations
    public void listReservations()
    {
        System.out.println(Reservation.getListingHeader());
        sorting();
        for (Reservation r: allReservations)
            System.out.println(r.toString());
    }

    // For undo in CmdRequest
    public void removeReservation(Reservation r) {
        allReservations.remove(r);
        BookingTicketController.removeTicket(r.getDateDine());  // remove ticket code
        sorting();
    }

    // For redo in CmdRequest
    public void addReservation(Reservation r) {
        allReservations.add(r);
        BookingTicketController.takeTicket(r.getDateDine());  // take ticket code
        sorting();
    }

    // Search for a Reservation by dateDine and ticketCode
    public Reservation findReservation(Day dateDine, int ticketCode) throws ExBookingNotFound, ExTableAlreadyAssigned, ExDateHasAlreadyPassed {
        // for loop to search for a Reservation
        for (Reservation r: allReservations) {
            if (r.getDateDine().equals(dateDine) && r.getTicketCode()==ticketCode) {
                // throw Exceptions
                if (r.getStatus() instanceof RStateTableAllocated) {
                    throw new ExTableAlreadyAssigned();
                }
                if (r.getDateDine().compareTo(SystemDate.getInstance()) < 0) {
                    throw new ExDateHasAlreadyPassed();
                }
                return r;
            }
        }
        throw new ExBookingNotFound();  // throw Exception
    }

    // For printing pending requests and total number of persons in CmdListTableAllocations
    public void printPendingRequest(Day dateDine) {
        int pendingRequestNum = 0, total = 0;
        for (Reservation r: allReservations) {
            // count the number of RStatePending and total number of persons
            if (r.getDateDine().equals(dateDine) && r.getStatus() instanceof RStatePending) {
                pendingRequestNum++;
                total += r.getTotPersons();
            }
        }
        System.out.printf("\nTotal number of pending requests = %d (Total number of persons = %d)\n", pendingRequestNum, total);
    }

    // For CmdCancel
    public Reservation cancelBooking(Day dateDine, int ticketCode) throws ExBookingNotFound, ExDateHasAlreadyPassed {
        // throw Exception
        if (dateDine.compareTo(SystemDate.getInstance()) < 0) {
            throw new ExDateHasAlreadyPassed();
        }

        // For loop to find the booking to be canceled
        Reservation targetBooking = null;
        for (Reservation r: allReservations) {
            if (r.getDateDine().equals(dateDine) && r.getTicketCode()==ticketCode) {
                targetBooking = r;
                break;
            }
        }
        // throw Exception
        if (targetBooking == null) {
            throw new ExBookingNotFound();
        }
        allReservations.remove(targetBooking);
        return targetBooking;
    }

    // For redo in CmdCancel
    public void cancelBooking(Reservation r) {
        allReservations.remove(r);
        TableController table = TableController.getInstance();
        table.cancelTable(r.getDateDine(), r.getTicketCode());
    }

    // For undo in CmdCancel
    public void undoCancelBooking(Reservation r) {
        allReservations.add(r);
    }
}
