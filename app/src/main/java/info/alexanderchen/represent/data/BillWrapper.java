package info.alexanderchen.represent.data;

import android.os.Parcel;
import android.os.Parcelable;

public class BillWrapper implements Parcelable {
    private boolean isIntroduced;
    private String billNumber;
    private String shortTitle;
    private String date;
    private String committee;
    private String govtrackURL;

    public BillWrapper(boolean isIntroduced, String billNumber, String shortTitle, String date, String committee, String govtrackURL) {
        this.isIntroduced = isIntroduced;
        this.billNumber = billNumber;
        this.shortTitle = shortTitle;
        this.date = date;
        this.committee = committee;
        this.govtrackURL = govtrackURL;
    }

    protected BillWrapper(Parcel in) {
        isIntroduced = in.readByte() != 0;
        billNumber = in.readString();
        shortTitle = in.readString();
        date = in.readString();
        committee = in.readString();
        govtrackURL = in.readString();
    }

    public boolean isIntroduced() {
        return isIntroduced;
    }

    public void setIntroduced(boolean introduced) {
        isIntroduced = introduced;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommittee() {
        return committee;
    }

    public void setCommittee(String committee) {
        this.committee = committee;
    }

    public String getGovtrackURL() {
        return govtrackURL;
    }

    public void setGovtrackURL(String govtrackURL) {
        this.govtrackURL = govtrackURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isIntroduced ? 1 : 0));
        parcel.writeString(billNumber);
        parcel.writeString(shortTitle);
        parcel.writeString(date);
        parcel.writeString(committee);
        parcel.writeString(govtrackURL);
    }

    public static final Creator<BillWrapper> CREATOR = new Creator<BillWrapper>() {
        @Override
        public BillWrapper createFromParcel(Parcel in) {
            return new BillWrapper(in);
        }

        @Override
        public BillWrapper[] newArray(int size) {
            return new BillWrapper[size];
        }
    };
}
