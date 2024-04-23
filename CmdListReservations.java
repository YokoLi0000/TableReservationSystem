public class CmdListReservations implements Command{
    // print reservations
    public void execute(String[] cmdParts) {
        BookingOffice bo = BookingOffice.getInstance();
        bo.listReservations();
    }
}
