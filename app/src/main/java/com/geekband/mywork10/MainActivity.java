package com.geekband.mywork10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor magneticSensor;
    private Sensor acceleromterSensor;

    private ImageView compassImageView;
    private ImageView arrowImageView;

    private float[] accelerometerValues=new float[3];
    private float[] magneticValues=new float[3];
    private float lastRotateDegree=0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initSensor();

    }

    public void initSensor() {
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        magneticSensor =sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        acceleromterSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void findViews() {
        compassImageView= (ImageView) findViewById(R.id.compass_image_view);
        arrowImageView= (ImageView) findViewById(R.id.arrow_image_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,acceleromterSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager!=null)
        {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){

            accelerometerValues=event.values.clone();

        }else if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){

            magneticValues=event.values.clone();

        }
        float[] values=new float[3];
        float[] R=new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
        SensorManager.getOrientation(R,values);
        float rotateDegree=-(float)Math.toDegrees(values[0]);
        if (Math.abs(rotateDegree-lastRotateDegree)>1){
            RotateAnimation rotateAnimation=new RotateAnimation
                    (lastRotateDegree,rotateDegree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setFillAfter(true);
            compassImageView.startAnimation(rotateAnimation);
            lastRotateDegree=rotateDegree;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
