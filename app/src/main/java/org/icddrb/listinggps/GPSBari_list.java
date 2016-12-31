package org.icddrb.listinggps;


//Android Manifest Code
//<activity android:name=".GPSBari_list" android:label="GPSBari: List" />
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import Common.*;

public class GPSBari_list extends Activity {
    boolean networkAvailable=false;
    Location currentLocation;
    double currentLatitude,currentLongitude;
    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME)
        { return false; }
        else { return true;  }
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
    Button btnAdd;
    Button btnRefresh;

    //************added by sakib***************
    TextView tvUnion,tvVillage;
    Spinner spnUnion,spnVillage;

    RadioGroup rdogrpBLD;

    RadioButton rdoBari,rdoLandmark,rdoPara;

    //************added by sakib***************

    String StartTime;
    static String PROJID = "";
    static String VCODE = "";
    static String BARINO = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.gpsbari_list);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            TableName = "GPSBari";

            lblHeading = (TextView)findViewById(R.id.lblHeading);
            lblHeading.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT  = 2;
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        if(event.getRawX() >= (lblHeading.getRight() - lblHeading.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(GPSBari_list.this);
                            adb.setTitle("Close");
                            adb.setMessage("Do you want to close this form[Yes/No]?");
                            adb.setNegativeButton("No", null);
                            adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }});
                            adb.show();
                            return true;
                        }
                    }
                    return false;
                }
            });

            ImageButton cmdBack = (ImageButton) findViewById(R.id.cmdBack);
            cmdBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(GPSBari_list.this);
                    adb.setTitle("Close");
                    adb.setMessage("Do you want to close this form[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }});
                    adb.show();
                }});

            btnRefresh = (Button) findViewById(R.id.btnRefresh);
            btnRefresh.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    //write your code here
                    DataSearch(PROJID, VCODE, BARINO);

                }});

            btnAdd   = (Button) findViewById(R.id.btnAdd);
            btnAdd.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("ProjId", "");
                    IDbundle.putString("VCode", "");
                    IDbundle.putString("BariNo", "");


                    String[] d_rdogrpElectricity = new String[]{"1", "2", "3"};

                    int rb = rdogrpBLD.getCheckedRadioButtonId();

                    Intent intent = null;

                    switch(rb)
                    {
                        case R.id.rdoBari:
                            intent = new Intent(getApplicationContext(), GPSBari.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtras(IDbundle);
                            break;
                        case R.id.rdoLandmark:
                            Toast.makeText(GPSBari_list.this, "Landmark is Selected", Toast.LENGTH_SHORT).show();
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtras(IDbundle);
                            break;
                        case R.id.rdoPara:
                            Toast.makeText(GPSBari_list.this, "Para is Selected", Toast.LENGTH_SHORT).show();
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtras(IDbundle);
                            break;
                        default:
                            Toast.makeText(GPSBari_list.this, "Nothing is Selected", Toast.LENGTH_SHORT).show();
                            break;
                    }

                    if(rb!= -1)
                    {
                        getApplicationContext().startActivity(intent);
                    }


                }});


            //**************************added by sakib***************************


            tvUnion= (TextView) findViewById(R.id.tvUnion);
            tvVillage= (TextView) findViewById(R.id.tvVillage);

            spnUnion= (Spinner) findViewById(R.id.spnUnion);
            spnVillage= (Spinner) findViewById(R.id.spnVillage);

            rdogrpBLD= (RadioGroup) findViewById(R.id.rdogrpBLD);

            rdoBari= (RadioButton) findViewById(R.id.rdoBari);
            rdoLandmark= (RadioButton) findViewById(R.id.rdoLandmark);
            rdoPara= (RadioButton) findViewById(R.id.rdoPara);

            spnUnion.setAdapter( (C.getArrayAdapter("Select '' Union Select distinct UnName||'-'||UnName from Village union Select '99-Others'")));
            spnUnion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnUnion.getSelectedItem().toString().length() == 0) return;
                    spnVillage.setAdapter(C.getArrayAdapter("Select '  ' Union  Select distinct VName from Village where UnName='"+ Connection.SelectedSpinnerValue(spnUnion.getSelectedItem().toString(),"-")+"'  union Select '999-Others'"));
                    //spnVillage.setAdapter(C.getArrayAdapter("Select VName from Village where UnName='"+ Connection.SelectedSpinnerValue(spnUnion.getSelectedItem().toString(),"-")+"'  union Select '999-Others'"));
                    if(Connection.SelectedSpinnerValue(spnUnion.getSelectedItem().toString(),"-").equals("999"))
                        spnVillage.setSelection(Global.SpinnerItemPositionAnyLength(spnVillage,"999"));
                    else
                        spnVillage.setSelection(Global.SpinnerItemPositionAnyLength(spnVillage,""));


                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            rdogrpBLD.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId)
                    {
                        case R.id.rdoBari:

                            btnAdd.setText("New "+rdoBari.getText());
                            break;
                        case R.id.rdoLandmark:
                            btnAdd.setText("New "+rdoLandmark.getText());
                            break;
                        case R.id.rdoPara:
                            btnAdd.setText("New "+rdoPara.getText());
                            break;
                    }
                }
            });


            //**************************added by sakib***************************


            DataSearch(PROJID, VCODE, BARINO);


        }
        catch(Exception  e)
        {
            Connection.MessageBox(GPSBari_list.this, e.getMessage());
            return;
        }
    }
    private void DataSearch(String ProjId, String VCode, String BariNo)
    {
        try
        {

            GPSBari_DataModel d = new GPSBari_DataModel();
            String SQL = "Select * from "+ TableName +"  Where ProjId='"+ ProjId +"' and VCode='"+ VCode +"' and BariNo='"+ BariNo +"'";
            List<GPSBari_DataModel> data = d.SelectAll(this, SQL);
            dataList.clear();

            dataAdapter = null;

            ListView list = (ListView)findViewById(R.id.lstData);
            HashMap<String, String> map;

            for(GPSBari_DataModel item : data){
                map = new HashMap<String, String>();
                map.put("ProjId", item.getProjId());
                map.put("VCode", item.getVCode());
                map.put("ParaName", item.getParaName());
                map.put("BariNo", item.getBariNo());
                map.put("BariName", item.getBariName());
                map.put("TotalHH", item.getTotalHH());
                map.put("latDeg", item.getlatDeg());
                map.put("latMin", item.getlatMin());
                map.put("latSec", item.getlatSec());
                map.put("lonDeg", item.getlonDeg());
                map.put("lonMin", item.getlonMin());
                map.put("lonSec", item.getlonSec());
                map.put("HCLoction", item.getHCLoction());
                dataList.add(map);
            }
            dataAdapter = new SimpleAdapter(GPSBari_list.this, dataList, R.layout.gpsbari_list,new String[] {"rowsec"},
                    new int[] {R.id.secListRow});
            list.setAdapter(new DataListAdapter(this, dataAdapter));
        }
        catch(Exception  e)
        {
            Connection.MessageBox(GPSBari_list.this, e.getMessage());
            return;
        }
    }


    public class DataListAdapter extends BaseAdapter
    {
        private Context context;
        private SimpleAdapter dataAdap;

        public DataListAdapter(Context c, SimpleAdapter da){ context = c;  dataAdap = da; }
        public int getCount() {  return dataAdap.getCount();  }
        public Object getItem(int position) {  return position;  }
        public long getItemId(int position) {  return position;  }
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gpsbari_row, null);
            }
            LinearLayout   secListRow = (LinearLayout)convertView.findViewById(R.id.secListRow);

            final TextView ProjId = (TextView)convertView.findViewById(R.id.ProjId);
            final TextView VCode = (TextView)convertView.findViewById(R.id.VCode);
            final TextView ParaName = (TextView)convertView.findViewById(R.id.ParaName);
            final TextView BariNo = (TextView)convertView.findViewById(R.id.BariNo);
            final TextView BariName = (TextView)convertView.findViewById(R.id.BariName);
            final TextView TotalHH = (TextView)convertView.findViewById(R.id.TotalHH);
            final TextView latDeg = (TextView)convertView.findViewById(R.id.latDeg);
            final TextView latMin = (TextView)convertView.findViewById(R.id.latMin);
            final TextView latSec = (TextView)convertView.findViewById(R.id.latSec);
            final TextView lonDeg = (TextView)convertView.findViewById(R.id.lonDeg);
            final TextView lonMin = (TextView)convertView.findViewById(R.id.lonMin);
            final TextView lonSec = (TextView)convertView.findViewById(R.id.lonSec);
            final TextView HCLoction = (TextView)convertView.findViewById(R.id.HCLoction);

            final HashMap<String, String> o = (HashMap<String, String>) dataAdap.getItem(position);
            ProjId.setText(o.get("ProjId"));
            VCode.setText(o.get("VCode"));
            ParaName.setText(o.get("ParaName"));
            BariNo.setText(o.get("BariNo"));
            BariName.setText(o.get("BariName"));
            TotalHH.setText(o.get("TotalHH"));
            latDeg.setText(o.get("latDeg"));
            latMin.setText(o.get("latMin"));
            latSec.setText(o.get("latSec"));
            lonDeg.setText(o.get("lonDeg"));
            lonMin.setText(o.get("lonMin"));
            lonSec.setText(o.get("lonSec"));
            HCLoction.setText(o.get("HCLoction"));

            secListRow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Write your code here
                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("ProjId", o.get("ProjId"));
                    IDbundle.putString("VCode", o.get("VCode"));
                    IDbundle.putString("BariNo", o.get("BariNo"));
                    Intent f1;
                    f1 = new Intent(getApplicationContext(), GPSBari.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            });


            return convertView;
        }
    }


}