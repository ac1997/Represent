package info.alexanderchen.represent.data;

import android.os.Parcel;
import android.os.Parcelable;

public class CommitteeWrapper implements Parcelable {
    private String name;
    private String code;
    private String side;
    private String title;
    private String endDate;
    private String api_uri;

    public CommitteeWrapper(String name, String code, String side, String title, String endDate, String api_uri) {
        this.name = name;
        this.code = code;
        this.side = side;
        this.title = title;
        this.endDate = endDate;
        this.api_uri = api_uri;
    }

    protected CommitteeWrapper(Parcel in) {
        name = in.readString();
        code = in.readString();
        side = in.readString();
        title = in.readString();
        endDate = in.readString();
        api_uri = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getApi_uri() {
        return api_uri;
    }

    public void setApi_uri(String api_uri) {
        this.api_uri = api_uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(code);
        parcel.writeString(side);
        parcel.writeString(title);
        parcel.writeString(endDate);
        parcel.writeString(api_uri);
    }

    public static final Creator<CommitteeWrapper> CREATOR = new Creator<CommitteeWrapper>() {
        @Override
        public CommitteeWrapper createFromParcel(Parcel in) {
            return new CommitteeWrapper(in);
        }

        @Override
        public CommitteeWrapper[] newArray(int size) {
            return new CommitteeWrapper[size];
        }
    };
}
