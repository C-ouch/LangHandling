package com.skilledmonster.examples.util.map;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.*;
import java.util.*;

public class TextCorrect {

    /**
     *
     * @param currWord
     * @param hash
     * @return Fixed word else null *NEED TO CHECK IF RETURNED NULL*
     */
    public static String autoCorrect(String currWord, Multimap<String, String> hash){

        if(hash.containsKey(currWord))
            return null;

//        for (Map.Entry<String, String> entry : hash.entries()) {
//        Multimaps.asMap(hash).forEach((_key, valueCollection) ->
        for (String _key: hash.keySet()) {

//            System.out.println("Key: "+ _key+"\nValues: " + valueCollection + "\n");
//            String _key = entry.getKey();

            if(Math.abs(currWord.length()-_key.length()) > 2) {
//                System.out.println("HIT");
                continue;
            }
            else if(!currWord.substring(0,1).equals(_key.substring(0,1))
                    && !((currWord.substring(currWord.length() -1)).equals(_key.substring(_key.length() -1)))){
                continue;
            }
            else{
                Collection<String> valueCollection = hash.get(_key);
//                System.out.println("Values = " + _values + "\n");
                System.out.println("Key = " + _key );
                System.out.println("Values = " + valueCollection);

                System.out.println(valueCollection.contains(currWord)+ "\n");

                if(valueCollection.contains(currWord))
                    return _key;


//                System.out.println("Key = " + _key);
//                System.out.println("Values = " + _values + "\n");
            }


        }

        return "Can't find value";

    }
  /*while (scanner.hasNextLine()) {
//                System.out.println("Values = " + valueList + "\n");
                line = scanner.nextLine();
                if(line.equals("BREAK")) {
                    System.out.println("Break: "+ line+"\n");
                    break;
                }


                first = line.substring(0,1);


                if(first.equals("$")) {
                    // put values into map
                    if(!valueList.isEmpty() && !key.equals("")) {
                        System.out.println("Key = " + key);
                        System.out.println("Values = " + valueList + "\n");

                        hash.put(key, valueList);
                    }

                    key = "";
                    for (int n = 1; n < line.length(); n++) {
                        x = line.charAt(n);
                        key = key + x;
//                        System.out.println(x);

                    }
//                    System.out.println(valueList.size());

//                    System.out.println(key);

                    //reset list
                    valueList.clear();
                }else if (key.equals("zenith")){
                    // put values into map
                    if(valueList.contains("zeenith")) {
                        System.out.println("Key = " + key);
                        System.out.println("Values = " + valueList + "\n");

                        hash.put(key, valueList);
                        //reset list
                        valueList.clear();

                    }
                }else{
                    line = line; //need this for some reason for line to realize there values in it
//
//                    System.out.println("current Line:" +line);
                    valueList.add(line); // populate list
//                    System.out.println("Values = " + valueList + "\n");
                }
            }*/

/* Map<String, List<String>> hash = new HashMap<>();
            Scanner scanner = new Scanner(new FileReader("missp.txt"));
            String line = "";

            List<String> valueList = new ArrayList<>();

            String first ="" ;
            char x = 0;
            String key = "";
            String value;


            while (scanner.hasNextLine()) {
//                System.out.println("Values = " + valueList + "\n");
                line = scanner.nextLine();
//                System.out.println(line);

//                if(line.equals("BREAK")) {
//                    System.out.println("Break: "+ line+"\n");
//                    break;
//                }


                first = line.substring(0,1);


                if(!first.equals("$")) {
                    line = line; //need this for some reason for line to realize there values in it
//
//                    System.out.println("current Line:" +line);
                    valueList.add(line); // populate list
//                    System.out.println("Values = " + valueList + "\n");

                }else if (key.equals("zenith")){
                    // put values into map
                    if(valueList.contains("zeenith")) {
                        System.out.println("Key = " + key);
                        System.out.println("Values = " + valueList + "\n");

                        hash.put(key, valueList);
                        //reset list
                        valueList.clear();

                    }
                }else{

//                    if (key.equals("zenith"))
//                        System.out.println(true);
                    // put values into map
                    if(!valueList.isEmpty() && !key.equals("")) {
//                        System.out.println("Key = " + key);
//                        System.out.println("Values = " + valueList + "\n");

                        hash.put(key, valueList);

//                        System.out.println("Key = " +hash.get(key));
//                        System.out.println("Values = " + valueList + "\n");

                    }

                    key = "";
                    for (int n = 1; n < line.length(); n++) {
                        x = line.charAt(n);
                        key = key + x;
//                        System.out.println(x);

                    }
//                    System.out.println(valueList.size());

//                    System.out.println(key);

                    //reset list
                    valueList.clear();
                }
            }


            System.out.println(hash.size());
            try {
                // iterate and display values
                System.out.println("Fetching Keys and corresponding [Multiple] Values n");
//                for (Map.Entry<String, List<String>> entry : hash.entrySet()) {
//                    String _key = entry.getKey();
//                    List<String> _values = entry.getValue();
//                    System.out.println("Key = " + _key);
//                    System.out.println("Values = " + _values + "\n");
//                }
                for (var entry : hash.entrySet()) {
                    System.out.println(entry.getKey() + "/" + entry.getValue());
                }
            }catch(NullPointerException e){}

            scanner.close();

//            String returned = autoCorrect("col",hash);
//            if(returned == null)
//                System.out.println("Correct: Move cursor/don't change");
//            else
//                System.out.println(returned);


*/
    public static Multimap<String,String> Misspelled()  throws FileNotFoundException, IOException {
        Multimap<String, String> hash = ArrayListMultimap.create();
        try {

            Scanner scanner = new Scanner(new FileReader("missp.txt"));
            String line = "";

//            List<String> valueList = new ArrayList<>();

            String first ="" ;
            char x = 0;
            String key = "";


            while (scanner.hasNextLine()) {
//                System.out.println("Values = " + valueList + "\n");
                line = scanner.nextLine();
//                if(line.equals("BREAK")) {
//                    System.out.println("Break: "+ line+"\n");
//                    break;
//                }


                first = line.substring(0,1);


                if(first.equals("$")) {
                    // put values into map
                    /*if(!valueList.isEmpty() && !key.equals("")) {
                        System.out.println("Key = " + key);
                        System.out.println("Values = " + valueList + "\n");

                        hash.put(key, valueList);
                    }*/

                    key = "";
                    for (int n = 1; n < line.length(); n++) {
                        x = line.charAt(n);
                        key = key + x;
//                        System.out.println(x);

                    }
//                    System.out.println(valueList.size());
//                    System.out.println(key);

                    //reset list
//                    valueList.clear();
                }else{
                    line = line; //need this for some reason for line to realize there values in it

                    hash.put(key, line);
//
//                    System.out.println("current Line:" +line);
//                    valueList.add(line); // populate list
//                    System.out.println("Values = " + valueList + "\n");
                }
            }
//            Collection<String> values = hash.get();
//            System.out.println(values);

//            System.out.println(hash.size());
//            try {
                // iterate and display values

//                System.out.println("Fetching Keys and corresponding [Multiple] Values n");
//            Multimaps.asMap(hash).forEach((_key, valueCollection) ->System.out.println("Key: "+ _key+"\nValues: " + valueCollection + "\n"));

//            }catch(NullPointerException e){}

            scanner.close();



//            String returned = autoCorrect("cant",hash);
//            if(returned == null)
//                System.out.println("*Correct*: Move cursor/don't change");
//            else
//                System.out.println(returned);



        }catch(FileNotFoundException e){}catch (IOException e) {}
        return hash;
    }

}
