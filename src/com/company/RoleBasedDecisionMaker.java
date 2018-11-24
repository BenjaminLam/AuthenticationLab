package com.company;

public class RoleBasedDecisionMaker implements IPolicyDecisionMaker {
    @Override
    public boolean isAllowedAccess(String username, Operation operation) {
        for(Role role : AuthorizedRolesFileReaderWriter.getUserRoles(username)){
            if (AuthorizedOperationsFileReaderWriter.getRoleOperations(role).contains(operation))
                return true;
        }
        return false;
    }

    @Override
    public void reset() {
        AuthorizedOperationsFileReaderWriter.resetAuthorizedOperationFile();
        AuthorizedRolesFileReaderWriter.resetAuthorizedRolesFile();

        AuthorizedRolesFileReaderWriter.addUserRole("Alice",Role.MANAGER);
        AuthorizedRolesFileReaderWriter.addUserRole("Bob",Role.SERVTECH);
        AuthorizedRolesFileReaderWriter.addUserRole("Cecilia",Role.POWERUSER);
        AuthorizedRolesFileReaderWriter.addUserRole("David",Role.USER);
        AuthorizedRolesFileReaderWriter.addUserRole("Erica",Role.USER);
        AuthorizedRolesFileReaderWriter.addUserRole("Fred",Role.USER);
        AuthorizedRolesFileReaderWriter.addUserRole("George",Role.USER);

        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.USER,Operation.PRINT);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.USER,Operation.QUEUE);

        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.POWERUSER,Operation.PRINT);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.POWERUSER,Operation.QUEUE);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.POWERUSER,Operation.TOPQUEUE);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.POWERUSER,Operation.RESTART);

        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.MANAGER,Operation.PRINT);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.MANAGER,Operation.QUEUE);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.MANAGER,Operation.TOPQUEUE);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.MANAGER,Operation.RESTART);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.MANAGER,Operation.START);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.MANAGER,Operation.STOP);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.MANAGER,Operation.STATUS);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.MANAGER,Operation.READCONFIG);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.MANAGER,Operation.SETCONFIG);

        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.SERVTECH,Operation.START);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.SERVTECH,Operation.STOP);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.SERVTECH,Operation.RESTART);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.SERVTECH,Operation.STATUS);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.SERVTECH,Operation.READCONFIG);
        AuthorizedOperationsFileReaderWriter.addRoleOperation(Role.SERVTECH,Operation.SETCONFIG);
    }
}
