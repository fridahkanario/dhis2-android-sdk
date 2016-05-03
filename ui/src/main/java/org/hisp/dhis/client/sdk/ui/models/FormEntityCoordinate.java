package org.hisp.dhis.client.sdk.ui.models;

import android.support.annotation.NonNull;

public class FormEntityCoordinate extends FormEntity {
    private double latitude;
    private double longitude;

    public FormEntityCoordinate(String id, String label) {
        super(id, label);
    }

    @NonNull
    @Override
    public Type getType() {
        return Type.COORDINATES;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
