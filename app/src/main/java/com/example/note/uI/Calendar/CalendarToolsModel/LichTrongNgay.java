package com.example.note.uI.Calendar.CalendarToolsModel;

import androidx.annotation.NonNull;

public class LichTrongNgay {
    int thu;
    int ca;

    public LichTrongNgay(int thu, int ca) {
        this.thu = thu;
        this.ca = ca;
    }

    public int getThu() {
        return thu;
    }

    public void setThu(int thu) {
        this.thu = thu;
    }

    public int getCa() {
        return ca;
    }

    public void setCa(int ca) {
        this.ca = ca;
    }

    @NonNull
    @Override
    public String toString() {
        return "Thứ: " + this.thu + " ca: " + this.ca;
    }
}
