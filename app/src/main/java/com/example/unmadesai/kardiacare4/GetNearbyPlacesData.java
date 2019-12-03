package com.example.unmadesai.kardiacare4;

/**
 * Created by unma desai on 04-Nov-17.
 */


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//import static android.support.v4.content.ContextCompatJellybean.startActivity;

/**
 * @author Priyanka
 */

public class GetNearbyPlacesData  extends AsyncTask<Object, String, String>  implements AsyncResponse {

    private String googlePlacesData;
    private GoogleMap mMap;
    String url;

    private RequestQueue requestQueue;
    Location location;
    LatLng loc;
    LatLng latLng1;
    RequestQueue requestqueue;
    Geocoder geocoder;
    List<Address> addresses;
    public static LatLng latLng;
    Double lat1, lng1;
    Double lat2, lang2;
    Button b2;
    Button b1;
    public static double lati;
    public static double longi;
    public static String place_id;
    public String Name;
    public String vic;
    public static String result;
    HashMap<Double, Object> storesDistance;
    TreeMap<Double, Object> treeMap;
    TreeMap<Double, Object> tree;
    List<HashMap<String, String>> nearbyPlaceList;
    double key;
    //key = treeMap.firstKey();
    GetNearbyPlacesData ctx=this;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {

        //List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        Log.d("nearbyplacesdata", "called parse method");
        try {
            showNearbyPlaces(nearbyPlaceList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) throws IOException {

        //LatLng objLatLng=getIntent().getExtras().getParcelable("Latlng");
        //lat1 = MapsActivity.location.getLatitude();
        //lng1 = MapsActivity.location.getLongitude();
        //Log.d("Current location",Double.toString(lat1));
        //Log.d("Current location",Double.toString(lng1));
        //LatLng latLng1 = new LatLng( lat1, lng1);

        double arr[] = new double[nearbyPlaceList.size()];
        double latarr[] = new double[nearbyPlaceList.size()];
        double lngarr[] = new double[nearbyPlaceList.size()];
        //hashmap which stores the distance
         storesDistance = new HashMap<Double, Object>();
        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            arr[i] = distance(MapsActivity.latLng0, latLng);
            Log.d("Reached here people", "will put things in hashmap after this");
            latarr[i] = latLng.latitude;
            lngarr[i] = latLng.longitude;
            storesDistance.put(arr[i], latLng);
            Log.d("after hashmap", "reached here");
            //Log.d("distanceeeeeeee",Float.toString(arr[i]));
        }

        printMap(storesDistance);
        Log.d("After getting distance", "Sorting the distance");
        //define treemap
         treeMap = new TreeMap<Double, Object>(storesDistance);
        String hospitals= treeMap.toString();
         Bundle hosp=new Bundle();

        hosp.putString("hosp",hospitals);
        printMap(treeMap);
        //printMap(googlePlace);
        getDetails();
        // TeleorPhone();
        //sendsms.performClick();
    }

    public static <K, V> void printMap(Map<K, V> map) {
        Log.d("reached in print", "will print everything now");
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Log.d("Distance", entry.getKey().toString());
            LatLng latlng = (LatLng) entry.getValue();

        }

    }

    //public static <K,V> latLng getlatlng()
    private double distance(LatLng current, LatLng last) {
        if (current == null) {
            Log.d("latest", "zeroooooooo");
            return 0;


        }


        int Radius = 6371;
        double dLat = Math.toRadians(last.latitude - current.latitude);
        double dLon = Math.toRadians(last.longitude - current.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(current.latitude))
                * Math.cos(Math.toRadians(last.latitude)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;


    }

    @Override
    public void processFinish(String Output) {
        result = Output;
        char num='1' ;
        Log.d("in processFinish", "" + result);
        //return result;
        Log.d("in teleorphone", "" + result);
        if (result != null) {
            try {
                 num = result.charAt(4);
                //char nine='9';
                while (num != '9') {
                    Log.d("number",""+num);
                    if (result != null) {
                        if (!treeMap.isEmpty()) {
                            treeMap.remove(key);
                            getDetails();
                        } else {
                            Log.d("in processFinish", "treemap isemepty");
                        }
                        //Log.d("in result=null", "didnt get the phone number");
                        return;
                    }
                }
            }catch (StringIndexOutOfBoundsException e){
                e.printStackTrace();
            }
            catch(NullPointerException n){
                n.printStackTrace();
            }
            if(num=='9'){
            Log.d("got phone number","stop"+result);
                //Handler mhandler;

                //Toast.makeText(,"LOGIN SUCCESSFUL----\n Welcome"+Name, Toast.LENGTH_LONG).show();
            return;
            }
        }
        //if(num==9){
        //Log.d("in num==9","got phone number with 9");
        //}
        else {
            Log.d("in else part of finish","did not get the phone number");
            treeMap.remove(key);
            getDetails();
            return;
        }
    }


    public void  getDetails() {
        key = treeMap.firstKey();
        //LatLng latlng = new LatLng(lat2, lang2);
        //latlng=getlatlng(treemap);
        latLng = (LatLng) treeMap.get(key);
        lati = latLng.latitude;
        longi = latLng.longitude;
        Log.d("Latitude", "" + lati);
        Log.d("Longitude", "" + longi);
        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            if (longi == lng && lati == lat) {
                place_id = googlePlace.get("place_id");
                Log.d("place id is", place_id);
                Name = googlePlace.get("place_name");
                Log.d("place name", "" + Name);
                vic = googlePlace.get("vicinity");
                Log.d("vicinity", "" + vic);
            }
        }
        FetchData f = new FetchData();
        f.delegate = this;
        f.execute();


    }
   // public void TeleorPhone(){
   public void deletekey() {
       try {
           key=treeMap.firstKey();
           treeMap.remove(key);
           getDetails();
       }catch(NullPointerException n){
           n.printStackTrace();
       }

   }

}