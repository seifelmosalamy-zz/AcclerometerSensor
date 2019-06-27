package com.example.app_;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";

    private SensorManager sensorManager;
    Sensor accelerometer;

    TextView resultText, stateText;
    Button resultBtn, sBtn, eBtn;

    private boolean clicked_start = false;
    private boolean clicked_end = false;

    String line = "Press one more for getting result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (TextView) findViewById(R.id.showResult);
        stateText = (TextView) findViewById(R.id.state);

        resultBtn = (Button) findViewById(R.id.getResult);
        eBtn = (Button) findViewById(R.id.endData);
        sBtn = (Button) findViewById(R.id.startData);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void btnStart (View view) {
        clicked_start = true;
        clicked_end = false;
        stateText.setText("data entry ...");
    }

    public void btnEnd (View view) {
        clicked_end = true;
        clicked_start = false;
        stateText.setText(" XX data is stopped being entered XX ");
    }

    public void btnResult (View view) { // read data from database
        Retreive r = new Retreive();
        r.execute();
        resultText.setText(line);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (clicked_start == true && clicked_end == false) {
            Log.d(TAG, "onSensorChnaged: X: " + sensorEvent.values[0] + " Y: " + sensorEvent.values[1] + " Z: " + sensorEvent.values[2]);
            Send objSend = new Send();
            objSend.execute(Float.toString(sensorEvent.values[0]), Float.toString(sensorEvent.values[1]), Float.toString(sensorEvent.values[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {}

    public class Retreive extends AsyncTask<String, String, String> {

        private String get_result_data = "http://192.168.2.6/App/get.php";

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(get_result_data);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                line = reader.readLine();

                Log.d("Result: ", "> " + line);

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public class Send extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String insert_test_data =  "http://192.168.2.6/App/insert.php";

            try {
                String xaxis = params[0];
                String yaxis = params[1];
                String zaxis = params[2];

                URL url = new URL(insert_test_data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("xaxis", "UTF-8") + "=" + URLEncoder.encode(xaxis, "UTF-8") + "&"
                        + URLEncoder.encode("yaxis", "UTF-8") + "=" + URLEncoder.encode(yaxis, "UTF-8") + "&" +
                        URLEncoder.encode("zaxis", "UTF-8") + "=" + URLEncoder.encode(zaxis, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
