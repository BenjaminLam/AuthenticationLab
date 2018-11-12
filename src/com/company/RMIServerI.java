package com.company;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerI extends Remote {
    String print(String fileName, String printer, String userName, String password) throws RemoteException;
    String queue(String userName, String password) throws RemoteException;
    String topQueue(int job, String userName, String password) throws RemoteException;
    String start(String userName, String password) throws RemoteException;
    String restart(String userName, String password) throws RemoteException;
    String status(String userName, String password)throws RemoteException;
    String readConfig(String parameter, String userName, String password) throws RemoteException;
    String setConfig(String parameter, String value, String userName, String password)throws RemoteException;
    String changePassword(String username, String oldPassword, String newPassWord);
}
