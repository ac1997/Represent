package info.alexanderchen.represent.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CongressMemberWrapper implements Parcelable {
    private String id;
    private String name;
    private String chamber;
    private String title;
    private String party;
    private String state;
    private String district;
    private String start_date;
    private String end_date;
    private String office;
    private String phone;
    private String contact_form;
    private String website;
    private String twitter_id;
    private String facebook_id;
    private String youtube_id;
    private String api_uri;

    private List<CommitteeWrapper> committees;
    private List<SubCommitteeWrapper> subCommittees;

    public CongressMemberWrapper(String id, String name, String website, String twitter_id, String facebook_id, String youtube_id, String api_uri) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.twitter_id = twitter_id;
        this.facebook_id = facebook_id;
        this.youtube_id = youtube_id;
        this.api_uri = api_uri;

        this.committees = new ArrayList<>();
        this.subCommittees = new ArrayList<>();
    }

    public CongressMemberWrapper(String id, String name, String chamber, String title, String party, String state, String district, String start_date, String end_date, String office, String phone, String contact_form, String website, String twitter_id, String facebook_id, String youtube_id) {
        this.id = id;
        this.name = name;
        this.chamber = chamber;
        this.title = title.replace(",", "");;
        this.party = party;
        this.state = state;
        this.district = ((district.equals("At-Large")) ? "1" : district);
        this.start_date = start_date;
        this.end_date = end_date;
        this.office = office;
        this.phone = phone;
        this.contact_form = contact_form;
        this.website = website;
        this.twitter_id = twitter_id;
        this.facebook_id = facebook_id;
        this.youtube_id = youtube_id;

        this.committees = new ArrayList<>();
        this.subCommittees = new ArrayList<>();
    }

    protected CongressMemberWrapper(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.chamber = in.readString();
        this.title = in.readString();
        this.party = in.readString();
        this.state = in.readString();
        this.district = in.readString();
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.office = in.readString();
        this.phone = in.readString();
        this.contact_form = in.readString();
        this.website = in.readString();;
        this.twitter_id = in.readString();
        this.facebook_id = in.readString();
        this.youtube_id = in.readString();
        this.api_uri = in.readString();

        this.committees = new ArrayList<>();
        this.subCommittees = new ArrayList<>();
        in.readTypedList(committees, CommitteeWrapper.CREATOR);
        in.readTypedList(subCommittees, SubCommitteeWrapper.CREATOR);
    }

    public void roleSectionUpdates(String chamber, String title, String party, String state, String district, String start_date, String end_date, String office, String phone, String contact_form) {
        this.chamber = chamber;
        this.title = title.replace(",", "");
        this.party = party;
        this.state = state;
        this.district = district;
        this.start_date = start_date;
        this.end_date = end_date;
        this.office = office;
        this.phone = phone;
        this.contact_form = contact_form;
    }

    public void roleSectionUpdates(String chamber, String title, String party, String state, String start_date, String end_date, String office, String phone, String contact_form) {
        this.chamber = chamber;
        this.title = title.replace(",", "");
        this.party = party;
        this.state = state;
        this.district = "";
        this.start_date = start_date;
        this.end_date = end_date;
        this.office = office;
        this.phone = phone;
        this.contact_form = contact_form;
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

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.replace(",", "");
    }

    public String getParty() {
        switch (this.party) {
            case "D":
                return "Democratic";
            case "I":
                return "Independent";
            case "R":
                return "Republican";
            default:
                return null;
        }
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = ((district.equals("At-Large")) ? "1" : district);;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact_form() {
        return contact_form;
    }

    public void setContact_form(String contact_form) {
        this.contact_form = contact_form;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
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

    public List<CommitteeWrapper> getCommittees() {
        return committees;
    }

    public void setCommittees(List<CommitteeWrapper> committees) {
        this.committees = committees;
    }

    public List<SubCommitteeWrapper> getSubCommittees() {
        return subCommittees;
    }

    public void setSubCommittees(List<SubCommitteeWrapper> subCommittees) {
        this.subCommittees = subCommittees;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.chamber);
        parcel.writeString(this.title);
        parcel.writeString(this.party);
        parcel.writeString(this.state);
        parcel.writeString(this.district);
        parcel.writeString(this.start_date);
        parcel.writeString(this.end_date);
        parcel.writeString(this.office);
        parcel.writeString(this.phone);
        parcel.writeString(this.contact_form);
        parcel.writeString(this.website);
        parcel.writeString(this.twitter_id);
        parcel.writeString(this.facebook_id);
        parcel.writeString(this.youtube_id);
        parcel.writeString(this.api_uri);

        parcel.writeTypedList(this.committees);
        parcel.writeTypedList(this.subCommittees);
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
