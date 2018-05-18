package com.wang.tilt_z;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

public class MainActivity extends AppCompatActivity  {

    private final String TAG = "MainActivity: ";
    private SensorManager sensorManager = null;
    private Sensor accSensor;

    private Sensor currentSensor = null;
    private SensorEventListener currentSensorListener, oldSensorListener = null;

    private float[] accValues;
    private float[] magValues;
    private float[] gyroValues;

    private TiltCalculator tiltCalculator = new TiltCalculator();
    private ShakeValidator shakeValidator = new ShakeValidator();

    private long prevToggleTime;
    private int shakeCounter = 0;
    private long shakeDuration = 0;

    private TiltInfo tiltInfo;
    private Vibrator vibrator;

    private BottomNavigationView bottomNavigationView;
    private GraphView graphView;
    private Graph graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get sensor manager
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.setCurrentSensor(accSensor);
        this.setCurrentSensorListener(accListener);

        sensorManager.registerListener(getCurrentSensorListener(), getCurrentSensor(), SensorManager.SENSOR_DELAY_NORMAL);

        accValues = new float[3];
        magValues = new float[3];
        gyroValues = new float[3];

        tiltInfo = new TiltInfo(this);
        final LinearLayout container = (LinearLayout) findViewById(R.id.linear_layout);
        container.addView(tiltInfo);

        graphView = (GraphView) findViewById(R.id.graph);
        graph = new Graph(graphView);

        // Add bottom navigation for graphs
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationItem exit = new BottomNavigationItem("Exit",
                ContextCompat.getColor(this, R.color.colorPrimary),
                R.drawable.ic_home_black_24dp);
        BottomNavigationItem accelo = new BottomNavigationItem("Accelo",
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                R.drawable.ic_dashboard_black_24dp);
        BottomNavigationItem magneto = new BottomNavigationItem("Magneto",
                ContextCompat.getColor(this, R.color.colorAccent),
                R.drawable.ic_launcher_background);
        BottomNavigationItem gryo = new BottomNavigationItem("Gryo",
                ContextCompat.getColor(this, R.color.colorSeaGreen),
                R.drawable.ic_notifications_black_24dp);
        bottomNavigationView.addTab(exit);
        bottomNavigationView.addTab(accelo);
        bottomNavigationView.addTab(magneto);
        bottomNavigationView.addTab(gryo);

        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {

            @Override
            public void onNavigationItemClick(int index) {
                oldSensorListener = getCurrentSensorListener();
                Sensor newSensor = null;
                SensorEventListener newListener = null;
                switch (index) {
                    case 0:
                        System.exit(0);
                    case 1:
                        newSensor = accSensor;
                        newListener = accListener;
                        break;
                    case 2:
                        newSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                        newListener = magnetListener;
                        break;
                    case 3:
                        newSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);;
                        newListener = gyroListener;
                        break;
                    default:

                }
                sensorManager.unregisterListener(oldSensorListener);
                setCurrentSensor(newSensor);
                setCurrentSensorListener(newListener);
                sensorManager.registerListener(getCurrentSensorListener(), getCurrentSensor(), SensorManager.SENSOR_DELAY_NORMAL);
                graphView.removeAllSeries();
                graph = new Graph();
                graph.setGraphView(graphView);
                graph.setBeginTime(System.currentTimeMillis());
                graph.render();
            }
        });
    }

    @Override
    public void onResume () {
        super.onResume();
        sensorManager.registerListener(currentSensorListener, currentSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(currentSensorListener);
    }

    private final SensorEventListener accListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                //Update accelometer sensor data
                accValues = sensorEvent.values;
                float x = accValues[0];
                float y = accValues[1];
                float z = accValues[2];
                //Log.i(TAG, ">> accValues obtained <<");
                graph.setSeries(getAccValues());

                //Update tiltCalculator
                tiltCalculator.setSensorValueX(x);
                tiltCalculator.setSensorValueY(y);
                tiltCalculator.setSensorValueZ(z);
                int grade = tiltCalculator.getGrade();

                // Update shakeValidator
                shakeValidator.setSensorValueX(x);
                shakeValidator.setSensorValueY(y);
                shakeValidator.setSensorValueZ(z);
                shakeValidator.validate();

                // Update View
                tiltInfo.setGrade(String.valueOf(grade));
                if (shouldToggle()) {
                    vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                    tiltInfo.setRedColor();
                } else {
                    tiltInfo.setBlueColor();
                }

                /** Toggle Color RED <-> Blue
                if (shouldToggle()) {
                    vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                    tiltInfo.toggleColor();
                }
                 */
            }
        }

        private boolean shouldToggle() {

            long timeDifference = System.currentTimeMillis() - prevToggleTime;
            prevToggleTime = System.currentTimeMillis();

            if (shakeValidator.isShaking()) {
                shakeDuration += timeDifference;
                shakeCounter ++;
            } else if (shakeCounter == 0) {
                shakeDuration = 0;
            } else {
                shakeCounter = 0;
            }
            return shakeDuration > Constants.THRESHOLD_SHAKE_DURATION.getValue();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private final SensorEventListener magnetListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                //Update magnet field meter sensor data
                magValues = sensorEvent.values;
                graph.setSeries(getMagValues());
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private final SensorEventListener gyroListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

                //Update gyroscope meter sensor data
                gyroValues = sensorEvent.values;
                graph.setSeries(getGyroValues());
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public float[] getAccValues() {
        return accValues;
    }

    public float[] getMagValues() {
        return magValues;
    }

    public float[] getGyroValues() {
        return gyroValues;
    }

    public Sensor getCurrentSensor() {
        return currentSensor;
    }

    public void setCurrentSensor(Sensor currentSensor) {
        this.currentSensor = currentSensor;
    }

    public SensorEventListener getCurrentSensorListener() {
        return currentSensorListener;
    }

    public void setCurrentSensorListener(SensorEventListener currentSensorListener) {
        this.currentSensorListener = currentSensorListener;
    }
}
