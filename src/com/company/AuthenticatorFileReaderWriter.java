package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class AuthenticatorFileReaderWriter {
    //SHULD NOT BE CHANGED
    public static final String FILELOCATION = "credentials.json";
    //SHULD NOT BE CHANGED
    public static final String JSONPASSWORDKEY = "pass";
    //SHULD NOT BE CHANGED
    public static final String JSONSALTKEY = "salt";

    //Sets (if user exists) or creates (if user doesn't already exist) password/salt for a user
    //returning boolean indicating if operation was handled succesfully
    //If method returns false, no password was changed/created
    public static boolean setPassword(String userName, String hashedPassword, String salt){
        JSONParser parser = new JSONParser();
        JSONObject jsonRoot;

        //read existing JSON object and check if password already exists
        try {
            Object obj = parser.parse(new FileReader(FILELOCATION));
            jsonRoot = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            //If file didn't exists create new root:
            jsonRoot = new JSONObject();
        } catch (IOException | ParseException e) {
            //for now we just overwrite json object:
            jsonRoot = new JSONObject();

            //later we should change to this, so we don't overwrite db of passwords due to some error
            //return false;
        }

        //Create new JSON object with username as key and another new JSON object as value
        //Put hashed password and salt into the other new JSON object and persist it
        try {
            JSONObject jsonPasswordObject = new JSONObject();
            jsonPasswordObject.put(JSONPASSWORDKEY, hashedPassword);
            jsonPasswordObject.put(JSONSALTKEY, salt);

            jsonRoot.put(userName, jsonPasswordObject);

            FileWriter file = new FileWriter(FILELOCATION);
            file.write(jsonRoot.toJSONString());
            file.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //On succes method returns string array of length 2 containing: 0: hashed password, 1: salt
    //On any error the method reutns null (GLHF with debug :-) )
    public static String[] getPassword(String userName){
        String[] result = new String[2];
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(FILELOCATION));
            JSONObject jsonRoot = (JSONObject) obj;

            JSONObject jsonPasswordObject = (JSONObject) jsonRoot.get(userName);
            if (jsonPasswordObject == null)  return null; //user doesn't exist

            String hashedPassword = (String) jsonPasswordObject.get(JSONPASSWORDKEY);
            String salt = (String) jsonPasswordObject.get(JSONSALTKEY);

            result[0]=hashedPassword;
            result[1]=salt;
        } catch (ParseException|IOException e) {
            return null; //could not read JSONFILE
        }
        return result;
    }




}
