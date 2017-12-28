package com.demo.test.util;

import android.text.TextUtils;

import com.demo.test.db.City;
import com.demo.test.db.County;
import com.demo.test.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lx on 2017/12/22.
 */

public class Utilty {
    /*
    * 解析服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i <allProvince.length() ; i++) {
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province provnce = new Province();
                    provnce.setProvinceName(provinceObject.getString("name"));
                    provnce.setProvinceCode(provinceObject.getInt("id"));
                    provnce.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    解析服务器返会的市级数据;
    * */
    public static boolean handleCtyResponse(String response ,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i <allCities.length() ; i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCiryName(cityObject.getString("name"));
                    city.setCiryCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
     解析服务器返会的县级数据;
     * */
    public static boolean handleCountyResponse(String response ,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCountys = new JSONArray(response);
                for (int i = 0; i <allCountys.length() ; i++) {
                    JSONObject countyObject = allCountys.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId("weather_id");
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
