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
    private static final String ACCESSCONTROLERRMSG = "You are not authorized for this action";

    IPolicyDecisionMaker decisionMaker;

    protected RMIServer(IPolicyDecisionMaker decisionMaker) throws RemoteException {
        super();
        this.decisionMaker = decisionMaker;
        //decisionMaker.reset();
        //resetAllPasswords();
    }

    @Override
    public String print(String fileName, String printer, String userName, String inputPassword) throws RemoteException {
        if(!checkPassword(userName, inputPassword)) return "Credentials were incorrect";
        if(!decisionMaker.isAllowedAccess(userName,Operation.PRINT)) return ACCESSCONTROLERRMSG;
        jobs.add(fileName);
        return "From server" + fileName + " printed on " + printer;
    }

    @Override
    public String queue(String userName, String inputPassword) {
        if(!checkPassword(userName, inputPassword)) return "Credentials were incorrect";
        if(!decisionMaker.isAllowedAccess(userName,Operation.QUEUE)) return ACCESSCONTROLERRMSG;
        String result = "";
        for(int counter = 0;counter<jobs.size();counter++){
            result += counter + " " + jobs.get(counter) + "\n";
        }
        return result;
    }

    @Override
    public String topQueue(int job, String userName, String inputPassword) {
        if(!checkPassword(userName, inputPassword)) return "Credentials were incorrect";
        if(!decisionMaker.isAllowedAccess(userName,Operation.TOPQUEUE)) return ACCESSCONTROLERRMSG;
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
    public String start(String userName, String inputPassword) {
        if(!checkPassword(userName, inputPassword)) return "Credentials were incorrect";
        if(!decisionMaker.isAllowedAccess(userName,Operation.START)) return ACCESSCONTROLERRMSG;
        isRunning = true;
        return "Started printing";
    }

    @Override
    public String stop(String userName, String inputPassword) {
        if(!checkPassword(userName, inputPassword)) return "Credentials were incorrect";
        if(!decisionMaker.isAllowedAccess(userName,Operation.STOP)) return ACCESSCONTROLERRMSG;
        isRunning = false;
        return "Stopped printing";
    }

    @Override
    public String restart(String userName, String inputPassword) {
        if(!checkPassword(userName, inputPassword)) return "Credentials were incorrect";
        if(!decisionMaker.isAllowedAccess(userName,Operation.RESTART)) return ACCESSCONTROLERRMSG;
        isRunning=false;
        jobs.clear();
        isRunning=true;
        return "Printer was successfully restarted";
    }

    @Override
    public String status(String userName, String inputPassword) {
        if(!checkPassword(userName, inputPassword)) return "Credentials were incorrect";
        if(!decisionMaker.isAllowedAccess(userName,Operation.STATUS)) return ACCESSCONTROLERRMSG;

        if(isRunning) return "Printer is printing";
        else return "Printer is idle";
    }

    @Override
    public String readConfig(String parameter, String userName, String inputPassword) {
        if(!checkPassword(userName, inputPassword)) return "Credentials were incorrect";
        if(!decisionMaker.isAllowedAccess(userName,Operation.READCONFIG)) return ACCESSCONTROLERRMSG;
        if(configurations.containsKey(parameter)){
            return configurations.get(parameter);
        }
        return "Configuration did not exist";
    }

    @Override
    public String setConfig(String parameter, String value, String userName, String inputPassword) {
        if(!checkPassword(userName, inputPassword)) return "Credentials were incorrect";
        if(!decisionMaker.isAllowedAccess(userName,Operation.SETCONFIG)) return ACCESSCONTROLERRMSG;
        configurations.put(parameter,value);
        return "Configuration for " + parameter + " was added";
    }

    @Override
    public String changePassword(String username, String oldPassword ,String newPassWord){
        if(!checkPassword(username,oldPassword)) //Password were wrong or user name did not excist
            return "Credentials were not correct";
        if(!decisionMaker.isAllowedAccess(username,Operation.START))
            return ACCESSCONTROLERRMSG;

        hashAndStorePassword(username,newPassWord);
        return "Password were changed";
    }

    // Generating salt by using a Secure Random number generator as built in java lib
    // returns a 32 byte array of randomness
    public byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return bytes;
    }


    //Auxiallry function to salt the password. It should be provided with a random generated salt by
    // calling the generate salt function.
    private byte[] saltPassword(byte[] salt, String password){
        byte[] pwInBytes = password.getBytes();
        // create a new byte array of length salt + password
        byte[] saltPlusPw = new byte[salt.length + pwInBytes.length];

        //add the salt to the beginning of the new array
        for(int i = 0; i<salt.length; i++){
            saltPlusPw[i]=salt[i];
        }
        //add the password after the salt
        for(int i = 0; i<pwInBytes.length;i++){
            saltPlusPw[i+salt.length]=pwInBytes[i];
        }
        System.out.println("saltPlusPW: " + Base64.getEncoder().encodeToString(saltPlusPw));
        return saltPlusPw;
    }

    private boolean checkPassword(String userName, String password){
        //Get salt and hash from storage
        String[] saltAndHash = AuthenticatorFileReaderWriter.getPassword(userName);

        if(saltAndHash == null)
            return false;

        //Extract salt and hash from string from storage
        String salt = saltAndHash[1];
        System.out.println("salt from check : " + salt);
        //Decode the salt string with base64decoder
        byte[] saltInByteAr = Base64.getDecoder().decode(salt);
        String hashedPWFromStorage = saltAndHash[0];
        // season input password with salt
        byte[] saltedInputPW = saltPassword(saltInByteAr, password);
        //hash salted input password
        byte[] inputPwHashed = calculateHash(saltedInputPW);
        // encode to base64
        String inputPwHashedAsString = Base64.getEncoder().encodeToString(inputPwHashed);
        System.out.println("Input password hashed: " + inputPwHashedAsString);
        System.out.println("PW from stoage: " + hashedPWFromStorage);
        //compare the two hashes and the return the boolean value
        return inputPwHashedAsString.equals(hashedPWFromStorage);
    }

    private byte[] calculateHash(byte[] saltedInputPW) {
        byte[] result = null;

        try {
            //find the hash function with a default provicer
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            //update the digest with input
            sha256.update(saltedInputPW);
            // digest the input and return result
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
            System.out.println("hashed password from storage: " + hashedPwString);
            System.out.println("salt" + saltAsString);
            //store the password and the salt in storage file using the AutenticationFileReaderWriter
            AuthenticatorFileReaderWriter.setPassword(userName,hashedPwString,saltAsString);
            return true;
        } //GLHF with debugging
        catch (Exception e)
        {
            return false;
        }
    }

    private void resetAllPasswords() {
        AuthenticatorFileReaderWriter.resetPasswordFile();
        String[] users = new String[]{
                "Alice", //manager
                "Bob", //janitor
                "Cecilia", //power user
                "David", //user
                "Erica", //user
                "Fred", //user
                "George" //user
        };
        for (String user : users)
            hashAndStorePassword(user,"QWERTY123");
    }

    private void resetDecisionMaker(){
        decisionMaker.reset();
    }
}
