package org.icddrb.listinggps;

/**
 * Created by mchd.dms on 29-Dec-16.
 */


import android.content.Context;
import android.database.Cursor;
import Common.Connection;
import java.util.ArrayList;
import java.util.List;
public class GPSBari_DataModel{

    private String _ProjId = "";
    public String getProjId(){
        return _ProjId;
    }
    public void setProjId(String newValue){
        _ProjId = newValue;
    }
    private String _VCode = "";
    public String getVCode(){
        return _VCode;
    }
    public void setVCode(String newValue){
        _VCode = newValue;
    }


    //**************************added by sakib***************************
    private String _WayPnt = "";
    public String getWayPnt(){
        return _WayPnt;
    }
    public void setWayPnt(String newValue){
        _WayPnt = newValue;
    }

    //**************************added by sakib***************************

    private String _ParaName = "";
    public String getParaName(){
        return _ParaName;
    }
    public void setParaName(String newValue){
        _ParaName = newValue;
    }
    private String _BariNo = "";
    public String getBariNo(){
        return _BariNo;
    }
    public void setBariNo(String newValue){
        _BariNo = newValue;
    }
    private String _BariName = "";
    public String getBariName(){
        return _BariName;
    }
    public void setBariName(String newValue){
        _BariName = newValue;
    }
    private String _TotalHH = "";
    public String getTotalHH(){
        return _TotalHH;
    }
    public void setTotalHH(String newValue){
        _TotalHH = newValue;
    }
    private String _latDeg = "";
    public String getlatDeg(){
        return _latDeg;
    }
    public void setlatDeg(String newValue){
        _latDeg = newValue;
    }
    private String _latMin = "";
    public String getlatMin(){
        return _latMin;
    }
    public void setlatMin(String newValue){
        _latMin = newValue;
    }
    private String _latSec = "";
    public String getlatSec(){
        return _latSec;
    }
    public void setlatSec(String newValue){
        _latSec = newValue;
    }
    private String _lonDeg = "";
    public String getlonDeg(){
        return _lonDeg;
    }
    public void setlonDeg(String newValue){
        _lonDeg = newValue;
    }
    private String _lonMin = "";
    public String getlonMin(){
        return _lonMin;
    }
    public void setlonMin(String newValue){
        _lonMin = newValue;
    }
    private String _lonSec = "";
    public String getlonSec(){
        return _lonSec;
    }
    public void setlonSec(String newValue){
        _lonSec = newValue;
    }
    private String _HCLoction = "";
    public String getHCLoction(){
        return _HCLoction;
    }
    public void setHCLoction(String newValue){
        _HCLoction = newValue;
    }
    private String _StartTime = "";
    public void setStartTime(String newValue){
        _StartTime = newValue;
    }
    private String _EndTime = "";
    public void setEndTime(String newValue){
        _EndTime = newValue;
    }
    private String _UserId = "";
    public void setUserId(String newValue){
        _UserId = newValue;
    }
    private String _EntryUser = "";
    public void setEntryUser(String newValue){
        _EntryUser = newValue;
    }
    private String _Lat = "";
    public void setLat(String newValue){
        _Lat = newValue;
    }
    private String _Lon = "";
    public void setLon(String newValue){
        _Lon = newValue;
    }
    private String _EnDt = "";
    public void setEnDt(String newValue){
        _EnDt = newValue;
    }
    private String _Upload = "2";

    private String _HCLoction1 = "";
    public String getHCLoction1(){
        return _HCLoction1;
    }
    public void setHCLoction1(String newValue){
        _HCLoction1 = newValue;
    }

    private String _HCLoction2 = "";
    public String getHCLoction2(){
        return _HCLoction2;
    }
    public void setHCLoction2(String newValue){
        _HCLoction2 = newValue;
    }



    String TableName = "GPSBari";

    public String SaveUpdateData(Context context)
    {
        String response = "";
        C = new Connection(context);
        String SQL = "";
        try
        {
            if(C.Existence("Select * from "+ TableName +"  Where ProjId='"+ _ProjId +"' and VCode='"+ _VCode +"' and BariNo='"+ _BariNo +"' "))
                response = UpdateData(context);
            else
                response = SaveData(context);
        }
        catch(Exception  e)
        {
            response = e.getMessage();
        }
        return response;
    }
    Connection C;

