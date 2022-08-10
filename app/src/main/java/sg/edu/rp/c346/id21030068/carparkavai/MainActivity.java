package sg.edu.rp.c346.id21030068.carparkavai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ListView lvCarParkAvai;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCarParkAvai = findViewById(R.id.lvCarParkAvai);
        client = new AsyncHttpClient();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<CarPark> alCarPark = new ArrayList<CarPark>();

        client.get("https://api.data.gov.sg/v1/transport/carpark-availability", new JsonHttpResponseHandler() {

            String total_lots;
            String lot_type;
            String lots_available;
            String carpark_number;
            String update_datetime;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArrItems = response.getJSONArray("items");
                    JSONObject firstObj = jsonArrItems.getJSONObject(0);
                    JSONArray jsonArrCarParkData = firstObj.getJSONArray("carpark_data");

                    for(int i = 0; i < jsonArrCarParkData.length(); i++) {
                        JSONObject jsonObjCarParkData = jsonArrCarParkData.getJSONObject(i);
                        JSONArray jsonArrCarParkInfo = jsonObjCarParkData.getJSONArray("carpark_info");
                        JSONObject secondObj = jsonArrCarParkInfo.getJSONObject(0);

                        total_lots = secondObj.getString("total_lots");
                        lot_type = secondObj.getString("lot_type");
                        lots_available = secondObj.getString("lots_available");
                        carpark_number = jsonObjCarParkData.getString("carpark_number");
                        update_datetime = jsonObjCarParkData.getString("update_datetime");

                        CarPark carpark = new CarPark(total_lots, lot_type, lots_available, carpark_number, update_datetime);
                        alCarPark.add(carpark);
                    }
                }
                catch(JSONException e){

                }
                //POINT X – Code to display List View
                ArrayAdapter<CarPark> aaCarPark = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, alCarPark);
                lvCarParkAvai.setAdapter(aaCarPark);


            }//end onSuccess
        });
    }//end onResume

}
