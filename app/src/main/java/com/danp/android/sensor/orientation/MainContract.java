package com.danp.android.sensor.orientation;

public interface MainContract {

    interface View {

        void updateOrientationSensorDataChanged(float xAngle,
                                                float yAngle,
                                                float zAngle);
        void updateMagneticSensorDataChanged(float xRotationRate,
                                         float yRotationRate,
                                         float zRotationRate);
        void updateAccelerationSensorDataChanged(float xAcceleration,
                                                 float yAcceleration,
                                                 float zAcceleration);
    }

    interface Presenter {

        void registerSensorsListeners();

        void unregisterSensorsListeners();
    }
}
