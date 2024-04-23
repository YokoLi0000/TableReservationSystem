public class CmdSuggestTable implements Command{
    BookingOffice bo = BookingOffice.getInstance();
    TableController table = TableController.getInstance();

    @Override
    public void execute(String[] cmdParts) {  // suggest tables for a reservation
        try {
            if (cmdParts.length < 3) {
                throw new ExInsufficientArguments();
            }
            // find the corresponding reservation by dining date and ticket code
            Reservation r = bo.findReservation(new Day(cmdParts[1]), Integer.parseInt(cmdParts[2]));
            table.suggestTable(r);  // suggest table
        } catch (NumberFormatException e) {
            System.out.println("Wrong number format!");
        } catch (ExInsufficientArguments | ExBookingNotFound | ExDateHasAlreadyPassed | ExTableAlreadyAssigned e) {
            System.out.println(e.getMessage());
        }
    }
}
