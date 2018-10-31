package com.company;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {
    protected RMIServer() throws RemoteException {
        super();
    }

    @Override
    public String print(String fileName, String printer) throws RemoteException {
        return "From server" + fileName + " printed on " + printer;
    }
}
