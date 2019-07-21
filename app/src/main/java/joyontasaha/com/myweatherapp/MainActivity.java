package joyontasaha.com.myweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText city;
    TextView result, longitude, latitude;
    ImageView weatherImage;
    Button button;

    // https://api.openweathermap.org/data/2.5/weather?q=paris&appid=db4e691e347bd43a7e802b7c5d94c954

    String baseURL = "https://api.openweathermap.org/data/2.5/weather?q=";
    String API = "&appid=db4e691e347bd43a7e802b7c5d94c954";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.getCity);
        result = findViewById(R.id.result);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        weatherImage = findViewById(R.id.weatherImage);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = city.getText().toString();
                if (TextUtils.isEmpty(cityName)) {
                    city.setError("Enter city name!");
                }

                String myURL = baseURL + cityName + API;

//                Log.i("URL", "URL" + myURL);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.i("JSON", "JSON: " + jsonObject);

                                try {
                                    String info = jsonObject.getString("weather");
                                    Log.i("INFO", "INFO: "+ info);

                                    JSONArray ar = new JSONArray(info);

                                    for (int i = 0; i < ar.length(); i++){
                                        JSONObject parObj = ar.getJSONObject(i);

                                        String myWeather = parObj.getString("main");

                                        result.setText(myWeather);
                                        Log.i("ID", "ID: " + parObj.getString("id"));
                                        Log.i("MAIN", "MAIN: " + parObj.getString("main"));

                                        setWeatherImage(myWeather);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                try {
                                    String coor = jsonObject.getString("coord");
                                    Log.i("COOR", "COOR: " + coor);
                                    JSONObject co = new JSONObject(coor);

                                    String lon = co.getString("lon");
                                    String lat = co.getString("lat");

                                    longitude.setText(lon);
                                    latitude.setText(lat);

                                    Log.i("LON", "LON: " + lon);
                                    Log.i("LAT", "LAT: " + lat);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.i("Error", "Something went wrong" + volleyError);

                                Toast.makeText(MainActivity.this, "No such city exists!", Toast.LENGTH_SHORT).show();

                            }
                        }


                );
                MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);
            }
        });


    }

    public void setWeatherImage(String myWeather) {

        myWeather = myWeather.toLowerCase();

        if (myWeather.contains("clear")) {
           weatherImage.setImageResource(R.drawable.clear_sky);
        }
        else if (myWeather.contains("clouds")) {
            weatherImage.setImageResource(R.drawable.scattered_clouds);
        }
        else if (myWeather.contains("rain")) {
            weatherImage.setImageResource(R.drawable.shower_rain);
        }
        else if (myWeather.contains("thunderstorm")) {
            weatherImage.setImageResource(R.drawable.thunderstorm);
        }
        else if (myWeather.contains("snow")) {
            weatherImage.setImageResource(R.drawable.snow);
        }
        else {
            weatherImage.setImageResource(R.drawable.mist);
        }
    }

}
