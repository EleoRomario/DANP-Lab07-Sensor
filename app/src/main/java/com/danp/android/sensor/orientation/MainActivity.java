package com.danp.android.sensor.orientation;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.orientation_x_axis) TextView textOrientationXAxis;
    @BindView(R.id.orientation_y_axis) TextView textOrientationYAxis;
    @BindView(R.id.orientation_z_axis) TextView textOrientationZAxis;
    @BindView(R.id.magne_x_axis) TextView textMagneticXAxis;
    @BindView(R.id.magne_y_axis) TextView textMagneticYAxis;
    @BindView(R.id.magne_z_axis) TextView textMagneticZAxis;
    @BindView(R.id.acceleration_x_axis) TextView textAccelerationXAxis;
    @BindView(R.id.acceleration_y_axis) TextView textAccelerationYAxis;
    @BindView(R.id.acceleration_z_axis) TextView textAccelerationZAxis;
    @BindView(R.id.txt_orientation) TextView textOrientation;

    @BindView(R.id.view_orientation) CoordinateFrameView coordinateFrameView;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        presenter = new MainPresenter(this, sensorManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.registerSensorsListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unregisterSensorsListeners();
    }

    @Override
    public void updateOrientationSensorDataChanged(float xAngle, float yAngle, float zAngle) {
        textOrientationXAxis.setText(String.valueOf(xAngle));
        textOrientationYAxis.setText(String.valueOf(yAngle));
        textOrientationZAxis.setText(String.valueOf(zAngle));
        coordinateFrameView.setAngleZAxis(zAngle);
        if(xAngle==270.0){
            textOrientation.setText("Vertical 1");
        }else if(xAngle==90.0 ){
            textOrientation.setText("Vertical 2");
        }else if(yAngle==90.0){
            textOrientation.setText("Horizontal 1");
        }else if(yAngle==270.0){
            textOrientation.setText("Horizontal 2");
        }
    }

    @Override
    public void updateMagneticSensorDataChanged(float xRotationRate, float yRotationRate, float zRotationRate) {
        textMagneticXAxis.setText(String.valueOf(xRotationRate));
        textMagneticYAxis.setText(String.valueOf(yRotationRate));
        textMagneticZAxis.setText(String.valueOf(zRotationRate));


    }

    @Override
    public void updateAccelerationSensorDataChanged(float xAcceleration, float yAcceleration, float zAcceleration) {
        textAccelerationXAxis.setText(String.valueOf(xAcceleration));
        textAccelerationYAxis.setText(String.valueOf(yAcceleration));
        textAccelerationZAxis.setText(String.valueOf(zAcceleration));

    }
}
