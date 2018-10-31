package com.company;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerI extends Remote {
    public String print(String fileName, String printer) throws RemoteException;
}
