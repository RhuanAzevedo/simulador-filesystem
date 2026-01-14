package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

import java.util.ArrayList;
import java.util.List;

public class WcCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if(args.length < 1){
            throw new CommandException("Too few arguments.");
        } else if (args.length > 1) {
            throw new CommandException("Too many arguments.");
        }

        Inode current = context.getCurrentDirectory();
        String name = args[0];

        if(context.resolvePath(name) != null){
            current =  context.resolvePath(name);
            if(current.isFile()){
                List<String> content = current.asFile().getLines();
                List<String> words = new ArrayList<>();
                int characterCount = 0;
                for(String line : content){
                    String[] word = line.trim().split("\\s+");
                    for(String word1 : word){
                        words.add(word1);
                    }
                }
                for (String s : content) {
                    for(char c : s.toCharArray()){
                        characterCount++;
                    }
                }
                int wordCount = words.size();
                int lineCount = content.size();

                System.out.println("Palavras: " + wordCount + "\nLinhas: " + lineCount + "\nCaracteres " + characterCount);

            } else {
                throw new CommandException("Not a file.");
            }
        }else {
            throw new CommandException("Invalid path.");
        }
    }
}
