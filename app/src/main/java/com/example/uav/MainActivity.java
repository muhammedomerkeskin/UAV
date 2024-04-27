package com.example.uav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.example.uav.CommunicationSystem.SystemData;
import com.example.uav.CommunicationSystem.UDP_Service;
import com.example.uav.Util.LocationCalculation;
import com.example.uav.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    private TextView tvDroneLatitude, tvDroneLongitude, tvDroneMSL, tvDroneAGL,
            tvHomeLatitude, tvHomeLongitude, tvHomeMSL,
            tvDroneHorizontalVelocity, tvDroneVerticalVelocity,
            tvDroneHeading, tvDroneGPS, tvDroneMode, tvDroneARM,
            tvHorizontalDistanceFromHome, tvVerticalDistanceFromHome,
            tvDroneBatteryVoltage, tvDroneBatteryPercent;
    private EditText edtTargetLatitude,edtTargetLongitude,edtTargetAltitude;
    private ImageView ivGpsFixed, ivBattery;
    private MapView map = null;
    private Marker droneMarker = null;
    private static MainActivity instance;

    private DatagramSocket socket;
    private UDP_Service udpClientThread;
    CoordinatorLayout cr_BatteryWarning;
    private Snackbar batteryWarningShow;
    private Boolean batteryPercent_10 = true;
    private Boolean batteryPercent_30 = true;
    private Boolean batteryPercent_50 = true;
    private ImageButton btnCenterAndTrack,btnNotCenterAndTrack;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    int batteryPercent_1 = 50;
    int batteryPercent_2 = 30;
    int batteryPercent_3 = 20;
    int batteryPercent_4 = 10;
    int batteryWarningShowDuringTime = 7000;

    private batteryLevel batteryStatus;

    private enum batteryLevel {battery50, battery30, battery10}

    private ActivityMainBinding binding;
    private Boolean isGpsHotStart = false;
    private IMapController mapController;
    SystemData systemData = new SystemData();
    LocationCalculation locationCalculation = new LocationCalculation();

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        Context ctx = getApplicationContext();
        handler = new Handler();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        tvDroneLatitude = binding.tvDroneLatitude;
        tvDroneLongitude = binding.tvDroneLongitude;
        tvDroneMSL = binding.tvDroneMSL;
        tvDroneAGL = binding.tvDroneAGL;
        tvHomeLatitude = binding.tvHomeLatitude;
        tvHomeLongitude = binding.tvHomeLongitude;
        tvHomeMSL = binding.tvHomeMSL;
        tvDroneHorizontalVelocity = binding.tvDroneHorizontalVelocity;
        tvDroneVerticalVelocity = binding.tvDroneVerticalVelocity;
        tvDroneHeading = binding.tvDroneHeading;
        ivGpsFixed = binding.ivGPSFixed;
        tvDroneGPS = binding.tvDroneGPS;
        cr_BatteryWarning = binding.crBatteryWarning;
        ivBattery = binding.ivBattery;
        tvDroneBatteryVoltage = binding.tvBatteryVoltage;
        tvDroneBatteryPercent = binding.tvBatteryPercent;

        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));


        map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);
        mapController = map.getController();
        mapController.setZoom(7.0);

        GeoPoint startPoint = new GeoPoint(36.49334199577103, 36.22377838831611);
        mapController.setCenter(startPoint);
        droneMarker = new Marker(map);
        map.getOverlays().add(droneMarker);
