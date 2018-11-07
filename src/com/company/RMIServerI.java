package com.company;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerI extends Remote {
    String print(String fileName, String printer) throws RemoteException;
    String queue() throws RemoteException;
    String topQueue(int job) throws RemoteException;
    String start() throws RemoteException;
    String restart() throws RemoteException;
    String status()throws RemoteException;
    String readConfig(String parameter) throws RemoteException;
    String setConfig(String parameter, String value)throws RemoteException;
}
