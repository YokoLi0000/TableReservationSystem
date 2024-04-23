public class CmdListTableAllocations implements Command{  // print table allocations
    @Override
    public void execute(String[] cmdParts) {
        try {
            if (cmdParts.length<2) {
                throw new ExInsufficientArguments();
            }
            TableController table = TableController.getInstance();
            Day dateDine = new Day(cmdParts[1]);
            table.listTableAllocations(dateDine);
        } catch (ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        }
    }
}
