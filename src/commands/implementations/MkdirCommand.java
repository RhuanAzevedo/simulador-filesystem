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

        String name = args[0];
        Inode current = context.getCurrentDirectory();

        if (!current.isDirectory()) {
            throw new CommandException("Contexto atual inválido.");
        }

        if (current.asDirectory().contains(name)) {
            throw new CommandException("Não foi possível criar o diretório '" + name + "': Arquivo já existe");
        }

        Inode newDir = Inode.createDirectory(current);
        current.asDirectory().addChild(name, newDir);
    }
}