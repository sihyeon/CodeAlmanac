package com.example.teamalmanac.codealmanac;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
    private final int GEO_PERMISSIONS_REQUEST = 1;
    private LocationManager mLocationManager;
    private locationListener mMyLocationListener;
    private boolean isGPSSensor = false;

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

        //배경 이미지
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.layout);
        relativeLayout.setBackground(rootView.getResources().getDrawable(R.drawable.bg_2, TabActivity.getMainContext().getTheme()));

        //Digital Clock FONT asset
        TextClock digitalClock = (TextClock) rootView.findViewById(R.id.digital_clock);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "LSSM.TTF");
        digitalClock.setTypeface(typeface);

        //datetime
        TextView dt = (TextView) rootView.findViewById(R.id.datetime);
        String format = new String("MM .dd  EEEE");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        dt.setText(sdf.format(new Date()));
        Typeface type = Typeface.createFromAsset(getContext().getAssets(), "LSSM.TTF");
        dt.setTypeface(type);

        //화면 이동 메세지 폰트
        TextView mv = (TextView) rootView.findViewById(R.id.textView);
        Typeface mvtype = Typeface.createFromAsset(getContext().getAssets(), "NanumSquareR.ttf");
        mv.setTypeface(mvtype);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //onCreateView에서 rootView가 리턴되기전에 getView() 함수를 부를 수 없어서 여기서 호출해야함.
        setMainText();
        setGeoLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isGPSSensor) {
            if (ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.removeUpdates(mMyLocationListener);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("LcokScreenFragment", "리퀘스트퍼미션리절트 진입");
        if (requestCode == GEO_PERMISSIONS_REQUEST) {
            boolean perimssionChecking = false;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    //권한 받음
                    Log.d("LcokScreenFragment", "권한 받음");
                    perimssionChecking = true;
                } else { //권한 거부함
                    Log.d("LcokScreenFragment", "권한 거부함");
                    return;
//                    TabActivity.getTabActivity().finish();
                }
            }
            if (perimssionChecking) {
                setGeoLocation();
            }
        }
    }

    /*
    GPS를 정리해보면
    setGeoLocation -> 권한체킹 -> (권한 없을시) requestpermission으로 권한 요청 후 함수종료 -> 권한 두개 전부 받으면 다시 setGeoLocation
                               -> (권한 있을시) locationManager 생성 및 등록 -> GPS 켜져있나 확인 -> 꺼져있으면 다이알로그 -> 확인창 누르면 설정창 띄움
                                 -> 설정창띄우고 isGPSSensor 통해서 GPS 있을 시 동작
     */
    //GPS 정보 가져오기
    private void setGeoLocation() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET};
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(getContext().getApplicationContext(), permission) != PermissionChecker.PERMISSION_GRANTED) {
                //권한이 없을 경우 권한을 요청
                ActivityCompat.requestPermissions(getActivity(), permissions, GEO_PERMISSIONS_REQUEST);
                return;
            }
        }

        mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Log.d("LockScreenFragment", mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) + " - GPS");
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGPSSensor = true;
            mMyLocationListener = new locationListener();
            Log.d("weather", "GPS 활성화되어있음.");
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, mMyLocationListener);
            Location lastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                getWeather(lastLocation.getLatitude(), lastLocation.getLongitude());
                try {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.KOREAN);
                    List<Address> addrData = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 2);
                    String address = addrData.get(0).getLocality() + " " + addrData.get(0).getSubLocality();
                    ((TextView) getView().findViewById(R.id.text_location)).setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else{
        isGPSSensor=false;    //GPS가 켜져있지 않으면 위치 & 날씨 안뜨게함
        ((TextView)getView().findViewById(R.id.text_weather_icon)).setText("");
        ((TextView)getView().findViewById(R.id.text_temp)).setText("");
        ((TextView)getView().findViewById(R.id.text_location)).setText("");
        return;
        }
    }

    //위치정보 리스너
    public class locationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.d("LockScreenFragment", "lat: " + location.getLatitude() + ", lon: " + location.getLongitude());
                try {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.KOREAN);
                    List<Address> addrData = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
                    String address = addrData.get(0).getLocality() + " " + addrData.get(0).getSubLocality();
                    ((TextView) getView().findViewById(R.id.text_location)).setText(address);
                } catch (IOException e) {
                    Log.d("LockScreenFragment", "geocoder error: " + e);
                }
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    }

    //OpenWeatherMap에서 날씨정보를 받아옴.
    private void getWeather(double lat, double lon) {
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
                            //날씨 텍스트를 받아와서 아이콘을 지정함
                            Log.d("jsonTest", response.getJSONArray("weather").getJSONObject(0).getString("description"));
                            setWeatherIcon(response.getJSONArray("weather").getJSONObject(0).getString("description"));
                            //온도 텍스트 받아와서 온도지정
                            Log.d("jsonTest", response.getJSONObject("main").getString("temp") + "");
                            setTemperature(response.getJSONObject("main").getString("temp"));
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

    private void setTemperature(String temperature) {
        TextView temperatureText = (TextView) getView().findViewById(R.id.text_temp);
        temperatureText.setText(temperature + "º");
    }

    //날씨 아이콘 선택
    private void setWeatherIcon(String weather) {
        int presentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        String weatherIconStringId = "wi_";
        switch (weather) {
            case "clear sky":
                if (5 <= presentHour && presentHour <= 17) {
                    //아침 & 낮
                    weatherIconStringId += "day_sunny";
                } else {
                    //저녁
                    weatherIconStringId += "night_clear";
                }
                break;
            case "few clouds":
                if (5 <= presentHour && presentHour <= 17) {
                    //아침 & 낮
                    weatherIconStringId += "day_cloudy";
                } else {
                    //저녁
                    weatherIconStringId += "night_alt_cloudy";
                }
                break;
            case "scattered clouds":
                weatherIconStringId += "cloud";
                break;
            case "broken clouds":
                weatherIconStringId += "cloudy";
                break;
            case "shower rain":
                weatherIconStringId += "showers";
                break;
            case "rain":
                weatherIconStringId += "rain";
                break;
            case "thunderstorm":
                weatherIconStringId += "thunderstorm";
                break;
            case "snow":
                weatherIconStringId += "snow";
                break;
            case "mist":
                weatherIconStringId += "fog";
                break;
        }
        //날씨 아이콘 선택
        int resId = getResources().getIdentifier(weatherIconStringId, "string", getContext().getPackageName());

        TextView weatherIconText = (TextView) getView().findViewById(R.id.text_weather_icon);
        weatherIconText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "weathericons-regular-webfont.ttf"));
        weatherIconText.setText(getContext().getString(resId));
    }

    //인사말 설정
    private String setGreetingMessage() {
        int presentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        //아침
        if (4 <= presentHour && presentHour <= 11) return "Good Morning";
            //오후(점심)
        else if (12 <= presentHour && presentHour <= 18) return "Good Afternoon";
            //저녁
        else return "Good Evening";
    }

    //메인 포커스 세팅 (메인포커스 없을 때, 사용자 이름 없을 때의 논의 필요)
    private void setMainText() {
        String userName = mDB.getUserName();
        String mainFocus = mDB.getMainFocus();
        String greetingMessage = "";
        String mainfocusMessage = "";

        TextView greetingText = (TextView) getView().findViewById(R.id.text_greeting);
        TextView userNameText = (TextView) getView().findViewById(R.id.text_user_name);
        TextView todayText = (TextView) getView().findViewById(R.id.text_today);
        TextView mainfocusText = (TextView) getView().findViewById(R.id.text_mainfocus);

        if (userName != null) {
            greetingMessage += setGreetingMessage() + ",";
        } else {
            greetingMessage += "Name is noting";
        }
        if (mainFocus != null) {
            mainfocusMessage += mainFocus;
        } else {
            mainfocusMessage += "MainFocus is noting";
        }

        //Good morning,~ 부분의 글꼴
        Typeface fontType = Typeface.createFromAsset(getContext().getAssets(), "FRAMDCN.TTF");
        greetingText.setTypeface(fontType);     //인사말
        userNameText.setTypeface(fontType);     //사용자 이름
        todayText.setTypeface(fontType);        //TODAY부분
        mainfocusText.setTypeface(fontType);    //mainfocus


        greetingText.setText(greetingMessage);
        userNameText.setText(userName);
        mainfocusText.setText(mainfocusMessage);
    }
}
