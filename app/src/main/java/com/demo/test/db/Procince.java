package com.demo.test.db;

import org.litepal.crud.DataSupport;

/**
 * Created by lx on 2017/12/22.
 */

public class Province extends DataSupport {

    private int id;
    private String ProvinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }


}
