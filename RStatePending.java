public class RStatePending implements RState{
    @Override
    public String getStatus(Day dateDine, int ticketCode) {
        return "Pending";
    }
}
