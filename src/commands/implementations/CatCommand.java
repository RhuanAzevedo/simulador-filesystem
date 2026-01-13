package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

public class CatCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        Inode current = context.getCurrentDirectory();
        if(args.length < 1){
            throw new CommandException("Too few arguments");
        }else if(args.length > 1){
            throw new CommandException("Too many arguments");
        } else {
            if(context.resolvePath(args[0]) == null) {
                throw new CommandException("Invalid path or file name");
            }else {
                current = context.resolvePath(args[0]);
                if(current.isDirectory()) {
                    throw new CommandException("Not a file");
                }else if(current.isFile()) {
                    for(String output: current.asFile().getLines()){
                        System.out.println(output);
                    }
                    System.out.println();
                }
            }
        }
    }
}
