public class CmdStartNewDay extends RecordedCommand{
    String preDate, date;

    @Override
    public void execute(String[] cmdParts) {  // change a new date
        try {
            if (cmdParts.length < 2) {
                throw new ExInsufficientArguments();
            }
            SystemDate instance = SystemDate.getInstance();
            date = cmdParts[1];  // save the new date for redo
            preDate = instance.toString();  // save the previous date for undo
            instance.set(cmdParts[1]);
            addUndoCommand(this);
            clearRedoList();
            System.out.println("Done.");
        } catch (ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void undoMe()
    {
        SystemDate.getInstance().set(preDate);
        addRedoCommand(this); //<====== upon undo, we should keep a copy in the redo list (addRedoCommand is implemented in RecordedCommand.java)
    }

    @Override
    public void redoMe()
    {
        SystemDate.getInstance().set(date);
        addUndoCommand(this); //<====== upon redo, we should keep a copy in the undo list
    }
}
