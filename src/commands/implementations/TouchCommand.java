package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;

public class TouchCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        System.out.println("TouchCommand");
        //TODO...
    }
}
