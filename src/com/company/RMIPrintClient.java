package com.company;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIPrintClient {

    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
        RMIServerI service = (RMIServerI) Naming.lookup("rmi://localhost:5099/print");
        System.out.println("---" + service.print("hey server", "3"));
    }
}
