package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

public class RmCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length < 1) {
            throw new CommandException("No arguments provided");
        } else if (args.length > 1) {
            throw new CommandException("Too many arguments");
        }
        Inode current = context.getCurrentDirectory();

        String name = args[0];

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

        if(current.asDirectory().contains(name)){
            current.asDirectory().remove(name);
        }else {
            throw new CommandException("Caminho inexistente.");
        }


    }
}
