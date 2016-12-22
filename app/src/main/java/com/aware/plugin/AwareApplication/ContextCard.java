package com.aware.plugin.AwareApplication;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aware.providers.Battery_Provider;
import com.aware.providers.Light_Provider;
import com.aware.utils.IContextCard;

import java.util.Calendar;

public class ContextCard implements IContextCard {

    //Empty constructor used to instantiate this card
    public ContextCard(){};

    @Override
    public View getContextCard(Context context) {
        //Inflate and return your card's layout. See LayoutInflater documentation.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = (View) inflater.inflate(R.layout.card, null);

        //Modify card's content

        //TextView light = (TextView) card.findViewById(R.id.light_value);

        //TextView status = (TextView) card.findViewById(R.id.status);

        TextView lux = (TextView) card.findViewById(R.id.light_sensor_value);
        TextView luxvalue = (TextView) card.findViewById(R.id.lux_val);

        TextView chargevalue = (TextView) card.findViewById(R.id.charging_value);

        /*Cursor light_data = context.getContentResolver().query(Accelerometer_Provider.Accelerometer_Data.CONTENT_URI, null, null, null, Accelerometer_Provider.Accelerometer_Data.TIMESTAMP + " DESC LIMIT 1");
        if( light_data != null && light_data.moveToFirst() ) {
            double val0 = light_data.getDouble(light_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_0));
            double val1 = light_data.getDouble(light_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_1));
            double val2 = light_data.getDouble(light_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_2));

            double sqrtd_val = Math.sqrt( (val0*val0) + (val1*val1) + (val2*val2));

            String status_val = "";
            light.setText("sqrtd value is" + sqrtd_val);

            if( sqrtd_val<= 10.1 ) {
                status.setText("I am still");
                status_val = "I am still";
            } else {
                status.setText("Walking or running");
                status_val = "I am walking or running";
            }


            Log.d("sqrtd",  "x is "+ Double.toString(val0)+" y is "+Double.toString(val1)+" z is "+Double.toString(val2)+" sqrtd value is" + Double.toString(sqrtd_val)+" and " + status_val );

        } else {
            light.setText("No data to show");
        }
        if( light_data != null && ! light_data.isClosed()) light_data.close();*/


        //Starting to get light sensor values.

        /*if ( Plugin.isCharging ) {
            chargevalue.setText("Charging");
        } else {
            chargevalue.setText("Not Charging");
        }*/
        double upper_threshold = 2000.0;
        double lower_threshold = 50.0;

        Cursor lux_data = context.getContentResolver().query(Light_Provider.Light_Data.CONTENT_URI, null, null, null, Light_Provider.Light_Data.TIMESTAMP + " DESC LIMIT 1");
        if( lux_data != null && lux_data.moveToFirst() ) {
            //double val0 = light_data.getDouble(light_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_0));
            //double val1 = light_data.getDouble(light_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_1));
            double l_val = lux_data.getDouble(lux_data.getColumnIndex(Light_Provider.Light_Data.LIGHT_LUX));

            luxvalue.setText("Lux value is " + l_val);

            if( l_val > upper_threshold ) {
                lux.setText("Outdoor/Semi Outdoor");
            } else {

                Boolean isNight;
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                if(hour < 6 || hour > 18){
                    isNight = true;
                } else {
                    isNight = false;
                }

                if( isNight == false ){
                    lux.setText("Indoor");
                } else {
                    if(l_val < lower_threshold) {
                        lux.setText("Outdoor/Semi outdoor");
                    } else {
                        lux.setText("Indoor");
                    }
                }
            }
            Log.d( "lux", "Lux value is " + Double.toString( l_val ) );

        } else {
            lux.setText("No data to show");
        }
        if( lux_data != null && ! lux_data.isClosed()) lux_data.close();

        Cursor battery_data = context.getContentResolver().query(Battery_Provider.Battery_Data.CONTENT_URI, null, null, null, Battery_Provider.Battery_Data.TIMESTAMP + " DESC LIMIT 1");

        if( battery_data != null && battery_data.moveToFirst() ) {
            String battery_val = battery_data.getString(battery_data.getColumnIndex(Battery_Provider.Battery_Data.STATUS));
            //chargevalue.setText(battery_val);

            if ( battery_val.equals("3") )
                chargevalue.setText("Not Charging");
            if ( battery_val.equals("2") )
                chargevalue.setText("Charging");
            /*chargevalue.setText("Battery status is " + battery_val);
            Log.d( "chargeval", "Battery Status is " + battery_val );*/
        }

        if( battery_data != null && ! battery_data.isClosed()) battery_data.close();

        return card;
    }
}
