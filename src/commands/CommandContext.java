package commands;

import core.VirtualFileSystem;
import inode.Inode;

public class CommandContext {

    private final VirtualFileSystem vfs;
    private Inode currentDirectory;
    private String currentPath;

    public CommandContext(VirtualFileSystem vfs) {
        this.vfs = vfs;
        this.currentDirectory = vfs.getRoot();
        this.currentPath = "/";
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

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }
}
