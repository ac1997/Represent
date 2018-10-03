package info.alexanderchen.represent.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ZipCodeWrapper implements Parcelable {

    private String zipCode;

    public ZipCodeWrapper(String zipCode) {
        this.zipCode = zipCode;
    }

    protected ZipCodeWrapper(Parcel in) {
        this.zipCode = in.readString();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.zipCode);
    }

    public static final Creator<ZipCodeWrapper> CREATOR = new Creator<ZipCodeWrapper>() {
        @Override
        public ZipCodeWrapper createFromParcel(Parcel in) {
            return new ZipCodeWrapper(in);
        }

        @Override
        public ZipCodeWrapper[] newArray(int size) {
            return new ZipCodeWrapper[size];
        }
    };
}
