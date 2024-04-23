public class ExTableReserved extends Exception{
    public ExTableReserved(String tableCode) {super("Table " + tableCode + " is already reserved by another booking!");}
}
