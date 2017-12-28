package com.demo.test.db;

import org.litepal.crud.DataSupport;

/**
 * Created by lx on 2017/12/22.
 */

public class City extends DataSupport {
    private int id;
    private String ciryName;
    private int ciryCode;
    private int provinceId;

    public int getCiryCode() {
        return ciryCode;
    }

    public void setCiryCode(int ciryCode) {
        this.ciryCode = ciryCode;
    }

    public String getCiryName() {
        return ciryName;
    }

    public void setCiryName(String ciryName) {
        this.ciryName = ciryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
