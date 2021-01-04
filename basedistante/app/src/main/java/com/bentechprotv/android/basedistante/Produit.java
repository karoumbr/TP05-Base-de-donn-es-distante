package com.bentechprotv.android.basedistante;

import org.json.JSONObject;

public class Produit {
    private String reference, designation;

    public Produit(JSONObject jObject) {
        this.reference = jObject.optString("reference");
        this.designation = jObject.optString("designation");
    }

    public String getReference() {
        return reference;
    }

    public String getDesignation() {
        return designation;
    }
}
