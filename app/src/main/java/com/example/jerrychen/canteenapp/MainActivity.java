package com.example.jerrychen.canteenapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

  int userId;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onStart() {
        super.onStart();
        ReadTask task = new ReadTask();
        task.execute("http://anbo-canteen.azurewebsites.net/Service1.svc/dishes");
    }

//    public void ButtonClicked(View view) {
//        Intent intent=new Intent(this,LoginActivity.class);
//        startActivity(intent);
//    }

    private class ReadTask extends ReadHttpTask {
        @Override
        protected void onPostExecute(CharSequence charSequence) {
            super.onPostExecute(charSequence);
            TextView message = (TextView) findViewById(R.id.message);
            //TextView messageT
            final List<Dish> dishes = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(charSequence.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    int id = obj.getInt("Id");
                    double alcohol = obj.getDouble("Alcohol");
                    double carbohydrates = obj.getDouble("Carbohydrates");
                    double energy = obj.getDouble("Energy");
                    double fat = obj.getDouble("Fat");
                    double price = obj.getDouble("Price");
                    double protein = obj.getDouble("Protein");
                    double weight = obj.getDouble("Weight");
                    String description = obj.getString("Description");
                    String pictureurl = obj.getString("PictureUrl");
                    String title = obj.getString("Title");
                    Dish dish = new Dish(id, alcohol, carbohydrates, energy, fat, price, protein, weight, description, pictureurl, title);
                    dishes.add(dish);
                }
                ListView listView = (ListView) findViewById(R.id.main_dishes_listview);
                DishListItemAdapter adapter = new DishListItemAdapter(getBaseContext(), R.layout.dishlist_item, dishes);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?>parent ,View view ,int position,long id){
                        Intent userintent=getIntent();
                        userId=userintent.getIntExtra("USER",1);

                        Intent intent=new Intent(getBaseContext(),DishActivity.class);

                        intent.putExtra("DISH", dishes.get((int)id));
                        intent.putExtra("USERID",userId);


                        startActivity(intent);
                    }
                });

            } catch (JSONException ex) {
                message.setText(ex.getMessage());
                Log.e("DISHES", ex.getMessage());
            }

        }
        @Override
        protected void onCancelled(CharSequence charSequence){
            super.onCancelled();
            TextView message=(TextView)findViewById(R.id.message);
            message.setText(charSequence);
            Log.e("DISHES",charSequence.toString());
        }

    }
}


