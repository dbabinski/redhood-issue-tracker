package com.redhood.issuetracker.service.account.permissions.handler;

public class PermissionsHandler {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    public static final int INT_READ = 8;
    public static final int INT_ADD = 4;
    public static final int INT_DELETE = 2;
    public static final int INT_UPDATE = 1;

    private boolean read = false;
    private boolean add = false;
    private boolean delete = false;
    private boolean update = false;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    public PermissionsHandler() {
    }

    public PermissionsHandler(Integer permissions) {
        setPermissions(permissions);
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Getters and Setters
    //------------------------------------------------------------------------------------------------------------------
    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    /**
     * Getting possision of each permission value.
     * When bit is set, method return {@code true} to values: read, add, delete, update.
     * @param permissions
     */
    private void setPermissions(Integer permissions) {
        if (permissions != null) {
            read = isBitSet(permissions, PermissionsEnum.READ.getPosition());
            add = isBitSet(permissions, PermissionsEnum.ADD.getPosition());
            delete = isBitSet(permissions, PermissionsEnum.DELETE.getPosition());
            update = isBitSet(permissions, PermissionsEnum.UPDATE.getPosition());
        }
    }
    //------------------------------------------------------------------------------------------------------------------



    private boolean isBitSet(int number, int position) {
        int newNumber = number >> (position - 1);
        return (newNumber & 1) == 1;
    }


    public boolean check(PermissionsEnum permissionsType) throws PermissionsException {
        boolean permissions = false;
        switch (permissionsType) {
            case READ:
                permissions = read;
                break;
            case ADD:
                permissions = add;
                break;
            case DELETE:
                permissions = delete;
                break;
            case UPDATE:
                permissions = update;
                break;
        }
        if (!permissions) {
            throw new PermissionsException();
        }
        return permissions;
    }
}
