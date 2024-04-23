public class RStateTableAllocated implements RState{
    private TableController table = TableController.getInstance();

    // For CmdListReservation
    @Override
    public String getStatus(Day dateDine, int ticketCode) {
        return "Table assigned: " + table.findTable(dateDine, ticketCode);
    }
}
