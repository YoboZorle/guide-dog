package com.hack.guidedog.info;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.format.Time;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LocationWeather {

    Context context;
    GoogleApiClient mLocationClient;
    double lat;
    double lng;

    public LocationWeather(Context c, GoogleApiClient client) {
        context = c;
        mLocationClient = client;
    }

    public String getLocation() {

        if (mLocationClient != null) {

            Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);

            if (mCurrentLocation == null) {
                return "";
            }

            lat = mCurrentLocation.getLatitude();
            lng = mCurrentLocation.getLongitude();
            System.out.println(lat + "  ,  " + lng);
            if (Geocoder.isPresent()) {
                try {
                    Geocoder gcd = new Geocoder(context, Locale.US);
                    List<Address> addresses = null;
                    addresses = gcd.getFromLocation(lat, lng, 1);
                    System.out.println("done");
                    StringBuilder s = new StringBuilder();
                    if (addresses.size() > 0)
                        System.out.println("locality: "
                                + addresses.get(0).getLocality());
                    System.out.println("Admin area: "
                            + addresses.get(0).getAdminArea());
                    System.out.println("postal code: "
                            + addresses.get(0).getPostalCode());
                    System.out.println("sub locality: "
                            + addresses.get(0).getSubLocality());
                    System.out.println("premises: "
                            + addresses.get(0).getPremises());
                    System.out.println("thoroughfare: "
                            + addresses.get(0).getThoroughfare());
                    System.out.println("SubAdminArea: "
                            + addresses.get(0).getSubAdminArea());
                    System.out.println("SubThroroughfare: "
                            + addresses.get(0).getSubThoroughfare());
                    System.out.println("FeatureName: "
                            + addresses.get(0).getFeatureName());
                    System.out.println("toString: "
                            + addresses.get(0).toString());
                    System.out.println("\n\n\n");
                    s.append(addresses.get(0).getSubThoroughfare() + " ");
                    s.append(addresses.get(0).getThoroughfare() + " ");
                    s.append(addresses.get(0).getSubAdminArea() + " ");
                    s.append(addresses.get(0).getAdminArea() + " ");
                    s.append(addresses.get(0).getPremises() + " ");
                    s.append(addresses.get(0).getLocality());
                    return s.toString();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("Location not available");
                    return "Error: Location not available";
                }
            } else {
                return "Error: Location not available";
            }

        } else {
            return "Error: Location not available";
        }
    }

    public String getWeather() {
        try {
            URL url = new URL(
                    "http://api.worldweatheronline.com/free/v1/weather.ashx?key=nu5h5purxrw9g7qyjjc87ys8&q="
                            + lat + "," + lng + "&format=xml");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            // System.out.println("\nSending 'GET' request to URL : " + url);
            // System.out.println("Response Code : " + responseCode);
            String s = readStream(con.getInputStream());
            System.out.println(s);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return ("Error: Weather not available");
        }
    }

    public String readStream(InputStream is) {
        BufferedReader in = null;
        String temp = null;
        String description = null;
        String result;
        try {
            in = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            if (response.toString().contains("<current_condition>")) {
                String line = response.substring(
                        response.indexOf("<current_condition>"),
                        response.indexOf("</current_condition>"));

                String tempstart = "<temp_C>";
                String tempend = "</temp_C>";
                int j = line.indexOf(tempstart);
                int j1 = line.indexOf(tempend);
                if (j != -1 && j1 != -1)
                    temp = line.substring(j + tempstart.length(), j1);
                String weatherstart = "<weatherDesc><![CDATA[";
                String weatherend = "]]></weatherDesc>";
                int n = line.indexOf(weatherstart);
                int n1 = line.indexOf(weatherend);
                if (n != -1 && n1 != -1)
                    description = line.substring(n + weatherstart.length(), n1);

                result = "The temperature is " + temp
                        + " degrees Celcius and it is " + description;
            } else {
                result = "Error: Weather not available";
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "Error: Weather not available";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public String getDate() {
        String result = null;
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.DAY_OF_MONTH) + "-"
                + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR);
        String time = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        System.out.println(date + "      " + time + "\n\n\n");

        Time now = new Time();
        now.setToNow();

        StringBuilder s = new StringBuilder();
        s.append(now.hour + " ");
        s.append(now.minute + " ");
        switch (now.weekDay) {
            case 0:
                s.append("Sunday" + " ");
                break;
            case 1:
                s.append("Monday" + " ");
                break;
            case 2:
                s.append("Tuesday" + " ");
                break;
            case 3:
                s.append("Wednesday" + " ");
                break;
            case 4:
                s.append("Thursday" + " ");
                break;
            case 5:
                s.append("Friday" + " ");
                break;
            case 6:
                s.append("Saturday" + " ");
        }

        return s.toString();
    }
}
