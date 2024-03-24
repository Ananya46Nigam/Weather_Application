package com.example.weather_app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Controller
@RequestMapping("/")
public class weatherController {
    public static final String API_KEY = "fe49ed53c0f492edade575a08868fcb3";
    public static final String IPINFO_TOKEN = "38ecb221b8e3dd";
    public static final String IP_SERVICE_URL = "https://api.ipify.org?format=json";

    @GetMapping("/test")
    public String testing() {
        return "currentweather";
    }

    @GetMapping("/weather")
    public String getWeather(Model model) {
        // Obtain user's public IP address
        String ipAddress = getPublicIPAddress();
        System.out.println("\n\n\n" + ipAddress);

        if (ipAddress == null) {
            model.addAttribute("errorMessage", "Failed to retrieve public IP address.");
            return "error";
        }

        // Use public IP address to get location information
        Location location = getLocationInfo(ipAddress);

        if (location == null || location.getCity() == null) {
            model.addAttribute("errorMessage", "Failed to retrieve location information.");
            return "error";
        }

        // Extract city from location
        String city = location.getCity();
        System.out.print(city);
        // Construct API URL for weather
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;

        try {
            // Make the API call and add response to model
            ResponseEntity<Response> responseEntity = new RestTemplate().getForEntity(apiUrl, Response.class);
//            System.out.println("\n"+responseEntity);
//            System.out.println("\n"+responseEntity.getBody());
            model.addAttribute("weatherData", responseEntity.getBody());
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "An error occurred: " + ex.getMessage());
            return "error";
        }

        return "weather";
    }

    // Method to retrieve public IP address using an external service
    private String getPublicIPAddress() {
        try {
            ResponseEntity<Map> responseEntity = new RestTemplate().getForEntity(IP_SERVICE_URL, Map.class);
            Map<String, String> responseBody = responseEntity.getBody();
            if (responseBody != null) {
                return responseBody.get("ip");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to fetch location information based on IP address
    private Location getLocationInfo(String ipAddress) {
        String apiUrl = "https://ipinfo.io/" + ipAddress + "/json?token=" + IPINFO_TOKEN;
        try {
            ResponseEntity<Location> responseEntity = new RestTemplate().getForEntity(apiUrl, Location.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}