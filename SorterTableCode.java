import java.util.Comparator;

public class SorterTableCode implements Comparator<Table> {  // sort the table codes
    // compare the table codes by prefix then by the number
    @Override
    public int compare(Table t1, Table t2) {
        if (t1.getTableCode().charAt(0) == t2.getTableCode().charAt(0)) {
            return Integer.compare(Integer.parseInt(t1.getTableCode().substring(1, 3)), Integer.parseInt(t2.getTableCode().substring(1, 3)));
        }
        if (t1.getTableCode().contains("T")) {
            if (t2.getTableCode().contains("F") || t2.getTableCode().contains("H")) {
                return -1;
            }
        } else if (t1.getTableCode().contains("F")) {
            if (t2.getTableCode().contains("H")) {
                return -1;
            }
        }
        return 1;
    }
}
