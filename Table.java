public class Table {
    private String tableCode;
    private Day dateDine;
    private int ticketCode;

    // constructor
    public Table(String sDateDine, int ticketCode ,String tableCode) {
        this.dateDine = new Day(sDateDine);
        this.ticketCode = ticketCode;
        this.tableCode = tableCode;
    }

    public String getTableCode() {
        return tableCode;
    }

    public Day getDateDine() {
        return dateDine;
    }

    public int getTicketCode() {
        return ticketCode;
    }
}
