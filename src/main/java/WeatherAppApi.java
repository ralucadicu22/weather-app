import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherAppApi {
    //fetch weather data
    public static JSONObject getWeatherData(String locationName){
    //get location coordinates using geolocation API
        JSONArray locationData=getLocationData(locationName);
return null;
    }
    public static JSONArray getLocationData(String locationName){
        locationName=locationName.replaceAll("","+");

        //build API url with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";
        try{
            //call API
            HttpURLConnection connection=fetchApi(urlString);
            if(connection.getResponseCode()!=200){
                System.out.println("Error");
                return null;
            }else{
                //store API results
                StringBuilder resultJson=new StringBuilder();
                Scanner scanner=new Scanner(connection.getInputStream());
                //read and store the resulting data
                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }
                //close scanner
                scanner.close();

                //close url connection
                connection.disconnect();

                //parse the JSON string into a JSON obj
                JSONParser parser=new JSONParser();
                JSONObject results=(JSONObject)parser.parse(String.valueOf(resultJson));

                JSONArray locationData=(JSONArray)results.get("results");
                return locationData;
            }

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
    private static HttpURLConnection fetchApi(String urlString){
        try{
            URL url=new URL(urlString);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return connection;

        }catch(IOException e){
            e.printStackTrace();
            return null;

        }

    }
}
