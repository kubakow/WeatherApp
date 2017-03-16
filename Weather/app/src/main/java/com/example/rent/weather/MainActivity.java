package com.example.rent.weather;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.city)
    TextView location;

    @BindView(R.id.temperature)
    TextView temperature;

    @BindView(R.id.skytext)
    TextView skytext;

    @BindView(R.id.picture)
    ImageView picture;

    @BindView(R.id.city_name_edit_text)
    TextInputEditText cityEditText;
    private Retrofit retrofit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://weathers.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        search("Warszawa");
    }

    private void search(String cityname) {
        WeatherService weatherService = retrofit.create(WeatherService.class);
        weatherService.getWeather(cityname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataContainer -> {
                    WeatherModel weatherModel = dataContainer.getData();
                    location.setText(weatherModel.getLocation());
                    temperature.setText(weatherModel.getTemperature()+"\u00b0"+"C");
                    skytext.setText(weatherModel.getSkytext());
                    iconDisplayer(weatherModel);
                    if(progressDialog !=null){
                    progressDialog.hide();
                }

                    showNotification(cityname);

                });
    }

    private void iconDisplayer(WeatherModel weatherModel) {
        if(weatherModel.getSkytext().equalsIgnoreCase("Sky is clear")){
            picture.setImageResource(R.drawable.clear_sunny);
        }
        else if(weatherModel.getSkytext().equalsIgnoreCase("Few clouds")){
            picture.setImageResource(R.drawable.semi_cloudy);
        }
        else if(weatherModel.getSkytext().equalsIgnoreCase("Overcast clouds")){
            picture.setImageResource(R.drawable.cloudy);
        }
        else if(weatherModel.getSkytext().equalsIgnoreCase("Broken clouds")){
            picture.setImageResource(R.drawable.rainy);
        }
        else if(weatherModel.getSkytext().equalsIgnoreCase("Scattered clouds")){
            picture.setImageResource(R.drawable.semi_cloudy);
        }
        else if(weatherModel.getSkytext().equalsIgnoreCase("Light rain")){
            picture.setImageResource(R.drawable.rainy);
        }
    }

    private void showNotification(String cityname) {
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(this, 66, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stormy);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentText("Weather info loaded for " + cityname)
                .setSmallIcon(R.drawable.friging_snowing)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .addAction(R.drawable.clear_sunny, "Search", mainActivityPendingIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(cityname.hashCode(), notification);
        
    }

    @OnClick(R.id.floating_search)
    void onSearchButtonClick(){
        progressDialog = ProgressDialog.show(this, "Loading", "Look outside the window", true);
        search(cityEditText.getText().toString());

    }
}
