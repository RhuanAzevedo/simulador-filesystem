package inode;

public class Permissions {

    private PermissionBits owner;
    private PermissionBits group;
    private PermissionBits others;

    public Permissions(PermissionBits owner,
                       PermissionBits group,
                       PermissionBits others) {
        this.owner = owner;
        this.group = group;
        this.others = others;
    }

    public static Permissions fromOctal(int mode) {
        if (mode < 0 || mode > 777) {
            throw new IllegalArgumentException("Modo inválido: " + mode);
        }

        int o = (mode / 100) % 10;
        int g = (mode / 10) % 10;
        int ot = mode % 10;

        return new Permissions(
                PermissionBits.fromDigit(o),
                PermissionBits.fromDigit(g),
                PermissionBits.fromDigit(ot)
        );
    }

    public static Permissions defaultFile() {
        return fromOctal(644);
    }

    public static Permissions defaultDirectory() {
        return fromOctal(755);
    }

    public String toSymbol() {
        return owner.toSymbol()
                + group.toSymbol()
                + others.toSymbol();
    }

    public PermissionBits owner() {
        return owner;
    }

    public PermissionBits group() {
        return group;
    }

    public PermissionBits others() {
        return others;
    }
}
