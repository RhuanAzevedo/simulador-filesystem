package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;

import java.util.List;

public class HistoryCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length > 1) {
            throw new CommandException("Uso: history [n]");
        }

        // Lista o historico da sessao atual
        List<String> history = context.getHistory();
        int startIndex = 0;

        if (args.length == 1) {
            try {
                int count = Integer.parseInt(args[0]);
                if (count < 0) {
                    throw new NumberFormatException();
                }
                startIndex = Math.max(0, history.size() - count);
            } catch (NumberFormatException e) {
                throw new CommandException("Numero invalido: " + args[0]);
            }
        }

        for (int i = startIndex; i < history.size(); i++) {
            System.out.println((i + 1) + "  " + history.get(i));
        }
    }
}
