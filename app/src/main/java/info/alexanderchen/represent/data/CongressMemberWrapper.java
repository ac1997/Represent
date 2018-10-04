package info.alexanderchen.represent.data;

import android.os.Parcel;
import android.os.Parcelable;

public class CongressMemberWrapper implements Parcelable {
    private String id;
    private String name;
    private String role;
    private String party;
    private String twitter_id;
    private String facebook_account;
    private String youtube_id;
    private String api_uri;

    public CongressMemberWrapper() {
        this.id = "";
        this.name = "";
        this.role = "";
        this.party = "";
        this.twitter_id = "";
        this.facebook_account = "";
        this.youtube_id = "";
        this.api_uri = "";
    }

    public CongressMemberWrapper(String id) {
        this.id = id;
        this.name = "";
        this.role = "";
        this.party = "";
        this.twitter_id = "";
        this.facebook_account = "";
        this.youtube_id = "";
        this.api_uri = "";
    }

    protected CongressMemberWrapper(Parcel in) {
        id = in.readString();
        name = in.readString();
        role = in.readString();
        party = in.readString();
        twitter_id = in.readString();
        facebook_account = in.readString();
        youtube_id = in.readString();
        api_uri = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.replace(",", "");
    }

    public String getParty() {
        if (this.party.equals("D"))
            return "Democratic";
        else if (this.party.equals("I"))
            return "Independent";
        else if (this.party.equals("R"))
            return "Republican";
        else
            return "null";
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String getFacebook_account() {
        return facebook_account;
    }

    public void setFacebook_account(String facebook_account) {
        this.facebook_account = facebook_account;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
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
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.role);
        parcel.writeString(this.party);
        parcel.writeString(this.twitter_id);
        parcel.writeString(this.facebook_account);
        parcel.writeString(this.youtube_id);
        parcel.writeString(this.api_uri);
    }

    public static final Creator<CongressMemberWrapper> CREATOR = new Creator<CongressMemberWrapper>() {
        @Override
        public CongressMemberWrapper createFromParcel(Parcel in) {
            return new CongressMemberWrapper(in);
        }

        @Override
        public CongressMemberWrapper[] newArray(int size) {
            return new CongressMemberWrapper[size];
        }
    };
}
