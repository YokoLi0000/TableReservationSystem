public class CmdCancel extends RecordedCommand{
    BookingOffice bo = BookingOffice.getInstance();
    TableController table = TableController.getInstance();
    Reservation r;
    int ticketCode;
    String tableCode = "", dayString;

    @Override
    public void execute(String[] cmdParts) {  // cancel a booking
        try {
            if (cmdParts.length < 3) {
                throw new ExInsufficientArguments();
            }
            dayString = cmdParts[1];  // save the day String for undo
            ticketCode = Integer.parseInt(cmdParts[2]);  // save the ticket code for undo
            r = bo.cancelBooking(new Day(cmdParts[1]), ticketCode);  // cancel the reservation
            tableCode = table.findTable(r.getDateDine(), r.getTicketCode());  // save the allocated table String for undo
            table.cancelTable(r.getDateDine(), r.getTicketCode());   // cancel the reserved table
            addUndoCommand(this);
            clearRedoList();
            System.out.println("Done.");
        } catch (NumberFormatException e) {
            System.out.println("Wrong number format!");
        } catch (ExInsufficientArguments | ExDateHasAlreadyPassed | ExBookingNotFound e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void undoMe() {
        bo.undoCancelBooking(r);  // add back the reservation
        if (!tableCode.equals("")) {
            String[] tableCodeParts = tableCode.split(" ");
            // for loop to add back the tables
            for (String s : tableCodeParts) {
                table.addTable(dayString, r.getTicketCode(), s);
            }
        }
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        bo.cancelBooking(r);  // remove the reservation
        addUndoCommand(this);
    }
}
