package org.icddrb.listinggps;

//Android Manifest Code
//<activity android:name=".GPSLandmark" android:label="GPSLandmark" />

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.Manifest;
import android.app.*;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import Common.*;

import static android.R.attr.data;

public class GPSLandmark extends Activity {
    boolean networkAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;

    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event) {
        if (iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        } else {
            return true;
        }
    }

    String VariableID;
    private int hour;
    private int minute;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;

    Connection C;
    Global g;
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static String TableName;

    TextView lblHeading;
    LinearLayout secProjId;
    View lineProjId;
    TextView VlblProjId;
    EditText txtProjId;
    LinearLayout secVCode;
    View lineVCode;
    TextView VlblVCode;
    EditText txtVCode;
    LinearLayout secParaName;
    View lineParaName;
    TextView VlblParaName;
    EditText txtParaName;
    LinearLayout secLMNo;
    View lineLMNo;
    TextView VlblLMNo;
    EditText txtLMNo;
    LinearLayout secLMName;
    View lineLMName;
    TextView VlblLMName;
    EditText txtLMName;
    LinearLayout seclblLatitude;
    View linelblLatitude;
    LinearLayout seclatDeg;
    View linelatDeg;
    TextView VlbllatDeg;
    EditText txtlatDeg;
    LinearLayout seclatMin;
    View linelatMin;
    TextView VlbllatMin;
    EditText txtlatMin;
    LinearLayout seclatSec;
    View linelatSec;
    TextView VlbllatSec;
    EditText txtlatSec;
    LinearLayout seclblLongitude;
    View linelblLongitude;
    LinearLayout seclonDeg;
    View linelonDeg;
    TextView VlbllonDeg;
    EditText txtlonDeg;
    LinearLayout seclonMin;
    View linelonMin;
    TextView VlbllonMin;
    EditText txtlonMin;
    LinearLayout seclonSec;
    View linelonSec;
    TextView VlbllonSec;
    EditText txtlonSec;

    TextView VlblVName;
    LinearLayout secWayPnt;
    View lineWayPnt;
    TextView VlblWayPnt;
    EditText txtWayPnt;

    String StartTime;
    Bundle IDbundle;
    static String PROJID = "";
    static String VCODE = "";
    static String LMNO = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.gpslandmark);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            IDbundle = getIntent().getExtras();
            PROJID = IDbundle.getString("ProjId");
            VCODE = IDbundle.getString("VCode");
            LMNO = IDbundle.getString("LMNo");

            TableName = "GPSLandmark";

            if (LMNO.equals("")) {
                LMNO = LMNoSerial();
            }

            //turnGPSOn();

            //GPS Location
            //FindLocation();
            // Double.toString(currentLatitude);
            // Double.toString(currentLongitude);
            lblHeading = (TextView) findViewById(R.id.lblHeading);

            ImageButton cmdBack = (ImageButton) findViewById(R.id.cmdBack);
            cmdBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(GPSLandmark.this);
                    adb.setTitle("Close");
                    adb.setMessage("Do you want to close this form[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    adb.show();
                }
            });


            secProjId = (LinearLayout) findViewById(R.id.secProjId);
            lineProjId = (View) findViewById(R.id.lineProjId);
            VlblProjId = (TextView) findViewById(R.id.VlblProjId);
            txtProjId = (EditText) findViewById(R.id.txtProjId);
            secVCode = (LinearLayout) findViewById(R.id.secVCode);
            lineVCode = (View) findViewById(R.id.lineVCode);
            VlblVCode = (TextView) findViewById(R.id.VlblVCode);
            txtVCode = (EditText) findViewById(R.id.txtVCode);
            secParaName = (LinearLayout) findViewById(R.id.secParaName);
            lineParaName = (View) findViewById(R.id.lineParaName);
            VlblParaName = (TextView) findViewById(R.id.VlblParaName);
            txtParaName = (EditText) findViewById(R.id.txtParaName);
            secLMNo = (LinearLayout) findViewById(R.id.secLMNo);
            lineLMNo = (View) findViewById(R.id.lineLMNo);
            VlblLMNo = (TextView) findViewById(R.id.VlblLMNo);
            txtLMNo = (EditText) findViewById(R.id.txtLMNo);
            secLMName = (LinearLayout) findViewById(R.id.secLMName);
            lineLMName = (View) findViewById(R.id.lineLMName);
            VlblLMName = (TextView) findViewById(R.id.VlblLMName);
            txtLMName = (EditText) findViewById(R.id.txtLMName);
            seclblLatitude = (LinearLayout) findViewById(R.id.seclblLatitude);
            linelblLatitude = (View) findViewById(R.id.linelblLatitude);
            seclatDeg = (LinearLayout) findViewById(R.id.seclatDeg);
            linelatDeg = (View) findViewById(R.id.linelatDeg);
            VlbllatDeg = (TextView) findViewById(R.id.VlbllatDeg);
            txtlatDeg = (EditText) findViewById(R.id.txtlatDeg);
            seclatMin = (LinearLayout) findViewById(R.id.seclatMin);
            linelatMin = (View) findViewById(R.id.linelatMin);
            VlbllatMin = (TextView) findViewById(R.id.VlbllatMin);
            txtlatMin = (EditText) findViewById(R.id.txtlatMin);
            seclatSec = (LinearLayout) findViewById(R.id.seclatSec);
            linelatSec = (View) findViewById(R.id.linelatSec);
            VlbllatSec = (TextView) findViewById(R.id.VlbllatSec);
            txtlatSec = (EditText) findViewById(R.id.txtlatSec);
            seclblLongitude = (LinearLayout) findViewById(R.id.seclblLongitude);
            linelblLongitude = (View) findViewById(R.id.linelblLongitude);
            seclonDeg = (LinearLayout) findViewById(R.id.seclonDeg);
            linelonDeg = (View) findViewById(R.id.linelonDeg);
            VlbllonDeg = (TextView) findViewById(R.id.VlbllonDeg);
            txtlonDeg = (EditText) findViewById(R.id.txtlonDeg);
            seclonMin = (LinearLayout) findViewById(R.id.seclonMin);
            linelonMin = (View) findViewById(R.id.linelonMin);
            VlbllonMin = (TextView) findViewById(R.id.VlbllonMin);
            txtlonMin = (EditText) findViewById(R.id.txtlonMin);
            seclonSec = (LinearLayout) findViewById(R.id.seclonSec);
            linelonSec = (View) findViewById(R.id.linelonSec);
            VlbllonSec = (TextView) findViewById(R.id.VlbllonSec);
            txtlonSec = (EditText) findViewById(R.id.txtlonSec);

            //**************************added by sakib***************************

            secWayPnt = (LinearLayout) findViewById(R.id.secWayPnt);
            lineWayPnt = (View) findViewById(R.id.lineWayPnt);
            VlblWayPnt = (TextView) findViewById(R.id.VlblWayPnt);
            txtWayPnt = (EditText) findViewById(R.id.txtWayPnt);

            VlblVName= (TextView) findViewById(R.id.VlblVName);
            VlblVName.setText(IDbundle.getString("VName"));

            txtLMNo.setText(LMNO);

            if (!PROJID.equals("")) {
                txtProjId.setText(PROJID);
            }

            if (!VCODE.equals("")) {
                txtVCode.setText(VCODE);
            }

            //**************************added by sakib***************************


            //Hide all skip variables


            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });

            DataSearch(PROJID, VCODE, LMNO);
        } catch (Exception e) {
            Connection.MessageBox(GPSLandmark.this, e.getMessage());
            return;
        }
    }

    private String LMNoSerial() {
        String SL = C.ReturnSingleValue("Select (ifnull(max(cast(LMNo as int)),0)+1)SL from GPSLandmark where VCode='"+VCODE+"'");  //where ParticipantID='"+ ParticipantID +"'");

        int length = SL.length();
        String s = "";
        for (int i = 0; i < 4 - length; i++) {
            s += "0";
        }
        SL = s + SL;


        return SL;
    }

    private void DataSave() {
        try {

            String DV = "";

            if (txtProjId.getText().toString().length() == 0 & secProjId.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Project Id.");
                txtProjId.requestFocus();
                return;
            } else if (Integer.valueOf(txtProjId.getText().toString().length() == 0 ? "1" : txtProjId.getText().toString()) < 1 || Integer.valueOf(txtProjId.getText().toString().length() == 0 ? "99999" : txtProjId.getText().toString()) > 99999) {
                Connection.MessageBox(GPSLandmark.this, "Value should be between 1 and 99999(Project Id).");
                txtProjId.requestFocus();
                return;
            } else if (txtVCode.getText().toString().length() == 0 & secVCode.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Village Code.");
                txtVCode.requestFocus();
                return;
            } else if (txtParaName.getText().toString().length() == 0 & secParaName.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Para Name.");
                txtParaName.requestFocus();
                return;
            } else if (txtLMNo.getText().toString().length() == 0 & secLMNo.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Landmark No.");
                txtLMNo.requestFocus();
                return;
            } else if (txtLMName.getText().toString().length() == 0 & secLMName.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Landmark Name.");
                txtLMName.requestFocus();
                return;
            } else if (txtlatDeg.getText().toString().length() == 0 & seclatDeg.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Degree.");
                txtlatDeg.requestFocus();
                return;
            } else if (txtlatMin.getText().toString().length() == 0 & seclatMin.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Minuet.");
                txtlatMin.requestFocus();
                return;
            } else if (txtlatSec.getText().toString().length() == 0 & seclatSec.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Second.");
                txtlatSec.requestFocus();
                return;
            } else if (txtlonDeg.getText().toString().length() == 0 & seclonDeg.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Degree.");
                txtlonDeg.requestFocus();
                return;
            } else if (txtlonMin.getText().toString().length() == 0 & seclonMin.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Minuet.");
                txtlonMin.requestFocus();
                return;
            } else if (txtlonSec.getText().toString().length() == 0 & seclonSec.isShown()) {
                Connection.MessageBox(GPSLandmark.this, "Required field: Second.");
                txtlonSec.requestFocus();
                return;
            }else if(txtWayPnt.getText().toString().length()==0 & secWayPnt.isShown()){
                Connection.MessageBox(GPSLandmark.this, "Required field: Way Point.");
                txtWayPnt.requestFocus();
                return;
            }else if(Integer.parseInt(txtlatMin.getText().toString())>=60 & secWayPnt.isShown()){
                Connection.MessageBox(GPSLandmark.this, "Latitude : Minute should be less than 60.");
                txtlatMin.requestFocus();
                return;
            }else if(Integer.parseInt(txtlonMin.getText().toString())>=60 & secWayPnt.isShown()){
                Connection.MessageBox(GPSLandmark.this, "Longitude : Minute should be less than 60.");
                txtlonMin.requestFocus();
                return;
            }

            String SQL = "";
            RadioButton rb;

            GPSLandmark_DataModel objSave = new GPSLandmark_DataModel();
            objSave.setProjId(txtProjId.getText().toString());
            objSave.setVCode(txtVCode.getText().toString());
            objSave.setParaName(txtParaName.getText().toString());
            objSave.setLMNo(txtLMNo.getText().toString());

            objSave.setWayPnt(txtWayPnt.getText().toString());

            objSave.setLMName(txtLMName.getText().toString());
            objSave.setlatDeg(txtlatDeg.getText().toString());
            objSave.setlatMin(txtlatMin.getText().toString());
            objSave.setlatSec(txtlatSec.getText().toString());
            objSave.setlonDeg(txtlonDeg.getText().toString());
            objSave.setlonMin(txtlonMin.getText().toString());
            objSave.setlonSec(txtlonSec.getText().toString());
            objSave.setEnDt(Global.DateTimeNowYMDHMS());
            objSave.setStartTime(StartTime);
            objSave.setEndTime(g.CurrentTime24());
            objSave.setUserId(g.getUserId());
            objSave.setEntryUser(g.getUserId()); //from data entry user list
            //objSave.setLat(Double.toString(currentLatitude));
            //objSave.setLon(Double.toString(currentLongitude));

            String status = objSave.SaveUpdateData(this);
            if (status.length() == 0) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("res", "");
                setResult(Activity.RESULT_OK, returnIntent);
                Connection.MessageBox(GPSLandmark.this, "Saved Successfully");

            } else {
                Connection.MessageBox(GPSLandmark.this, status);
                return;
            }
        } catch (Exception e) {
            Connection.MessageBox(GPSLandmark.this, e.getMessage());
            return;
        }
    }

    private void DataSearch(String ProjId, String VCode, String LMNo) {
        try {

            RadioButton rb;
            GPSLandmark_DataModel d = new GPSLandmark_DataModel();
            String SQL = "Select * from " + TableName + "  Where ProjId='" + ProjId + "' and VCode='" + VCode + "' and LMNo='" + LMNo + "'";
            List<GPSLandmark_DataModel> data = d.SelectAll(this, SQL);
            for (GPSLandmark_DataModel item : data) {
                txtProjId.setText(item.getProjId());
                txtVCode.setText(item.getVCode());
                txtParaName.setText(item.getParaName());
                txtLMNo.setText(item.getLMNo());
                txtLMName.setText(item.getLMName());
                txtlatDeg.setText(item.getlatDeg());
                txtlatMin.setText(item.getlatMin());
                txtlatSec.setText(item.getlatSec());
                txtlonDeg.setText(item.getlonDeg());
                txtlonMin.setText(item.getlonMin());
                txtlonSec.setText(item.getlonSec());
                txtWayPnt.setText(item.getWayPnt());
            }
        } catch (Exception e) {
            Connection.MessageBox(GPSLandmark.this, e.getMessage());
            return;
        }
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, g.mYear, g.mMonth - 1, g.mDay);
            case TIME_DIALOG:
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            EditText dtpDate = null;


            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime = null;


            tpTime.setText(new StringBuilder().append(Global.Right("00" + hour, 2)).append(":").append(Global.Right("00" + minute, 2)));

        }
    };


    //GPS Reading
    //.....................................................................................................
    public void FindLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    void updateLocation(Location location) {
        currentLocation  = location;
        currentLatitude  = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();
    }


    // Method to turn on GPS
    public void turnGPSOn(){
        try
        {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(!provider.contains("gps")){ //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        }
        catch (Exception e) {
        }
    }

    // Method to turn off the GPS
    public void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }
}