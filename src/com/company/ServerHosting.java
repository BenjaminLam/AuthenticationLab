package com.company;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerHosting {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("print", new RMIServer(new RoleBasedDecisionMaker()));
    }
}
