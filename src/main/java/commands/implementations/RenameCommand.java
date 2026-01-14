package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;
import inode.content.DirectoryContent;

public class RenameCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length != 2) {
            throw new CommandException("Uso: rename <nome_antigo> <nome_novo>");
        }

        String oldName = args[0];
        String newName = args[1];

        // Impede renomear as entradas especiais
        if (oldName.equals(".") || oldName.equals("..")) {
            throw new CommandException("Não é possível renomear '.' ou '..'");
        }

        DirectoryContent dir = context.getCurrentDirectory().asDirectory();

        if (!dir.contains(oldName)) {
            throw new CommandException("Arquivo não encontrado: " + oldName);
        }

        if (dir.contains(newName)) {
            throw new CommandException("Já existe um arquivo com o nome: " + newName);
        }

        // Recupera o inode, remove a entrada antiga e adiciona a nova
        Inode inode = dir.getEntries().remove(oldName);
        dir.addChild(newName, inode);

        System.out.println("Renomeado de '" + oldName + "' para '" + newName + "'");
    }
}