package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;
import inode.content.DirectoryContent;

public class CdCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length < 1) return;

        String path = args[0];

        // Caminho absoluto começa com '/'
        if (path.startsWith("/")) {
            context.goToRoot();
            if (path.equals("/")) return;
        }

        String[] steps = path.split("/");

        for (String step : steps) {
            if (step.isEmpty() || step.equals(".")) {
                continue;
            }
            Inode current = context.getCurrentDirectory();
            if (!current.isDirectory()) {
                throw new CommandException("Caminho inválido. '" + context.getCurrentPath() + "' não é diretório.");
            }

            DirectoryContent dirContent = current.asDirectory();

            // cd ..
            if (step.equals("..")) {
                Inode parent = dirContent.getEntries().get("..");
                // leaveDirectory já remove o nome da pilha
                context.leaveDirectory(parent);
            }
            // cd <nome>
            else {
                Inode nextInode = dirContent.getEntries().get(step);

                if (nextInode == null) {
                    throw new CommandException("Diretório não encontrado: " + step);
                }
                if (!nextInode.isDirectory()) {
                    throw new CommandException("'" + step + "' não é um diretório.");
                }

                // enterDirectory já adiciona o nome na pilha
                context.enterDirectory(step, nextInode);
            }
        }
    }
}