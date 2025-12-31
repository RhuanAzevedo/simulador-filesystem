package commands;

import core.VirtualFileSystem;
import inode.Inode;

public class CommandContext {

    private final VirtualFileSystem vfs;
    private Inode currentDirectory;

    public CommandContext(VirtualFileSystem vfs) {
        this.vfs = vfs;
        this.currentDirectory = vfs.getRoot();
    }

    public VirtualFileSystem getFileSystem() {
        return vfs;
    }

    public Inode getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(Inode dir) {
        this.currentDirectory = dir;
    }
}
