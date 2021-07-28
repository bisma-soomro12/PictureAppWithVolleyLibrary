package com.example.pictureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // list view and array list for class
    ListView listView;
    ArrayList<MainData> dataArrayList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.list_view);
        // URL for the JSON file
        String url="https://picsum.photos/v2/list";
        ProgressDialog PD= new ProgressDialog(this);
        PD.setMessage("Loading!");
        PD.setCancelable(true);
        PD.show();

        // request from server
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    PD.dismiss();
                    try {
                        // json array
                        JSONArray jsonArray= new JSONArray(response);
                        // parsing the json array
                        parseArray(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), (CharSequence) error,Toast.LENGTH_SHORT).show();
            }
        });
        // requests
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
    private void parseArray(JSONArray jsonArray) {
        // this loop will create the objects of json
        for(int i=0;i<jsonArray.length();i++){
            try {
                // json object
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                // obj of maindata class
                MainData mainData=new MainData();
                // setting the name and picture form the json file according to the key pair
                mainData.setName(jsonObject.getString("author"));
                mainData.setImg(jsonObject.getString("download_url"));
                // initilze the data
                dataArrayList.add(mainData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return dataArrayList.size() ;
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =getLayoutInflater().inflate(R.layout.item_main,null);
                    MainData data = dataArrayList.get(position);
                    ImageView imageView=view.findViewById(R.id.img_view);
                    TextView textView=view.findViewById(R.id.txt_view);
                    // set the image from json file in imageview
                    // used glide to load the images in imageview
                    Glide.with(getApplicationContext()).load(data.getImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                    // set the text(author name) in textview
                    textView.setText(data.getName());
                    return view;
                }
            });
        }
    }

}