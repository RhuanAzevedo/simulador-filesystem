package commands;

public interface Command {

    void execute(String[] args, CommandContext context)
            throws CommandException;
}
