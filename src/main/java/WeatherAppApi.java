import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherAppApi {
    //fetch weather data
    public static JSONObject getWeatherData(String locationName){
    //get location coordinates using geolocation API
        JSONArray locationData=getLocationData(locationName);
    //extract latitude and longitude
    JSONObject location=(JSONObject)locationData.get(0);
    double latitude=(Double)location.get("latitude");
    double longitude=(Double)location.get("longitude");
    //build API request for coordinates
        String urlString="https://api.open-meteo.com/v1/forecast?" + "latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=auto";
        try{
            HttpURLConnection connection=fetchApi(urlString);
            if(connection.getResponseCode()!=200){
                System.out.println("Error:Could not find connection");
                return null;

            }
            StringBuilder resultJson=new StringBuilder();
            Scanner scanner=new Scanner(connection.getInputStream());
            while(scanner.hasNext()){
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            connection.disconnect();
            JSONParser parser=new JSONParser();
            JSONObject resultJsonObj=(JSONObject)parser.parse(String.valueOf(resultJson));

            //hour
            JSONObject hourly=(JSONObject)resultJsonObj.get("hourly");
            JSONArray time=(JSONArray)hourly.get("time");
            int index=findCurrentIndexOfTime(time);

            JSONArray temperatureDate=(JSONArray)hourly.get("temperature_2m");
            double temperature=(double)temperatureDate.get(index);

            JSONArray weatherCode=(JSONArray)hourly.get("weather_code");
            String weatherCondition=convertWeatherCode((long)weatherCode.get(index));

            JSONArray relativeHumidity=(JSONArray)hourly.get("relative_humidity_2m");
            long humidity=(long)relativeHumidity.get(index);

            JSONArray windSpeed=(JSONArray)hourly.get("wind_speed_10m");
            double wind=(double)windSpeed.get(index);

            JSONObject weatherData=new JSONObject();
            weatherData.put("temperature",temperature);
            weatherData.put("weatherCondition",weatherCondition);
            weatherData.put("humidity",humidity);
            weatherData.put("wind",wind);
            return weatherData;
        }catch(Exception e){
            e.printStackTrace();

        }
        return null;
    }
    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");

        // Build API URL with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";


        try {
            // Call API
            HttpURLConnection connection = fetchApi(urlString);
            int responseCode = connection.getResponseCode();


            if (responseCode != 200) {
                System.out.println("Error: HTTP request failed with response code " + responseCode);
            } else {
                // Store API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                // Read and store the resulting data
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                // Close scanner
                scanner.close();

                // Close URL connection
                connection.disconnect();

                // Parse the JSON string into a JSON object
                JSONParser parser = new JSONParser();
                JSONObject results = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONArray locationData = (JSONArray) results.get("results");
                return locationData;
            }

        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions
        }
        return null;
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
    private static int findCurrentIndexOfTime(JSONArray timeList){
        String currentTime=getCurrentTime();
        for(int i=0;i<timeList.size();i++){
            String time=(String)timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                return i;

            }
        }
       return 0;
    }
 public static String getCurrentTime(){
        //get current date and time
        LocalDateTime currentDateTime=LocalDateTime.now();
        //data format
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formatedDateTime=currentDateTime.format(formatter);
        return formatedDateTime;
    }

    public static String convertWeatherCode(long weatherCode){
        String weatherCondition="";
        if(weatherCode==0){
            weatherCondition="Clear";
        }else if(weatherCode>0&&weatherCode<=3){
            weatherCondition="Cloudy";

        }else if((weatherCode>=51&&weatherCode<=67)||(weatherCode>=80&&weatherCode<=99)){
            weatherCondition="Rain";
        }
        else if(weatherCode>=71&&weatherCode<=77){
            weatherCondition="Snow";
        }
return weatherCondition;

    }
}
