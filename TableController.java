import java.util.ArrayList;
import java.util.Collections;

public class TableController {
    // singleton
    private ArrayList<Table> allTable;
    private static TableController instance = new TableController();
    private TableController() {allTable = new ArrayList<>();}
    public static TableController getInstance() {return instance;}

    // add a new Table object to allTable ArrayList
    public Table addTable(String sDateDine, int ticketCode ,String tableCode) {
        Table t = new Table(sDateDine, ticketCode, tableCode);
        allTable.add(t);
        return t;
    }

    // For undo in CmdAssignTable
    public void removeTable(Table t) {
        allTable.remove(t);
    }

    // For redo in CmdAssignTable
    public void addTable(Table t) {
        allTable.add(t);
    }

    // For RStateTableAllocated to find the tables assigned
    public String findTable(Day DateDine, int ticketCode) {
        StringBuilder s = new StringBuilder();
        for (Table t : allTable) {
            if (t.getDateDine().equals(DateDine) && t.getTicketCode() == ticketCode) {
                s.append(t.getTableCode()).append(" ");
            }
        }
        return s.toString();
    }

    // For CmdListTableAllocations
    public void listTableAllocations(Day dateDine) {
        // as we don't want to sort the allTable ArrayList
        // so another ArrayList is created only for this function
        ArrayList<Table> printList = new ArrayList<>(allTable);
        Collections.sort(printList, new SorterTableCode());  // sort the ArrayList by table code
        System.out.println("Allocated tables:");
        String s = "";
        // for loop to find allocated tables and the corresponding ticket codes
        for (Table t: printList) {
            if (t.getDateDine().equals(dateDine)) {
                s += String.format("%s (Ticket %d)\n", t.getTableCode(), t.getTicketCode());
            }
        }
        if (s.equals("")) {
            s += "[None]\n";
        }
        System.out.println(s);
        // find and print available tables
        String availableTable = findAvailableTable(s);
        System.out.println("Available tables:\n" + availableTable);
        BookingOffice bo = BookingOffice.getInstance();
        bo.printPendingRequest(dateDine);
    }

    // find available table String
    public String findAvailableTable(String s) {
        String availableTable = "";
        // for loop to find available table String
        for (int i = 0; i <19 ; i++) {
            String tableCode = "";
            if (i < 9) {
                tableCode = "T0" + (i + 1);
            } else if (i == 9) {
                tableCode = "T10";
            } else if (i > 9 && i < 16) {
                tableCode = "F0" + (i - 9);
            } else {
                tableCode = "H0" + (i - 15);
            }
            if (!s.contains(tableCode)) {
                availableTable += tableCode + " ";
            }
        }
        return availableTable;
    }

    // check if the table is valid otherwise throw Exceptions
    public void checkTable(String tableCode, Day dateDine) throws ExTableReserved, ExTableCodeNotFound {
        int tableNum = Integer.parseInt(tableCode.substring(1, 3));
        if (!(tableCode.contains("T") || tableCode.contains("F") || tableCode.contains("H"))) {
            throw new ExTableCodeNotFound(tableCode);
        } else {
            if (tableCode.contains("T") && (tableNum < 1 || tableNum > 10)) {
                throw new ExTableCodeNotFound(tableCode);
            } else if (tableCode.contains("F") && (tableNum < 1 || tableNum > 6)) {
                throw new ExTableCodeNotFound(tableCode);
            } else if (tableCode.contains("H") && (tableNum < 1 || tableNum > 3)) {
                throw new ExTableCodeNotFound(tableCode);
            }
        }
        for (Table t: allTable) {
            if (t.getTableCode().equals(tableCode) && t.getDateDine().equals(dateDine)) {
                throw new ExTableReserved(tableCode);
            }
        }
    }

    // For CmdCancel
    public void cancelTable(Day dateDine, int ticketCode) {
        allTable.removeIf(t -> t.getDateDine().equals(dateDine) && t.getTicketCode() == ticketCode);
    }

    // For CmdSuggestTable
    public void suggestTable(Reservation r) {
        String suggestTableCode = "", allocatedTable = "";
        int personsWaiting = r.getTotPersons(), availableAmount = 68; // Total seat number is 68
        // for loop to find the String of allocated table codes and count the available seats
        for (Table t: allTable) {
            if (t.getDateDine().equals(r.getDateDine())) {
                allocatedTable += t.getTableCode() + " ";
                if (t.getTableCode().contains("T")) {
                    availableAmount -= 2;
                } else if (t.getTableCode().contains("F")) {
                    availableAmount -= 4;
                } else  {
                    availableAmount -= 8;
                }
            }
        }

        if (availableAmount < personsWaiting) {
            System.out.printf("Suggestion for %d persons: Not enough tables\n", personsWaiting);
        } else {
            String availableTable = findAvailableTable(allocatedTable);
            // while loop to find suitable tables
            while (personsWaiting > 0) {
                // check if the tables still available
                boolean Tfull = false, Ffull = false, Hfull = false;
                if (!availableTable.contains("T")) {
                    Tfull = true;  // Tfull indicating the availability table of 2
                }
                if (!availableTable.contains("F")) {
                    Ffull = true;  // Ffull indicating the availability table of 4
                }
                if (!availableTable.contains("H")) {
                    Hfull = true;  //Hfull indicating the availability table of 8
                }

                // find the suggest table codes
                String tempTableCode = "";
                if (!Hfull && ((personsWaiting > 4) || (Ffull && personsWaiting > 2) || (Ffull && Tfull))) {
                    for (int i = 0; i < 3; i++) {
                        tempTableCode = "H0" + (i + 1);
                        if (availableTable.contains(tempTableCode)) {
                            availableTable = availableTable.replaceFirst(tempTableCode, "");
                            suggestTableCode += tempTableCode + " ";
                            personsWaiting -= 8;
                            break;
                        }
                    }
                } else if (!Ffull && (personsWaiting > 2 || (Hfull && personsWaiting > 2) || Tfull)) {
                    for (int i = 0; i < 6; i++) {
                        tempTableCode = "F0" + (i + 1);
                        if (availableTable.contains(tempTableCode)) {
                            availableTable = availableTable.replaceFirst(tempTableCode, "");
                            suggestTableCode += tempTableCode + " ";
                            personsWaiting -= 4;
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        if (i <= 8) {
                            tempTableCode = "T0" + (i + 1);
                        } else {
                            tempTableCode = "T10";
                        }
                        if (availableTable.contains(tempTableCode)) {
                            availableTable = availableTable.replaceFirst(tempTableCode, "");
                            suggestTableCode += tempTableCode + " ";
                            personsWaiting -= 2;
                            break;
                        }
                    }
                }
            }
            System.out.printf("Suggestion for %d persons: %s\n", r.getTotPersons(), suggestTableCode);
        }
    }
}
