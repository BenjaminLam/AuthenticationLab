package com.company;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {
    //index in jobs array will be the jobnumber for each job.
    ArrayList<String> jobs = new ArrayList<String>();
    Map<String,String> configurations = new HashMap<String, String>();

    boolean isRunning = false;


    protected RMIServer() throws RemoteException {
        super();
    }

    @Override
    public String print(String fileName, String printer) throws RemoteException {
        jobs.add(fileName);
        AuthenticatorFileReaderWriter.getPassword("hej");
        return "From server" + fileName + " printed on " + printer;
    }

    @Override
    public String queue() {
        String result = "";
        for(int counter = 0;counter<jobs.size();counter++){
            result += counter + " " + jobs.get(counter) + "\n";
        }
        return result;
    }

    @Override
    public String topQueue(int job) {
        String temp = jobs.get(job);
        ArrayList<String> tempJobs = jobs;
        tempJobs.remove(job);
        jobs.clear();
        jobs.add(temp);
        for (String jobToBeAdded : tempJobs) {
            jobs.add(jobToBeAdded);
        }
        return "Jobs was successfully moved to the top of the queue";
    }

    @Override
    public String start() {
        isRunning = true;
        return "Started printing";
    }

    @Override
    public String restart() {
        isRunning=false;
        jobs.clear();
        isRunning=true;
        return "Printer was successfully restarted";
    }

    @Override
    public String status() {
        AuthenticatorFileReaderWriter.setPassword("martin","12345","abc");
        AuthenticatorFileReaderWriter.setPassword("benjamin","123","def");

        //example of error handling:
        if(!AuthenticatorFileReaderWriter.setPassword("mikkel","456","ghi")) {
            //some error handling (password was not set/created
        }
        String[] res;
        String password;
        String salt;


        res = AuthenticatorFileReaderWriter.getPassword("martin");
        if (res == null){
            password = "pass err";
            salt = "salt err";
        } else {
            password = res[0];
            salt = res[1];
        }
        System.out.println("martin password: " + password +", martin salt: " + salt);


        res = AuthenticatorFileReaderWriter.getPassword("benjamin");
        if (res == null){
            password = "pass err";
            salt = "salt err";
        } else {
            password = res[0];
            salt = res[1];
        }
        System.out.println("benjamin password: " + password +", benjamin salt: " + salt);

        res = AuthenticatorFileReaderWriter.getPassword("mikkel");
        if (res == null){
            password = "pass err";
            salt = "salt err";
        } else {
            password = res[0];
            salt = res[1];
        }
        System.out.println("mikkel password: " + password +", mikkel salt: " + salt);


        res = AuthenticatorFileReaderWriter.getPassword("hitler-jesus");
        if (res == null){
            password = "pass err";
            salt = "salt err";
        } else {
            password = res[0];
            salt = res[1];
        }
        System.out.println("hitler-jesus password: " + password +", hitler-jesus salt: " + salt);

        // have you ever asked if you love or hate hitler-jesus?

        return "";

        //if(isRunning) return "Printer is printing";
        //else return "Printer is idle";
    }

    @Override
    public String readConfig(String parameter) {
        if(configurations.containsKey(parameter)){
            return configurations.get(parameter);
        }
        return "Configuration did not exist";
    }

    @Override
    public String setConfig(String parameter, String value) {
        configurations.put(parameter,value);
        return "Configuration for " + parameter + " was added";
    }
}
