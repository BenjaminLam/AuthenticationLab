package com.company;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerI extends Remote {
    public String print(String fileName, String printer) throws RemoteException;
    public String queue();
    public String topQueue(int job);
    public String start();
    public String restart();
    public String status();
    public String readConfig(String parameter);
    public String setConfig(String parameter, String value);
}
