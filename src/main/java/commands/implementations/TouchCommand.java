package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

import static core.Shell.GREEN;

public class TouchCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length == 0) throw new CommandException("Por favor, preencha os argumentos");
        if (args.length > 1) throw new CommandException("Argumentos excedentes");
        String filename = args[0];
        // Verifica se o nome de arquivo contém ".", "/" ou " ", exceto por finalizando em ".txt".
        if (filename.matches("[^/.\\s]+(\\.txt)?")) {
            if (!filename.endsWith(".txt")) {
                filename += ".txt";
            }
            context.getCurrentDirectory().asDirectory().addChild(filename, Inode.createFile());
            System.out.println(GREEN + context.getCurrentPath() + "/" + filename + " created");
        } else {
            throw new CommandException("Filename inválido!");
        }
    }
}
