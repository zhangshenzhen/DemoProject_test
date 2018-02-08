package com.demo.test.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.test.R;
import com.demo.test.db.City;
import com.demo.test.db.County;
import com.demo.test.db.Province;
import com.demo.test.util.HttpUtil;
import com.demo.test.util.Utilty;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lx on 2017/12/23.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE =0;
    public static final int LEVEL_CTY =1;
    public static final int LEVEL_COUNTY =2;

    private ProgressDialog progressDialog;
    private TextView titletext;
    private Button backbutton;
    private ArrayAdapter<String>  adapter;
    private ListView listView;

    private List<String>  dataList = new ArrayList<>();
    /*
    省列表
    * */
    private List<Province> provinceList;
    /*市列表
    * */
    private List<City> cityList;
    /*县列表
    * */
    private List<County> countyList;

    /*
    * 选中的省*/
    private Province  selectProvince;
      /*
    * 选中的市*/
      private City  selectcity;
      /*
    * 选中的县*/
      private County  selectcounty;
    /*
    * 当前的级别*/
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //填充布局
       View view = inflater.inflate(R.layout.choose_are,container,false);
        //查找控件
        titletext = view.findViewById(R.id.title_text);
        backbutton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        //适配器
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             if(currentLevel ==LEVEL_PROVINCE){
                 selectProvince = provinceList.get(position);
                 queryCitys(); //省下面查市
             }else if(currentLevel==LEVEL_CTY){
                 selectcity = cityList.get(position);
                 queryCountys();//市下面查县
              }
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel ==LEVEL_COUNTY){
                    queryCitys();
                }else if(currentLevel==LEVEL_CTY){
                    queryProvince();
                }
            }
        });
        queryProvince();
    }

    private void queryProvince() {
        titletext.setText("中国");
        backbutton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String  address ="http://guolin.tech.api.china";
            queryFromServer(address ,"province");
        }

    }

    private void queryCountys() {
        titletext.setText(selectcity.getCiryName());
        backbutton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectcity.getId())).find(County.class
        );
        if(countyList.size()>0){
           dataList.clear(); //清空集合
            for(County county:countyList){
                dataList.add(county.getCountyName());//往集合中添加东西
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectProvince.getProvinceCode();
            int cityCode = selectcity.getCiryCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
          queryFromServer(address ,"county");
        }

    }

    /*
    * 省查询市*/
    private void queryCitys() {
        titletext.setText(selectProvince.getProvinceName());
        backbutton.setVisibility(View.VISIBLE);
        cityList = DataSupport.findAll(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for (City city :cityList ) {
                dataList.add(city.getCiryName());
            }
             adapter.notifyDataSetChanged();//刷新
            listView.setSelection(0);
            currentLevel =LEVEL_CTY;
        }else {
           int provinceCode = selectProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

    /*
    * 根据传入的地址和类型从服务器上查询省市县数据
    * */
    private void queryFromServer(String address, final String typle) {
        showProgressDialog();
        HttpUtil.sendOkhttpRequest(address, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
           String responsetext = response.body().string();
            boolean result = false;
                if("province".equals(typle)){
                    result = Utilty.handleProvinceResponse(responsetext);
                }else if ("city".equals(typle)){
                    result = Utilty.handleCtyResponse(responsetext,selectProvince.getId());
                }else if("county".equals(typle)){
                    result = Utilty.handleCountyResponse(responsetext,selectcity.getId());

                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();

                            if("province".equals(typle)){
                                queryProvince();
                            } else if("city".equals(typle)){
                                queryCitys();
                            }else if ("county".equals(typle)){
                                queryCountys();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
       getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run() {
               closeProgressDialog();
               Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                }
              });
            }
        });
    }

    /*显示对话框
    @  dialog.setCancelable(false);
    @  dialog弹出后会点击屏幕或物理返回键，dialog不消失
    @  dialog.setCanceledOnTouchOutside(false);
    @  dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
    * */
    private void showProgressDialog() {
      if(progressDialog ==null){
          progressDialog = new ProgressDialog(getContext());
          progressDialog.setMessage("正在加载");
          progressDialog.setCanceledOnTouchOutside(false);
      }
    }
    /*关闭对话框
    * */
    private void closeProgressDialog() {
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
