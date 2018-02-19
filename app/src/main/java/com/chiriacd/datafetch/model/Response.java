package com.chiriacd.datafetch.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Response implements Parcelable {

    private final String code;
    private int timesFetched;

    public Response(String responseCode, int timesFetched) {
        code = responseCode;
        this.timesFetched = timesFetched;
    }

    public Response(String responseCode) {
        this(responseCode, 0);
    }

    public String getCode() {
        return code;
    }

    public int getTimesFetched() {
        return timesFetched;
    }

    protected Response(Parcel in) {
        code = in.readString();
        timesFetched = in.readInt();
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    public void increment() {
        timesFetched++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeInt(timesFetched);
    }
}
