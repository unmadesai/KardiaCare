package com.example.unmadesai.kardiacare4;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.example.unmadesai.kardiacare4.GetNearbyPlacesData.latLng;
import static com.example.unmadesai.kardiacare4.GetNearbyPlacesData.place_id;

/**
 * Created by Hitesh on 21-02-2018.
 */

public class FetchData extends AsyncTask<Void, Void, String> {
    public AsyncResponse delegate=null;
    @Override
    protected String doInBackground(Void... voids) {
        double lat=latLng.latitude;
        double longi=latLng.longitude;
        //Context ctx=this;
        //JSONObject ret = getLocationInfo(lat, longi);
        JSONObject locations,phonenumber,pnumber;
        String location_string="";
        String myAdress[];
           String phone="";
         //String phn=phone;

        Log.d("Got the Object", "will try to get the address from it");
        /*try {
            //Get JSON Array called "results" and then get the 0th complete object as JSON
            locations = ret.getJSONArray("results").getJSONObject(0);
            // Get the value of the attribute whose name is "formatted_string"
            location_string = locations.getString("formatted_address");
            Log.d("test", "formattted address:" + location_string);
        } catch (JSONException e1) {
            e1.printStackTrace();
        } */try {
            Log.d("reached here","will try to get phone number now");
            phonenumber = getphoneNumber(location_string);
           // pnumber=phonenumber.getJSONArray("result").getJSONObject(0);

            /*for (int i = 0; i < pnumber.length; i++) {
                myAddress[i] = myJSONResult.results[i].formatted_address;
            }*/
            /*JSONArray results=phonenumber.getJSONArray("result");
            for(int i=0;i<results.length();i++){
                //myAdress[i]=phonenumber.results[i].formatted_adrress;
            }*/
            JSONObject jObjectresult=(JSONObject) phonenumber.get("result");
            phone=jObjectresult.getString("international_phone_number");
            Log.d("got phone number",""+phone);

            //Toast.makeText(,""+phone,Toast.LENGTH_SHORT).show();
            //Intent i = new Intent(this,GetNearbyPlacesData.class);
        }catch (JSONException e2){
            e2.printStackTrace();
        }
        if(phone==""){
            //GetNearbyPlacesData nb=new GetNearbyPlacesData();
            //nb.deletekey();
            //return null;
            onPostExecute(null);
        }


        return phone;
    }

    @Override
    protected void onPostExecute(String s) {
            delegate.processFinish(s);
    }

    /*public JSONObject getLocationInfo(double lat, double lng) {
                Log.d("getLocationInfo","Hope to get the Jason Object");
                HttpGet httpGet = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat +","+lng+"&key=AIzaSyCdN3jfae-UEM--haICu5vma_3pwF6fKro");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response;
                StringBuilder stringBuilder = new StringBuilder();

                try {
                    response = client.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    InputStream stream = entity.getContent();
                    int b;
                    while ((b = stream.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(stringBuilder.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject;
            }*/
    public JSONObject getphoneNumber(String locationString){
        HttpGet httpGet = new HttpGet("https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key=AIzaSyAxVo4hlgESbcpYI2HcXCUVblWuHL3m1VI");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("in getphonenumber","will return the number");
        return jsonObject;
    }

}
