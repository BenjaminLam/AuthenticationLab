package com.company;

import java.util.List;

public class ActionControlListDecisionMaker implements  IPolicyDecisionMaker{
    @Override
    public boolean isAllowedAccess(String username, Operation operation) {
        List<Operation> allowedOperations = AccessControlListFileReaderWriter.getUserRights(username);
        if (allowedOperations.contains(operation))
            return true;
        return false;
    }

    @Override
    public void reset() {
        AccessControlListFileReaderWriter.resetAccessControlListFile();

        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.PRINT);
        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.QUEUE);
        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.TOPQUEUE);
        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.START);
        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.STOP);
        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.RESTART);
        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.STATUS);
        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.READCONFIG);
        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.SETCONFIG);
        AccessControlListFileReaderWriter.addUserRight("Alice",Operation.CHANGEPASSWORD);

        AccessControlListFileReaderWriter.addUserRight("Bob",Operation.START);
        AccessControlListFileReaderWriter.addUserRight("Bob",Operation.STOP);
        AccessControlListFileReaderWriter.addUserRight("Bob",Operation.RESTART);
        AccessControlListFileReaderWriter.addUserRight("Bob",Operation.STATUS);
        AccessControlListFileReaderWriter.addUserRight("Bob",Operation.SETCONFIG);
        AccessControlListFileReaderWriter.addUserRight("Bob",Operation.READCONFIG);

        AccessControlListFileReaderWriter.addUserRight("Cecilia",Operation.PRINT);
        AccessControlListFileReaderWriter.addUserRight("Cecilia",Operation.QUEUE);
        AccessControlListFileReaderWriter.addUserRight("Cecilia",Operation.TOPQUEUE);
        AccessControlListFileReaderWriter.addUserRight("Cecilia",Operation.RESTART);

        AccessControlListFileReaderWriter.addUserRight("David",Operation.PRINT);
        AccessControlListFileReaderWriter.addUserRight("David",Operation.QUEUE);

        AccessControlListFileReaderWriter.addUserRight("Erica",Operation.PRINT);
        AccessControlListFileReaderWriter.addUserRight("Erica",Operation.QUEUE);

        AccessControlListFileReaderWriter.addUserRight("Fred",Operation.PRINT);
        AccessControlListFileReaderWriter.addUserRight("Fred",Operation.QUEUE);

        AccessControlListFileReaderWriter.addUserRight("George",Operation.PRINT);
        AccessControlListFileReaderWriter.addUserRight("George",Operation.QUEUE);
    }

}
