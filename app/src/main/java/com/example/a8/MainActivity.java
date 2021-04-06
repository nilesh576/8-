package com.example.a8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView searchbar;
    String[] suggestion;
    ArrayAdapter<String> arrayAdapter;
    Timer timer;
    Clock clock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchbar = findViewById(R.id.search_bar_tv);
        timer = new Timer();

        clock = new Clock() {
            @Override
            public ZoneId getZone() {
                return null;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return null;
            }

            @Override
            public Instant instant() {
                return null;
            }
        };

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer.purge();
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="https://www.metaweather.com/api/location/search/?query="+s.toString();

                JsonArrayRequest response = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Toast.makeText(MainActivity.this, response.getString(1), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, s.toString()+" not found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "connect to internet", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(response);

//              poc for timer
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        queue.stop();
//                        queue.start();
//                        queue.add(response);
//
//                    }
//                };
//                timer.schedule(timerTask,3000);
            }
        });

        searchbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,"---"+parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
            }
        });


        suggestion = getResources().getStringArray(R.array.stocks);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, suggestion);
        searchbar.setAdapter(arrayAdapter);
    }
}