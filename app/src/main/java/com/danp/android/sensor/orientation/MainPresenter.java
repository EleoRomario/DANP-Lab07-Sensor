package com.danp.android.sensor.orientation;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    private SensorManager sensorManager;
    private Sensor sensorMagnetic;
    private Sensor sensorAccelerometer;

    private SensorEventListener sensorMagneticEvenListener;
    private SensorEventListener sensorAccelerometerEvenListener;

    public MainPresenter(MainContract.View view, SensorManager sensorManager) {
        this.view = view;
        this.sensorManager = sensorManager;
        initSensors();
    }

    public void registerSensorsListeners() {
        sensorManager.registerListener(sensorMagneticEvenListener,
                sensorMagnetic,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorAccelerometerEvenListener,
                sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorsListeners() {
        sensorManager.unregisterListener(sensorMagneticEvenListener);
        sensorManager.unregisterListener(sensorAccelerometerEvenListener);
    }

    private void initSensors() {
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initSensorsListeners();
    }

    final float[] accelerometerReading = new float[3];
    final float[] magnetometerReading = new float[3];

    final float[] rotationMatrix = new float[9];
    final float[] orientationAngles = new float[3];

    private void initSensorsListeners() {
        if (sensorMagneticEvenListener == null) {
            sensorMagneticEvenListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float xRotationRate = roundFloat(sensorEvent.values[0]);
                    float yRotationRate = roundFloat(sensorEvent.values[1]);
                    float zRotationRate = roundFloat(sensorEvent.values[2]);

                    System.arraycopy(sensorEvent.values,0,magnetometerReading,0,magnetometerReading.length);
                    updateOrientationAngles();
                    view.updateMagneticSensorDataChanged(xRotationRate, yRotationRate, zRotationRate);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }
        if (sensorAccelerometerEvenListener == null) {
            sensorAccelerometerEvenListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float xAcceleration = roundFloat(sensorEvent.values[0]);
                    float yAcceleration = roundFloat(sensorEvent.values[1]);
                    float zAcceleration = roundFloat(sensorEvent.values[2]);

                    System.arraycopy(sensorEvent.values,0,accelerometerReading,0,accelerometerReading.length);
                    updateOrientationAngles();
                    view.updateAccelerationSensorDataChanged(xAcceleration, yAcceleration, zAcceleration);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }
    }
    public void updateOrientationAngles(){
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        float[] orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles);

        Log.d("ORIENTATION", "->"+orientation);
        float zAngle = (float) (roundFloat((float) (Math.toDegrees(orientation[0]) + 360.0)) % 360.0);
        float xAngle = (float) (roundFloat((float) (Math.toDegrees(orientation[1]) + 360.0)) % 360.0);;
        float yAngle = (float) (roundFloat((float) (Math.toDegrees(orientation[2]) + 360.0)) % 360.0);;
        view.updateOrientationSensorDataChanged(xAngle, yAngle, zAngle);
    }
    private float roundFloat(float value) {
        return (float) Math.round(value * 100) / 100;
    }
}