    private String SaveData(Context context)
    {
        String response = "";
        C = new Connection(context);
        String SQL = "";
        try
        {
            //SQL = "Insert into "+ TableName +" (ProjId,VCode,ParaName,BariNo,BariName,TotalHH,latDeg,latMin,latSec,lonDeg,lonMin,lonSec,HCLoction,StartTime,EndTime,UserId,EntryUser,Lat,Lon,EnDt,Upload)Values('"+ _ProjId +"', '"+ _VCode +"', '"+ _ParaName +"', '"+ _BariNo +"', '"+ _BariName +"', '"+ _TotalHH +"', '"+ _latDeg +"', '"+ _latMin +"', '"+ _latSec +"', '"+ _lonDeg +"', '"+ _lonMin +"', '"+ _lonSec +"', '"+ _HCLoction +"', '"+ _StartTime +"', '"+ _EndTime +"', '"+ _UserId +"', '"+ _EntryUser +"', '"+ _Lat +"', '"+ _Lon +"', '"+ _EnDt +"', '"+ _Upload +"')";
            SQL = "Insert into "+ TableName +" (ProjId,VCode,ParaName,BariNo,WayPoint,BariName,TotalHH,latDeg,latMin,latSec,lonDeg,lonMin,lonSec,HCLoction,HCLoction1,HCLoction2,StartTime,EndTime,UserId,EntryUser,Lat,Lon,EnDt,Upload)Values('"+ _ProjId +"', '"+ _VCode +"', '"+ _ParaName +"', '"+ _BariNo +"', '"+_WayPnt+"', '"+ _BariName +"', '"+ _TotalHH +"', '"+ _latDeg +"', '"+ _latMin +"', '"+ _latSec +"', '"+ _lonDeg +"', '"+ _lonMin +"', '"+ _lonSec +"', '"+ _HCLoction+"', '"+ _HCLoction1+"', '"+ _HCLoction2 +"', '"+ _StartTime +"', '"+ _EndTime +"', '"+ _UserId +"', '"+ _EntryUser +"', '"+ _Lat +"', '"+ _Lon +"', '"+ _EnDt +"', '"+ _Upload +"')";
            C.Save(SQL);
            C.close();
        }
        catch(Exception  e)
        {
            response = e.getMessage();
        }
        return response;
    }

    private String UpdateData(Context context)
    {
        String response = "";
        C = new Connection(context);
        String SQL = "";
        try
        {
            SQL = "Update "+ TableName +" Set Upload='2',ProjId = '"+ _ProjId +"',VCode = '"+ _VCode +"',ParaName = '"+ _ParaName +"',BariNo = '"+ _BariNo +"',WayPoint = '"+ _WayPnt +"',BariName = '"+ _BariName +"',TotalHH = '"+ _TotalHH +"',latDeg = '"+ _latDeg +"',latMin = '"+ _latMin +"',latSec = '"+ _latSec +"',lonDeg = '"+ _lonDeg +"',lonMin = '"+ _lonMin +"',lonSec = '"+ _lonSec +"',HCLoction = '"+ _HCLoction  +"',HCLoction1 = '"+ _HCLoction1 +"',HCLoction2 = '"+ _HCLoction2  +"'  Where ProjId='"+ _ProjId +"' and VCode='"+ _VCode +"' and BariNo='"+ _BariNo +"'";
            C.Save(SQL);
            C.close();
        }
        catch(Exception  e)
        {
            response = e.getMessage();
        }
        return response;
    }


    public List<GPSBari_DataModel> SelectAll(Context context, String SQL)
    {
        Connection C = new Connection(context);
        List<GPSBari_DataModel> data = new ArrayList<GPSBari_DataModel>();
        GPSBari_DataModel d = new GPSBari_DataModel();
        Cursor cur = C.ReadData(SQL);

        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            d = new GPSBari_DataModel();
            d._ProjId = cur.getString(cur.getColumnIndex("ProjId"));
            d._VCode = cur.getString(cur.getColumnIndex("VCode"));
            d._ParaName = cur.getString(cur.getColumnIndex("ParaName"));
            d._BariNo = cur.getString(cur.getColumnIndex("BariNo"));
            d._WayPnt = cur.getString(cur.getColumnIndex("WayPoint"));
            d._BariName = cur.getString(cur.getColumnIndex("BariName"));
            d._TotalHH = cur.getString(cur.getColumnIndex("TotalHH"));
            d._latDeg = cur.getString(cur.getColumnIndex("latDeg"));
            d._latMin = cur.getString(cur.getColumnIndex("latMin"));
            d._latSec = cur.getString(cur.getColumnIndex("latSec"));
            d._lonDeg = cur.getString(cur.getColumnIndex("lonDeg"));
            d._lonMin = cur.getString(cur.getColumnIndex("lonMin"));
            d._lonSec = cur.getString(cur.getColumnIndex("lonSec"));
            d._HCLoction = cur.getString(cur.getColumnIndex("HCLoction"));
            d._HCLoction1 = cur.getString(cur.getColumnIndex("HCLoction1"));
            d._HCLoction2 = cur.getString(cur.getColumnIndex("HCLoction2"));
            data.add(d);

            cur.moveToNext();
        }
        cur.close();
        return data;
    }
}
