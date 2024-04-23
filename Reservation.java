public class Reservation {

        private String guestName;
        private String phoneNumber;
        private int totPersons;
        private Day dateDine;
        private Day dateRequest;
        private int ticketCode;
        private RState status;

        // constructor
        public Reservation(String guestName, String phoneNumber, int totPersons, String sDateDine)
        {
                this.guestName = guestName;
                this.phoneNumber = phoneNumber;
                this.totPersons = totPersons;
                this.dateDine = new Day(sDateDine);
                this.dateRequest = SystemDate.getInstance().clone();
                this.ticketCode = BookingTicketController.takeTicket(this.dateDine);
                this.status = new RStatePending();
        }

        // For CmdListReservation
        @Override
        public String toString()
        {
                return String.format("%-13s%-11s%-14s%-25s%4d       %s", guestName, phoneNumber, dateRequest, dateDine+String.format(" (Ticket %d)", ticketCode), totPersons, status.getStatus(dateDine, ticketCode));
        }

        // For CmdListReservation
        public static String getListingHeader()
        {
                return String.format("%-13s%-11s%-14s%-25s%-11s%s", "Guest Name", "Phone", "Request Date", "Dining Date and Ticket", "#Persons", "Status");
        }

        // For CmdRequest
        public void printRequestString() {
                System.out.printf("Done. Ticket code for %s: %d\n", dateDine, ticketCode);
        }

        public String getGuestName() {
                return guestName;
        }

        public String getPhoneNumber() {
                return phoneNumber;
        }

        public Day getDateDine() {
                return dateDine;
        }

        public Day getDateRequest() {
                return dateRequest;
        }

        public int getTicketCode() { return ticketCode; }

        public RState getStatus() { return status; }

        public int getTotPersons() { return totPersons; }

        // For CmdAssignTable
        public RState changeStatusToTableAllocated() {
                return status = new RStateTableAllocated();
        }

        // For undo in CmdAssignTable
        public RState changeStatusToPending() {
                return status = new RStatePending();
        }
}