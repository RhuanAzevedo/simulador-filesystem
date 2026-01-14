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

        if(name.contains("/")){
            String[] splitName = name.split("/");
            String path = "";
            for(int i = 0; i < splitName.length-1; i++){
                path += splitName[i]+"/";
            }
            if(context.resolvePath(path) != null && context.resolvePath(path).isDirectory()){
                current = context.resolvePath(path);
            }else {
                throw new CommandException("Caminho inexistente.");
            }
            name = splitName[splitName.length-1];
        }

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