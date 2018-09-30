package com.example.hp_pc.weatheraap.Ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp_pc.weatheraap.R;
import com.example.hp_pc.weatheraap.Weather.CurrentWeather;
import com.example.hp_pc.weatheraap.Weather.Forecast;
import com.example.hp_pc.weatheraap.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private Forecast forecast;
    private ImageView iconImageView;
   final double latitude = 28.7041;
    final double longitude = 77.1025;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getForecast(latitude,longitude);
    }

    private void getForecast(double latitude, double longitude) {
        final ActivityMainBinding binding = DataBindingUtil
                .setContentView(MainActivity.this, R.layout.activity_main);

        TextView darkSky= (TextView) findViewById(R.id.darkSkyAttribution);
        darkSky.setMovementMethod(LinkMovementMethod.getInstance());
        iconImageView = (ImageView) findViewById(R.id.iconImageView);
        String apiKey = "1b09db65f1328a0a2f7d9f16b87dc33c";

        String forecastURL = "https://api.forecast.io/forecast/"
                + apiKey + "/"
                + latitude + ","
                + longitude;

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData=response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                              forecast=parseForecastData(jsonData);
                            CurrentWeather currentWeather=forecast.getCurrentWeather();
                            final CurrentWeather displayWeather = new CurrentWeather(
                                    currentWeather.getLocationLabel(),
                                    currentWeather.getIcon(),
                                    currentWeather.getTime(),
                                    currentWeather.getTemperature(),
                                    currentWeather.getHumidity(),
                                    currentWeather.getPrecipChance(),
                                    currentWeather.getSummary(),
                                    currentWeather.getTimeZone()
                            );


                            binding.setWeather(displayWeather);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable = getResources().getDrawable(displayWeather.getIconId());
                                    iconImageView.setImageDrawable(drawable);
                                }
                            });



                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception caught: ", e);
                    }catch (JSONException j){
                        Log.e(TAG,"Json Exception caught",j);
                    }
                }
            });


        }
    }
    private Forecast parseForecastData(String jsonData) throws JSONException {
        Forecast forecast=new Forecast();
        forecast.setCurrentWeather(getCurrentDetails(jsonData));
      //  forecast.setHours(getHours(jsonData));
        return forecast;
    }

    /*private Hour[] getHours(String jsonData) throws JSONException {
        JSONObject forecast= new JSONObject(jsonData);
        String timezone=forecast.getString("timezone");
        JSONObject hourly=forecast.getJSONObject("hourly");
        JSONArray data=hourly.getJSONArray("data");
        Hour[] hours=new Hour[data.length()];


    }*/

    private CurrentWeather getCurrentDetails(String jsonData)throws JSONException {
        JSONObject forecast=new JSONObject(jsonData);
        String timezone=forecast.getString("timezone");
        Log.d(TAG,"from Json:"+timezone);
        JSONObject currently=forecast.getJSONObject("currently");
        CurrentWeather currentWeather=new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setLocationLabel("Delhi, INDIA");
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));

        currentWeather.setTimeZone(timezone);

        Log.d(TAG, currentWeather.getFormattedTime());


        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        boolean isAvailable=false;
        if (networkInfo !=null&&networkInfo.isConnected()){
            isAvailable=true;
        }
        else {
            Toast.makeText(this, R.string.network_unavailable_msg,Toast.LENGTH_LONG).show();
        }
             return isAvailable;
    }

    private void alertUserAboutError() {
        AndroidDialogFragment dialog=new AndroidDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }


    public void refreshOnClick(View view) {
        getForecast(latitude, longitude);
        Toast.makeText(this, "Refreshing data", Toast.LENGTH_LONG).show();
    }

}



