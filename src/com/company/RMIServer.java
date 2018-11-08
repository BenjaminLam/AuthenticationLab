package com.company;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

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

    public byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return bytes;
    }

    public byte[] saltPassword(byte[] salt, String password){
        byte[] pwInBytes = Base64.getDecoder().decode(password);

        byte[] saltPlusPw = new byte[salt.length + pwInBytes.length];

        return saltPlusPw;
    }

    public boolean checkPassword(String userName, String password){
        //Get salt and hash from storage
        String[] saltAndHash = AuthenticatorFileReaderWriter.getPassword(userName);
        //Extract salt and hash from string from storage
        String salt = saltAndHash[1];
        byte[] saltInByteAr = Base64.getDecoder().decode(salt);
        String hashedPWFromStorage = saltAndHash[0];
        // season input password with salt
        byte[] saltedInputPW = saltPassword(saltInByteAr, password);
        //hash salted input password
        byte[] inputPwHashed = calculateHash(saltedInputPW);
        // encode to base64
        String inputPwHashedAsString = Base64.getEncoder().encodeToString(inputPwHashed);
        //compare the two hashes
        return inputPwHashedAsString.equals(hashedPWFromStorage);
    }

    private byte[] calculateHash(byte[] saltedInputPW) {
        byte[] result = null;

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(saltedInputPW);
            result = sha256.digest();
            return result;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean hashAndStorePassword(String userName, String password){
        try{
            // salt password
            byte[] salt = generateSalt();
            //prepare salt for storage by base64Decoding it
            String saltAsString = Base64.getEncoder().encodeToString(salt);
            //salt password
            byte[] saltedHashedPW = saltPassword(salt,password);
            //calculate hash of password
            byte[] hashedPwBytes = calculateHash(saltedHashedPW);
            //encode to string using base64
            String hashedPwString = Base64.getEncoder().encodeToString(hashedPwBytes);
            //store
            AuthenticatorFileReaderWriter.setPassword(userName,hashedPwString,saltAsString);
            return true;
        } //GLHF with debugging
        catch (Exception e)
        {
            return false;
        }
    }

}
