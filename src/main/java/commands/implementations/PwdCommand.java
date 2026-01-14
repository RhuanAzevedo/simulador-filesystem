package commands.implementations;

import commands.Command;
import commands.CommandContext;

public class PwdCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) {
        System.out.println(context.getCurrentPath());
    }
}