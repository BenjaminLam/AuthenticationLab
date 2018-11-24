package com.company;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.List;

//maybe we should hash information here
public class AccessControlListFileReaderWriter {
    //SHULD NOT BE CHANGED
    private static final String FILELOCATION = "accesscontrol.json";

    //returning whether user right was succesfully added
    public static boolean addUserRight(String username, Operation operation){
        JSONParser parser = new JSONParser();
        JSONObject jsonRoot;

        //read existing JSON object and check if password already exists
        try {
            Object obj = parser.parse(new FileReader(FILELOCATION));
            jsonRoot = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            return false;
        } catch (ParseException e) {
            jsonRoot = new JSONObject();
        } catch (IOException e){
            return false;
        }

        JSONArray allowedLabelsJsonArray;
        try {
            allowedLabelsJsonArray = (JSONArray) jsonRoot.get(username);
        } catch (Exception e){
            return false;
        }

        if (allowedLabelsJsonArray == null){
            allowedLabelsJsonArray = new JSONArray();
            jsonRoot.put(username,allowedLabelsJsonArray);
        }

        //Don't add label if it already exists
        if(allowedLabelsJsonArray.contains(operation.name()))
            return true;

        allowedLabelsJsonArray.add(operation.name());

        try {
            FileWriter filew = new FileWriter(FILELOCATION);
            filew.write(jsonRoot.toJSONString());
            filew.flush();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public static List<Operation> getUserRights(String username){
        List<Operation> allowedOperations = new ArrayList<Operation>();

        JSONParser parser = new JSONParser();
        JSONObject jsonRoot;

        //read existing JSON object and check if password already exists
        try {
            Object obj = parser.parse(new FileReader(FILELOCATION));
            jsonRoot = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            return allowedOperations;
        } catch (IOException | ParseException e) {
            return allowedOperations;
        }
        JSONArray allowedLabelsJsonArray;
        try {
            allowedLabelsJsonArray = (JSONArray) jsonRoot.get(username);
        } catch (Exception e){
            return allowedOperations;
        }

        for(Object alloweLabelJsonObject : allowedLabelsJsonArray) {
            try {
                allowedOperations.add(Operation.valueOf((String) alloweLabelJsonObject));
            } catch (Exception e){
                //ignore wrongly formatted enums
            }
        }

        return allowedOperations;
    }

    public static void resetAccessControlListFile(){
        File accessControlListFile = new File(FILELOCATION);

        try{ //clear file content
            PrintWriter writer = new PrintWriter(FILELOCATION);
            writer.print("");
            writer.close();
            return;
        } catch (FileNotFoundException e){

        }
        try {
            accessControlListFile.createNewFile();
        } catch (IOException e){
            //ignore
        }
    }
}