//        droneMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.direction));
//        droneMarker.setPosition(new GeoPoint(-35.36335, 149.16524));

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        map.setMultiTouchControls(true);


        btnCenterAndTrack = binding.btnCenterAndTrack;
        btnNotCenterAndTrack=binding.btnNotCenterAndTrack;

        btnCenterAndTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRepeatingTask();
            }
        });

        btnNotCenterAndTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRepeatingTask();
            }
        });

        udpClientThread = new UDP_Service(this);
        udpClientThread.start();
        Button buttonOpenDialog = findViewById(R.id.buttonOpenDialog);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInputDialog();
            }
        });
    }
    protected void onPause() {
        super.onPause();
        stopRepeatingTask(); // Aktivite durduğunda tekrarlayan görevi durdur
    }
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
        if (udpClientThread != null && udpClientThread.isAlive()) {
            udpClientThread.interrupt();
        }

    }
    public void openInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.way_point_input_dialog, null);
        builder.setView(dialogView);
        edtTargetLatitude = dialogView.findViewById(R.id.edt_targetLatitude);
        edtTargetLongitude=dialogView.findViewById(R.id.edt_targetLongitude);
        Button buttonSend = dialogView.findViewById(R.id.buttonSend);
        edtTargetLatitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.isEmpty()) {
                    buttonSend.setEnabled(false);
                } else {
                    buttonSend.setEnabled(true);
                }
            }
        });
        edtTargetLongitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.isEmpty()) {
                    buttonSend.setEnabled(false);
                } else {
                    buttonSend.setEnabled(true);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String targetLatitudeInput = edtTargetLatitude.getText().toString().trim();
                String targetLongitudeInput = edtTargetLongitude.getText().toString().trim();
                byte[] sendingArray;
                byte[] targetLatitudeArray;
                byte[] targetLongitudeArray;
                if (isValidDoubleInput(targetLatitudeInput)) {
                    targetLatitudeArray = numberToLittleEndian(Float.parseFloat(targetLatitudeInput));

                } else {
                    edtTargetLatitude.setError("Invalid value!");
                    return;
                }
                if (isValidDoubleInput(targetLongitudeInput)) {
                    targetLongitudeArray= numberToLittleEndian(Float.parseFloat(targetLongitudeInput));
                } else {
                    edtTargetLongitude.setError("Invalid value!");
                    return;
                }
                if (isValidDoubleInput(targetLatitudeInput) && isValidDoubleInput(targetLongitudeInput)) {
                    sendingArray=combineArrays(targetLatitudeArray, targetLongitudeArray);
                    sendUDPMessage(sendingArray);
                }

                dialog.dismiss();
            }
        });
    }
    public void sendUDPMessage(byte [] message) {
        udpClientThread.sendData(message);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void DisplayData(byte[] telemetryData) {
        double latitude = systemData.getData(telemetryData).getDroneLatitude();
        double longitude = systemData.getData(telemetryData).getDroneLongitude();
        setDroneMarker(latitude, longitude);
        tvDroneLatitude.setText(String.format("%.5f",
                systemData.getData(telemetryData).getDroneLatitude()));
        tvDroneLongitude.setText(String.format("%.5f",
                systemData.getData(telemetryData).getDroneLongitude()));
        tvDroneMSL.setText(String.format("%.2f",
                systemData.getData(telemetryData).getDroneMSL()) + " M");
        tvDroneAGL.setText(String.format("%.2f",
                systemData.getData(telemetryData).getDroneAGL()) + " M");

        tvHomeLatitude.setText(String.format("%.5f",
                systemData.getData(telemetryData).getHomeLatitude()));
        tvHomeLongitude.setText(String.format("%.5f",
                systemData.getData(telemetryData).getHomeLongitude()));
        tvHomeMSL.setText(String.format("%.2f",
                systemData.getData(telemetryData).getHomeMSL()) + " M");

        tvDroneVerticalVelocity.setText(String.format("%.2f",
                systemData.getData(telemetryData).getDroneVerticalVelocity()) + " m/s");
        tvDroneHorizontalVelocity.setText(String.format("%.2f",
                systemData.getData(telemetryData).getDroneHorizontalVelocity()) + " m/s");

        tvDroneHeading.setText(String.format("%.2f", systemData.getData(telemetryData).getDroneHeading()));
        int GPSValue = systemData.getData(telemetryData).getDroneGPS();
        ivGpsFixed.setColorFilter(GPSIconColor(GPSValue));
        tvDroneGPS.setText(String.valueOf(GPSValue));
        tvDroneBatteryVoltage.setText(String.format("%.2f", systemData.getData(telemetryData).getDroneBatteryVoltage()));
        tvDroneBatteryPercent.setText("%"+ (systemData.getData(telemetryData).getDroneBatteryPercent()));
        int batterPercent = systemData.getData(telemetryData).getDroneBatteryPercent();
        droneBatteryStatusCheck(batterPercent);
        BatteryIconBar(batterPercent);
        ivBattery.setColorFilter(BatteryIconColor(batterPercent));
    }

    public int GPSIconColor(int GPSValue) {
        int color;
        if (GPSValue < 5) {
            color = ContextCompat.getColor(this, R.color.red);
        } else if (GPSValue <= 8) {
            color = ContextCompat.getColor(this, R.color.yellow);
        } else {
            color = ContextCompat.getColor(this, R.color.green);
        }
        return color;
    }


    public int BatteryIconColor(int BatteryValue) {
        int color;
        if (BatteryValue < 38) {
            color = ContextCompat.getColor(this, R.color.red);
        } else if (BatteryValue < 75) {
            color = ContextCompat.getColor(this, R.color.yellow);
        } else {
            color = ContextCompat.getColor(this, R.color.green);
        }
        return color;
    }

    public void BatteryIconBar(int BatteryValue) {
        if (BatteryValue < 13) {
            binding.ivBattery.setImageResource(R.drawable.battery_alert);
        } else if (BatteryValue < 25) {
            binding.ivBattery.setImageResource(R.drawable.battery_0);
        } else if (BatteryValue < 38) {
            binding.ivBattery.setImageResource(R.drawable.battery_1);
        } else if (BatteryValue < 50) {
            binding.ivBattery.setImageResource(R.drawable.battery_2);
        } else if (BatteryValue < 63) {
            binding.ivBattery.setImageResource(R.drawable.battery_3);
        } else if (BatteryValue < 75) {
            binding.ivBattery.setImageResource(R.drawable.battery_4);
        } else if (BatteryValue < 88) {
            binding.ivBattery.setImageResource(R.drawable.battery_5);
        } else if (BatteryValue < 100) {
            binding.ivBattery.setImageResource(R.drawable.battery_6);
        } else {
            binding.ivBattery.setImageResource(R.drawable.battery_full);
        }

    }

    public void droneBatteryStatusCheck(int batteryPercent) {
        //batteryColor(val);

        if (batteryPercent <= batteryPercent_1 && batteryPercent > batteryPercent_2 && batteryPercent_50) {
            batteryWarningShow = Snackbar.make(cr_BatteryWarning, "Battery is below 50%!", Snackbar.LENGTH_LONG)
                    .setTextColor(Color.YELLOW)
                    .setDuration(batteryWarningShowDuringTime);
            batteryPercent_50 = false;

            if (batteryStatus != batteryLevel.battery50) {
                batteryWarningShow.show();
                batteryStatus = batteryLevel.battery50;
            }

        }
        if (batteryPercent <= batteryPercent_2 && batteryPercent > batteryPercent_4 && batteryPercent_30) {
            batteryWarningShow = Snackbar.make(cr_BatteryWarning, "Battery is below 30%!", Snackbar.LENGTH_LONG)
                    .setTextColor(Color.YELLOW)
                    .setDuration(batteryWarningShowDuringTime);
            batteryPercent_30 = false;

            if (batteryStatus != batteryLevel.battery30) {
                batteryWarningShow.show();
                batteryStatus = batteryLevel.battery30;
            }

        }
        if (0 < batteryPercent && batteryPercent <= batteryPercent_4 && batteryPercent_10) {

            batteryWarningShow = Snackbar.make(cr_BatteryWarning, "Battery is below 10% !!!", Snackbar.LENGTH_INDEFINITE)
                    .setTextColor(Color.RED);

            if (batteryStatus != batteryLevel.battery10) {
                batteryWarningShow.show();
                batteryStatus = batteryLevel.battery10;
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDroneMarker(double latitude, double longitude) {

        if (droneMarker != null && latitude != 0 && longitude != 0) {

            if (!isGpsHotStart) {
                mapController.setCenter(new GeoPoint(latitude, longitude));
                isGpsHotStart = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        map.getController().animateTo(new GeoPoint(latitude, longitude));
                        mapController.setZoom(19.0);
                    }
                }, 300);
            }
            droneMarker.setPosition(new GeoPoint(latitude, longitude));
            map.getOverlays().add(droneMarker);

            droneMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.direction));
            droneMarker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_LEFT);

            map.invalidate();
        }
    }

    void startRepeatingTask() {
        runnable = new Runnable() {
            @Override
            public void run() {
                centerAndTrackDrone();
                handler.postDelayed(this, 500); // 500ms aralıklarla tekrarla
            }
        };
        handler.post(runnable);
    }

    void stopRepeatingTask() {
        handler.removeCallbacks(runnable);
    }

    private void centerAndTrackDrone() {
        if (droneMarker != null) {
            GeoPoint droneLocation = droneMarker.getPosition();
            mapController.animateTo(droneLocation);
            mapController.setZoom(19.0);
            isGpsHotStart = true;
        }
    }
    private boolean isValidDoubleInput(String input) {

        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static byte[] numberToLittleEndian(float number) {

        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putFloat(number);
        return buffer.array();
    }
    public static byte[] combineArrays(byte[] array1, byte[] array2) {
        int totalLength = array1.length + array2.length;
        byte[] combinedArray = new byte[totalLength];
        //Arrays
        System.arraycopy(array1, 0, combinedArray, 0, array1.length);
        System.arraycopy(array2, 0, combinedArray, array1.length, array2.length);

        return combinedArray;
    }

}
