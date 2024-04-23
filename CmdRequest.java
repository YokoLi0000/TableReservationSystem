public class CmdRequest extends RecordedCommand {
    Reservation r;
    BookingOffice bo = BookingOffice.getInstance();

    @Override
    public void execute(String[] cmdParts) {  // for request a booking
        try {
            if (cmdParts.length<5) {
                throw new ExInsufficientArguments();
            }
            int tot = Integer.parseInt(cmdParts[3]);  // total number of persons
            r = bo.addReservation(cmdParts[1], cmdParts[2], tot, cmdParts[4]);
            addUndoCommand(this);
            clearRedoList();
            r.printRequestString();
        } catch (NumberFormatException e) {
            System.out.println("Wrong number format!");
        } catch (ExDateHasAlreadyPassed | ExBookingAlreadyExists | ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void undoMe()
    {
        bo.removeReservation(r);
        addRedoCommand(this);
    }

    @Override
    public void redoMe()
    {
        bo.addReservation(r);
        addUndoCommand(this);
    }
}