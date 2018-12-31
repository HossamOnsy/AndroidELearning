package com.salwa.androidelearning;

import android.content.Context;
import android.content.SharedPreferences;

public class pref {
    Context c;
    public static final String name = "name";

    public pref(Context c) {
        this.c = c;
    }

    public static SharedPreferences getsharedPref(Context c) {
        SharedPreferences pref = c.getSharedPreferences("prefID", Context.MODE_PRIVATE);

        return pref;
    }

}
