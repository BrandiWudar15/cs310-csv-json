package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;


public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results;
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
          
          JSONObject jsonObject = new JSONObject(); //Map
          JSONArray rowHeaders = new JSONArray(); //List
          JSONArray mainData = new JSONArray();
          JSONArray colHeaders = new JSONArray();
          
         
          for (String string : iterator.next()) {
              colHeaders.add(string);
          }
          
          while (iterator.hasNext()) { // Iterate through all records
              
              JSONArray rowData = new JSONArray(); // Create "rowData" list
                
                String[] rows = iterator.next();     // Get next row as string array
                rowHeaders.add(rows[0]);             // Add first element to "rowHeaders"
                
                // INSERT CODE HERE (convert elements; add to "mainData")
                for(int x = 1; x < rows.length; ++x)
                {  
                    rowData.add(Integer.parseInt(rows[x]));
                }
                
                
                
                mainData.add(rowData);
            
           }
          jsonObject.put("colHeaders", colHeaders); // Add column headers
          jsonObject.put("rowHeaders", rowHeaders); // Add row headers
          jsonObject.put("data", mainData);         // Add row data
            
          results = JSONValue.toJSONString(jsonObject);

        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
String results;
        
        try {
        
            // Create CSVWriter object, which will use a StringWriter to
            // encode the data, one row at a time, to CSV format.
        
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');

            // Parse JSON Data to a JSONObject map.
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            
            JSONArray colHeaders = (JSONArray)jsonObject.get("colHeaders");
            JSONArray rowHeaders = (JSONArray)jsonObject.get("rowHeaders");
            JSONArray data = (JSONArray)jsonObject.get("data");

            
            String[] colStringArray = new String[colHeaders.size()];
            String[] rowStringArray = new String[rowHeaders.size()];
            String[] dataStringArray = new String[data.size()];
            
            // First, get the column headers, copying each into the
            // "colStringArray" container.

            for (int i = 0; i < colHeaders.size(); i++){
                colStringArray[i] = colHeaders.get(i).toString();
            }
            
            // Next, output the column headers to the CSV writer.  (This
            // is all we need to do with the column headers.)
            
            csvWriter.writeNext(colStringArray);
            

            
            for (int i = 0; i < rowHeaders.size(); i++){
            
                // Get next row header
            
                rowStringArray[i] = rowHeaders.get(i).toString();
                
                // Get next set of row data
                
                dataStringArray[i] = data.get(i).toString();
                
            }

            for (int i = 0; i < dataStringArray.length; i++) {
                
                JSONArray dataValues = (JSONArray)parser.parse(dataStringArray[i]);


                String[] row = new String[dataValues.size()+1];

                
                row[0] = rowStringArray[i];
                row[1] = dataValues.get(0).toString();
                row[2] = dataValues.get(1).toString();
                row[3] = dataValues.get(2).toString();
                row[4] = dataValues.get(3).toString();
                
                csvWriter.writeNext(row);
                
            }
            
            // Output the completed CSV data to a string
            
            results = writer.toString();
            
        }
        
        catch(ParseException e) { return e.toString(); }
        
        return results.trim();
        
    }

}