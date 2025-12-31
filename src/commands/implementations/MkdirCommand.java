package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

public class MkdirCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {

        if (args.length != 1) {
            throw new CommandException("Uso: mkdir <nome>");
        }

        Inode current = context.getCurrentDirectory();

        if (!current.isDirectory()) {
            throw new CommandException("Diretório atual inválido");
        }

        String name = args[0];

        if (current.asDirectory().contains(name)) {
            throw new CommandException("Diretório já existe: " + name);
        }

        Inode newDir = Inode.createDirectory(current);
        current.asDirectory().addChild(name, newDir);
    }
}
