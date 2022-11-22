package edu.uw.tcss450.team3.tiktalk.ui.weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

public class WeatherViewModel extends AndroidViewModel {
    private MutableLiveData<JSONObject> mResponse;

    private String coorWeatherURL = "https://tiktalk-app-web-service.herokuapp.com/weather/lat-lon";
    private String zipcodeWeatherURL= "https://tiktalk-app-web-service.herokuapp.com/weather/zipcode";
    // Hard coded for the location --> UWT
    private static final String HARD_CODED_LATITUDE = "47.2454";
    private static final String HARD_CODED_LONGITUDE = "-122.4385";
    private static final String HARD_CODED_ZIPCODE = "98402";

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {

        try {
            String city = result.getString("city");
            JSONObject current = result.getJSONObject("current");
            String cTempF = String.valueOf(current.getInt("tempF"));
            String cTempC = String.valueOf(current.getInt("tempC"));
            String cCondition = current.getString("condition");
            String cIconValue = current.getString("iconValue");
            String cIcon = current.getString("icon");

            JSONObject body = new JSONObject();
            try {
                body.put("city", city);
                body.put("tempF", cTempF);
                body.put("tempC", cTempC);
                body.put("condition", cCondition);
                body.put("icon", cIconValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mResponse.setValue(body);

            //inform observers of the change (setValue)
            //getOrCreateMapEntry(response.getInt("chatId")).setValue(list);
            //mWeatherData.setValue(list);
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success WeatherViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }

    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connectGet() {
        String url = zipcodeWeatherURL + "/" + HARD_CODED_ZIPCODE;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
                this::handleError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public void connectPost() {
        String url = zipcodeWeatherURL + "/" + HARD_CODED_ZIPCODE;
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }


//    public void connectGet(final String zipcode, final String jwt) {
//        String url =
//                zipcodeWeatherURL + "/" + zipcode ;
//        Request request = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null, //no body for this get request
//                this::handleResult,
//                this::handleError) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                // add headers <key,value>
//                headers.put("Authorization", jwt);
//                return headers;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Instantiate the RequestQueue and add the request to the queue
//        Volley.newRequestQueue(getApplication().getApplicationContext())
//                .add(request);
//    }
//
//    public void connect(final String first,
//                        final String last,
//                        final String email,
//                        final String nickname,
//                        final String password) {
//
//        String url = "https://tiktalk-app-web-service.herokuapp.com/auth";
//        JSONObject body = new JSONObject();
//        try {
//            body.put("first", first);
//            body.put("last", last);
//            body.put("email", email);
//            body.put("nickname", nickname);
//            body.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Request request = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                body,
//                mResponse::setValue,
//                this::handleError);
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Instantiate the RequestQueue and add the request to the queue
//        Volley.newRequestQueue(getApplication().getApplicationContext())
//                .add(request);
//    }
//
//    private void handleResult(final JSONObject response) {
//        List<WeatherData> list = new ArrayList<>();
//        try {
//            city = response.getString("city");
//            JSONObject current = response.getJSONObject("current");
//            cTempF = current.getInt("tempF");
//            cTempC = current.getInt("tempC");
//            cCondition = current.getString("condition");
//            cIconValue = current.getString("iconValue");
//            cIcon = current.getString("icon");
//            WeatherData weatherData = new WeatherData(
//                        city, cTempF, cTempC, cCondition, cIconValue, cIcon
//            );
//                if (!list.contains(weatherData)) {
//                    // don't add a duplicate
//                    list.add(0, weatherData);
//                    System.out.println("Success");
//
//                } else {
//                    // this shouldn't happen but could with the asynchronous
//                    // nature of the application
//                    Log.wtf("No Location", "No weather data of this location");
//                }
//
//            //inform observers of the change (setValue)
//            //getOrCreateMapEntry(response.getInt("chatId")).setValue(list);
//            //mWeatherData.setValue(list);
//        }catch (JSONException e) {
//            Log.e("JSON PARSE ERROR", "Found in handle Success WeatherViewModel");
//            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
//        }
//
//    }
//
//    private void handleError(final VolleyError error) {
//        if (Objects.isNull(error.networkResponse)) {
//            try {
//                mResponse.setValue(new JSONObject("{" +
//                        "error:\"" + error.getMessage() +
//                        "\"}"));
//            } catch (JSONException e) {
//                Log.e("JSON PARSE", "JSON Parse Error in handleError");
//            }
//        }
//        else {
//            String data = new String(error.networkResponse.data, Charset.defaultCharset())
//                    .replace('\"', '\'');
//            try {
//                JSONObject response = new JSONObject();
//                response.put("code", error.networkResponse.statusCode);
//                response.put("data", new JSONObject(data));
//                mResponse.setValue(response);
//            } catch (JSONException e) {
//                Log.e("JSON PARSE", "JSON Parse Error in handleError");
//            }
//        }
//    }


}
