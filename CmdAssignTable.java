import java.util.ArrayList;

public class CmdAssignTable extends RecordedCommand{  // for assign tables for a reservation
    TableController table = TableController.getInstance();
    ArrayList<Table> tableArrayList;
    Reservation r;

    @Override
    public void execute(String[] cmdParts) {
        try {
            BookingOffice bo = BookingOffice.getInstance();
            int seatNum = 0;
            if (cmdParts.length < 4) {
                throw new ExInsufficientArguments();
            }
            int ticketCode = Integer.parseInt(cmdParts[2]);
            tableArrayList = new ArrayList<>();  // save the assigned tables for undo and redo
            r = bo.findReservation(new Day(cmdParts[1]), ticketCode);
            for (int i = 3; i < cmdParts.length; i++) {
                if (cmdParts[i].contains("T")) {
                    seatNum += 2;
                } else if (cmdParts[i].contains("F")) {
                    seatNum += 4;
                } else {
                    seatNum += 8;
                }
                table.checkTable(cmdParts[i], new Day(cmdParts[1]));  // check if the tables inputted are valid
            }
            if (seatNum < r.getTotPersons()) {
                throw new ExNotEnoughSeats();
            }
            // for loop to assign tables
            for (int i=3; i<cmdParts.length; i++) {
                Table t = table.addTable(cmdParts[1], ticketCode, cmdParts[i]);
                tableArrayList.add(t);
            }
            r.changeStatusToTableAllocated();  // change the status from pending to table allocated
            addUndoCommand(this);
            clearRedoList();
            System.out.println("Done.");
        } catch (NumberFormatException e) {
            System.out.println("Wrong number format!");
        } catch (ExInsufficientArguments | ExBookingNotFound | ExTableAlreadyAssigned | ExDateHasAlreadyPassed | ExTableCodeNotFound | ExNotEnoughSeats | ExTableReserved e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void undoMe() {
        for (Table t:tableArrayList) {
            table.removeTable(t);
        }
        r.changeStatusToPending();
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        for (Table t: tableArrayList) {
            table.addTable(t);
        }
        r.changeStatusToTableAllocated();
        addUndoCommand(this);
    }
}
