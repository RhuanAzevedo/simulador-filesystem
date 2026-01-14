package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import commands.CommandUtils;
import inode.Inode;

public class DuCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length != 1) {
            throw new CommandException("Uso: du <diretorio>");
        }

        String path = args[0];
        Inode target = context.resolvePath(path);
        if (target == null) {
            throw new CommandException("Caminho nao encontrado: " + path);
        }

        // Mostra o tamanho total do diretorio/arquivo
        long size = target.getSize();
        String absolute = CommandUtils.toAbsolutePath(context, path);
        System.out.println(size + " " + absolute);
    }
}
