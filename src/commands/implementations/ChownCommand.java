package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

public class ChownCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        // Validação
        if (args.length < 2) {
            throw new CommandException("Uso: chown <novo_dono> <arquivo_ou_diretorio>");
        }

        String newOwner = args[0];
        String path = args[1];

        // Encontrar o alvo
        Inode target = context.resolvePath(path);

        if (target == null) {
            throw new CommandException("Arquivo ou diretório não encontrado: " + path);
        }

        // Alterar o dono
        target.setOwner(newOwner);

        System.out.println("Dono de '" + path + "' alterado para " + newOwner);
    }
}