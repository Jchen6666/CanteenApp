package com.example.jerrychen.canteenapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DishActivity extends AppCompatActivity {
    private Dish dish;
    private User user;
    private GestureDetector gestureDetector;
    private SharedPreferences preferences;
    int[]rateList;
    public static final String ENERGY="ENERGY";
    public static final String PREFFILE_NAME = "energycal";
    public static final String  DISHNUM="DISHNUM";
    int ratings;
    int rate_mean;
    int userId;
    int sum;
    float num;
    float energy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);


        Intent intent = getIntent();
        dish = (Dish) intent.getSerializableExtra("DISH");
        userId =intent.getIntExtra("USERID",1);
        Log.e("userid",userId+"");
        ImageView photoview = (ImageView) findViewById(R.id.PHOTO);
        Picasso.with(getApplicationContext()).load(dish.getPictureurl()).into(photoview);

        TextView descriptionview = (TextView) findViewById(R.id.DESCRIPTION);
        descriptionview.setText(dish.getDescription());

        TextView weightview = (TextView) findViewById(R.id.WEIGHT);
        weightview.setText(dish.getWeight() + "g");

       // TextView userview=(TextView)findViewById(R.id.userId);
        //userview.setText(userId);

        addListeneronSpinner();
        Gesture();
        gettingRate();
        addListeneronRatingBar();

    }

    public void Gesture() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return DoIt(e1, e2);
            }

            private boolean DoIt(MotionEvent e1, MotionEvent e2) {
                boolean leftMovement = e1.getX() < e2.getX();
                if (leftMovement) {
                    finish();
                }
                return true;
            }

        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void addListeneronSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.SPINNER);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.energy_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView resultview = (TextView) findViewById(R.id.RESULT);
                String item = parent.getItemAtPosition(position) + "";
                if (item.contains("Carbohydrates")) {
                    resultview.setText(dish.getCarbohydrates() + "");

                }
                if (item.contains("Energy")) {
                    resultview.setText(dish.getEnergy() + "");
                }
                if (item.contains("Fat")) {
                    resultview.setText(dish.getFat() + "");
                }
                if (item.contains("Protein")) {
                    resultview.setText(dish.getProtein() + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Warning","0");
            }
        });
    }

    public int addListeneronRatingBar() {
        RatingBar rb = (RatingBar) findViewById(R.id.Rating);


        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings = Math.round(rating);
                Log.e("rating",ratings+"");
            }
        });
        return ratings;
    }

    public void Rating() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("CustomerId",userId);
            jsonObject.put("DishId",dish.getId());
            jsonObject.put("Rate",ratings);
            String jsonDocument = jsonObject.toString();
            Log.e("REQUEST",jsonDocument);
            RatingTask task=new RatingTask();
            task.execute("http://anbo-canteen.azurewebsites.net/Service1.svc/ratings",jsonDocument);
            //TextView textView=(TextView)findViewById(R.id.Receipt);
            Toast toast=Toast.makeText(this,"Thank you",Toast.LENGTH_LONG);
            toast.show();
        }catch (JSONException ex){
         Log.e("Message",ex.getMessage());
        }

    }

    public void Order(){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CustomerId",userId);
            jsonObject.put("PickupDateTime","/Date("+new Date().getTime()+2000+")/");
            jsonObject.put("DishId",dish.getId());
            jsonObject.put("Howmany",1);
            String jsonDocument =jsonObject.toString();
            Log.e("order",jsonDocument);
            RatingTask task=new RatingTask();
            task.execute("http://anbo-canteen.azurewebsites.net/Service1.svc/takeaways",jsonDocument);
            TextView textView=(TextView)findViewById(R.id.Receipt);
            textView.setText("Succeed");
        }catch(JSONException ex){
            Log.e("Message",ex.getMessage());
        }
    }

    public void ButtonClicked(View view) {
        Rating();
    }

    public void ButtonClickedOrder(View view) {
        Order();
        float energy2=Math.round(dish.getEnergy());
        preferences=getSharedPreferences(PREFFILE_NAME,MODE_PRIVATE);
        energy=preferences.getFloat(ENERGY,0);
        num=preferences.getFloat(DISHNUM,0);
        float energy3=energy;
        SharedPreferences.Editor editor=preferences.edit();

        Log.e("another dish",energy2+"");
        Log.e("DISHNUM",num+"");
        if (num==0){
            editor.putFloat(ENERGY,energy2);
            editor.putFloat(DISHNUM,1);
            editor.apply();
            Toast toast=Toast.makeText(this,"Total Energy : "+energy2,Toast.LENGTH_LONG);
            toast.show();
            Log.e("ENERGY",energy2+"");
            Log.e("DISHNUM",num+"");
        }
        else {
            editor.remove(ENERGY);
            editor.apply();
            editor.putFloat(ENERGY,energy2+energy3);
            float totalEnergy=energy2+energy3;
            Toast toast=Toast.makeText(this,"Total Energy : "+totalEnergy,Toast.LENGTH_LONG);
            toast.show();
            Log.e("ENERGY",totalEnergy+"");
            editor.apply();
        }

//        editor.remove(ENERGY);
//        editor.remove(DISHNUM);
//        editor.apply();
    }

    public double gettingRate(){

        gettingRateTask task=new gettingRateTask();
        task.execute("http://anbo-canteen.azurewebsites.net/Service1.svc/ratings/dish/"+dish.getId());

        return rate_mean;


    }
    private class gettingRateTask extends ReadHttpTask{


        @Override
        protected void onPostExecute(CharSequence charSequence){
            super.onPostExecute(charSequence);
            try{
                JSONArray array = new JSONArray(charSequence.toString());
                for (int i=0;i<array.length();i++){
                   JSONObject obj=array.getJSONObject(i);
                   int rate=obj.getInt("Rate");
                   sum+=rate;
                   Log.e("total",sum+"");
//                   rate_mean=sum/i;
//                   Log.e("average",rate_mean+"");
               }
                rate_mean=sum/array.length();
                Log.e("averageRate",rate_mean+"");

            }catch (JSONException ex) {
                Log.e("Error","error");
            }
            }
        @Override
        protected void onCancelled(CharSequence charSequence){
            super.onCancelled();

            Log.e("DISHES",charSequence.toString());
        }
    }
 }



