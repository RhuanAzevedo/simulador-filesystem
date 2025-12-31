package inode;

public class PermissionBits {

    private boolean read;
    private boolean write;
    private boolean execute;

    public PermissionBits(boolean read, boolean write, boolean execute) {
        this.read = read;
        this.write = write;
        this.execute = execute;
    }

    public boolean canRead() {
        return read;
    }

    public boolean canWrite() {
        return write;
    }

    public boolean canExecute() {
        return execute;
    }

    public static PermissionBits fromDigit(int digit) {
        if (digit < 0 || digit > 7) {
            throw new IllegalArgumentException("Permissão inválida: " + digit);
        }

        return new PermissionBits(
                (digit & 4) != 0,
                (digit & 2) != 0,
                (digit & 1) != 0
        );
    }

    public String toSymbol() {
        return (read ? "r" : "-") +
                (write ? "w" : "-") +
                (execute ? "x" : "-");
    }
}
