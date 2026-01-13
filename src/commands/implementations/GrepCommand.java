package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

import java.util.List;

public class GrepCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length != 2) {
            throw new CommandException("Uso: grep <termo> <arquivo>");
        }

        String term = args[0];
        String path = args[1];

        Inode target = context.resolvePath(path);
        if (target == null) {
            throw new CommandException("Arquivo nao encontrado: " + path);
        }
        if (!target.isFile()) {
            throw new CommandException("Nao e um arquivo: " + path);
        }

        // Filtra e exibe linhas que contem o termo informado
        List<String> lines = target.asFile().getLines();
        for (String line : lines) {
            if (line.contains(term)) {
                System.out.println(line);
            }
        }
    }
}
