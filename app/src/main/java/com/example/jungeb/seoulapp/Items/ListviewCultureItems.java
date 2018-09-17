package com.example.jungeb.seoulapp.Items;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jungeb.seoulapp.R;

public class ListviewCultureItems {

    private String CultureTitle;
    private String CultureAddress;
    private String CultureKinds;

    public String getCultureTitle() {
        return CultureTitle;
    }

    public void setCultureTitle(String cultureTitle) {
        CultureTitle = cultureTitle;
    }

    public String getCultureAddress() {
        return CultureAddress;
    }

    public void setCultureAddress(String cultureAddress) {
        CultureAddress = cultureAddress;
    }

    public String getCultureKinds() {
        return CultureKinds;
    }

    public void setCultureKinds(String cultureKinds) {
        CultureKinds = cultureKinds;
    }
}
