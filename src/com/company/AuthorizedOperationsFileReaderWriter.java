package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorizedOperationsFileReaderWriter {
    //SHULD NOT BE CHANGED
    private static final String FILELOCATION = "authorizedoperationss.json";

    //returning whether user right was succesfully added
    public static boolean addRoleOperation(Role role, Operation operation){
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

        JSONArray AuthorizedOperationsJsonArray;
        try {
            AuthorizedOperationsJsonArray = (JSONArray) jsonRoot.get(role.name());
        } catch (Exception e){
            return false;
        }

        if (AuthorizedOperationsJsonArray == null){
            AuthorizedOperationsJsonArray = new JSONArray();
            jsonRoot.put(role.name(),AuthorizedOperationsJsonArray);
        }

        //Don't add label if it already exists
        if(AuthorizedOperationsJsonArray.contains(operation.name()))
            return true;

        AuthorizedOperationsJsonArray.add(operation.name());

        try {
            FileWriter filew = new FileWriter(FILELOCATION);
            filew.write(jsonRoot.toJSONString());
            filew.flush();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public static List<Role> getUserRoles(String username){
        List<Role> allowedRoles = new ArrayList<Role>();

        JSONParser parser = new JSONParser();
        JSONObject jsonRoot;

        //read existing JSON object and check if password already exists
        try {
            Object obj = parser.parse(new FileReader(FILELOCATION));
            jsonRoot = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            return allowedRoles;
        } catch (IOException | ParseException e) {
            return allowedRoles;
        }
        JSONArray allowedRolesJsonArray;
        try {
            allowedRolesJsonArray = (JSONArray) jsonRoot.get(username);
        } catch (Exception e){
            return allowedRoles;
        }

        for(Object alloweLabelJsonObject : allowedRolesJsonArray) {
            try {
                allowedRoles.add(Role.valueOf((String) alloweLabelJsonObject));
            } catch (Exception e){
                //ignore wrongly formatted enums
            }
        }

        return allowedRoles;
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
