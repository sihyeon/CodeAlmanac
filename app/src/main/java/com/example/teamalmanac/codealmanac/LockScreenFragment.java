package com.example.teamalmanac.codealmanac;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LockScreenFragment extends Fragment {
    private DataManager mDB = null;
    private Calendar mCalendar;

    public LockScreenFragment() {
        // Required empty public constructor
    }

    public static LockScreenFragment newInstance() {
        LockScreenFragment fragment = new LockScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDB = DataManager.getSingletonInstance();
        mCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lock_screen, container, false);

        RelativeLayout relativeLayout = (RelativeLayout)rootView.findViewById(R.id.layout_lock_fragment);
        relativeLayout.setBackground(rootView.getResources().getDrawable(R.drawable.test, getContext().getTheme()));

        //Digital Clock FONT asset
        TextClock digitalClock = (TextClock) rootView.findViewById(R.id.digital_clock);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "FRABK.TTF");
        digitalClock.setTypeface(typeface);

        //datetime
        TextView dt = (TextView) rootView.findViewById(R.id.datetime);
        String format = "MM . dd EEEE";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
        dt.setText(sdf.format(new Date()));
        Typeface type = Typeface.createFromAsset(getContext().getAssets(), "NanumSquareB.ttf");
        dt.setTypeface(type);
        //여기있던 메인포커스 타입설정문 setMainText 함수로 옮김

        //인사말 밑 mainfocus
        setMainText(rootView);
        setGeoLocation(rootView);

        return rootView;
    }

    //GPS 정보 가져오기
    private void setGeoLocation(View rootView){
        double lat, lon;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "널값");
            return;
        }
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        lat = lastLocation.getLatitude();
        lon = lastLocation.getLongitude();
        getWeather(lat, lon);
        if(lastLocation != null){
            //위치정보 -> 지역정보로 변환
            Geocoder geocoder = new Geocoder(getContext(), Locale.KOREAN);
            try {
                List<Address> addr = geocoder.getFromLocation(lat, lon, 2);
                String location = addr.get(0).getLocality() + " " + addr.get(0).getSubLocality();
                ((TextView)rootView.findViewById(R.id.text_location)).setText(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getWeather(double lat, double lon){
        String key = "e883ecf0bc01088daed574539d20aa43";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://api.openweathermap.org/data/2.5/weather?" +
                "APPID=" + key +
                "&lat=" + lat +
                "&lon=" + lon +
                "&mode=json&units=metric";
        Log.d("jsonTest", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("jsonTest", response.getJSONArray("weather").getJSONObject(0).getString("description"));
                            Log.d("jsonTest", response.getJSONObject("main").getString("temp") + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "not data");
            }
        });
        queue.add(jsonObjectRequest);
    }

    //날씨 아이콘 선택
    private void setWeatherIcon(String weather){
        int presentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        String weatherIconStringId = "";
        if (5 <= presentHour && presentHour <= 17){
            //아침 & 낮
        } else {
            //저녁
        }
        switch (weather){
            case "clear sky":
                break;
            case "few clouds":
                break;
            case "scattered clouds":
                break;
            case "broken clouds":
                break;
            case "shower rain":
                break;
            case "rain":
                break;
            case "thunderstorm":
                break;
            case "snow":
                break;
            case "mist":
                break;
        }
        int resId = getResources().getIdentifier(weatherIconStringId, "string", getContext().getPackageName());
        getContext().getString(R.string.wi_cloud);
    }

    //인사말 설정
    private String setGreetingMessage(){
        int presentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        //아침
        if(4 <= presentHour && presentHour <= 11) return "Good Morning";
        //오후(점심)
        else if(12 <= presentHour && presentHour <= 18) return "Good Afternoon";
        //저녁
        else return "Good Evening";
    }

    //메인 포커스 세팅 (메인포커스 없을 때, 사용자 이름 없을 때의 논의 필요)
    private void setMainText(View rootView){
        String userName = mDB.getUserName();
        String mainFocus = mDB.getMainFocus();
        String greetingMessage = "";
        String mainfocusMessage = "";
        TextView greetingText = (TextView)rootView.findViewById(R.id.text_greeting);
        TextView mainfocusText = (TextView)rootView.findViewById(R.id.text_mainfocus);
        if(userName != null){
            greetingMessage += setGreetingMessage() + ", " + userName;
        } else {
            greetingMessage += "Name is noting";
        }
        if(mainFocus != null){
            mainfocusMessage += mainFocus;
        } else {
            mainfocusMessage += "MainFocus is noting";
        }
        //글꼴
        Typeface textType = Typeface.createFromAsset(getContext().getAssets(),"NanumSquareB.ttf");
        greetingText.setTypeface(textType);
        mainfocusText.setTypeface(textType);

        greetingText.setText(greetingMessage);
        mainfocusText.setText(mainfocusMessage);
    }
}
