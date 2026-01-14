package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

import java.util.List;

public class DiffCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length != 2) {
            throw new CommandException("Uso: diff <arq1> <arq2>");
        }

        Inode first = context.resolvePath(args[0]);
        Inode second = context.resolvePath(args[1]);

        if (first == null || second == null) {
            throw new CommandException("Arquivo nao encontrado.");
        }
        if (!first.isFile() || !second.isFile()) {
            throw new CommandException("Ambos os alvos devem ser arquivos.");
        }

        // Comparacao linha a linha
        List<String> a = first.asFile().getLines();
        List<String> b = second.asFile().getLines();

        int max = Math.max(a.size(), b.size());
        boolean different = false;

        for (int i = 0; i < max; i++) {
            String left = i < a.size() ? a.get(i) : "";
            String right = i < b.size() ? b.get(i) : "";
            if (!left.equals(right)) {
                different = true;
                System.out.println("Linha " + (i + 1) + ":");
                System.out.println("< " + left);
                System.out.println("> " + right);
            }
        }

        if (!different) {
            System.out.println("Arquivos sao identicos.");
        }
    }
}
