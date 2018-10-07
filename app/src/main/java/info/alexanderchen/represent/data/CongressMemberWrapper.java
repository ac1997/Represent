package info.alexanderchen.represent.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CongressMemberWrapper implements Parcelable {
    public static final Map<String, String> STATE_MAP;
    static {
        STATE_MAP = new HashMap<>();
        STATE_MAP.put("AL", "Alabama");
        STATE_MAP.put("AK", "Alaska");
        STATE_MAP.put("AB", "Alberta");
        STATE_MAP.put("AZ", "Arizona");
        STATE_MAP.put("AR", "Arkansas");
        STATE_MAP.put("BC", "British Columbia");
        STATE_MAP.put("CA", "California");
        STATE_MAP.put("CO", "Colorado");
        STATE_MAP.put("CT", "Connecticut");
        STATE_MAP.put("DE", "Delaware");
        STATE_MAP.put("DC", "District Of Columbia");
        STATE_MAP.put("FL", "Florida");
        STATE_MAP.put("GA", "Georgia");
        STATE_MAP.put("GU", "Guam");
        STATE_MAP.put("HI", "Hawaii");
        STATE_MAP.put("ID", "Idaho");
        STATE_MAP.put("IL", "Illinois");
        STATE_MAP.put("IN", "Indiana");
        STATE_MAP.put("IA", "Iowa");
        STATE_MAP.put("KS", "Kansas");
        STATE_MAP.put("KY", "Kentucky");
        STATE_MAP.put("LA", "Louisiana");
        STATE_MAP.put("ME", "Maine");
        STATE_MAP.put("MB", "Manitoba");
        STATE_MAP.put("MD", "Maryland");
        STATE_MAP.put("MA", "Massachusetts");
        STATE_MAP.put("MI", "Michigan");
        STATE_MAP.put("MN", "Minnesota");
        STATE_MAP.put("MS", "Mississippi");
        STATE_MAP.put("MO", "Missouri");
        STATE_MAP.put("MT", "Montana");
        STATE_MAP.put("NE", "Nebraska");
        STATE_MAP.put("NV", "Nevada");
        STATE_MAP.put("NB", "New Brunswick");
        STATE_MAP.put("NH", "New Hampshire");
        STATE_MAP.put("NJ", "New Jersey");
        STATE_MAP.put("NM", "New Mexico");
        STATE_MAP.put("NY", "New York");
        STATE_MAP.put("NF", "Newfoundland");
        STATE_MAP.put("NC", "North Carolina");
        STATE_MAP.put("ND", "North Dakota");
        STATE_MAP.put("NT", "Northwest Territories");
        STATE_MAP.put("NS", "Nova Scotia");
        STATE_MAP.put("NU", "Nunavut");
        STATE_MAP.put("OH", "Ohio");
        STATE_MAP.put("OK", "Oklahoma");
        STATE_MAP.put("ON", "Ontario");
        STATE_MAP.put("OR", "Oregon");
        STATE_MAP.put("PA", "Pennsylvania");
        STATE_MAP.put("PE", "Prince Edward Island");
        STATE_MAP.put("PR", "Puerto Rico");
        STATE_MAP.put("QC", "Quebec");
        STATE_MAP.put("RI", "Rhode Island");
        STATE_MAP.put("SK", "Saskatchewan");
        STATE_MAP.put("SC", "South Carolina");
        STATE_MAP.put("SD", "South Dakota");
        STATE_MAP.put("TN", "Tennessee");
        STATE_MAP.put("TX", "Texas");
        STATE_MAP.put("UT", "Utah");
        STATE_MAP.put("VT", "Vermont");
        STATE_MAP.put("VI", "Virgin Islands");
        STATE_MAP.put("VA", "Virginia");
        STATE_MAP.put("WA", "Washington");
        STATE_MAP.put("WV", "West Virginia");
        STATE_MAP.put("WI", "Wisconsin");
        STATE_MAP.put("WY", "Wyoming");
        STATE_MAP.put("YT", "Yukon Territory");
    }

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

    private List<CommitteeWrapper> committeeWrappers;
    private List<BillWrapper> billWrappers;

    public CongressMemberWrapper(String id, String name, String website, String twitter_id, String facebook_id, String youtube_id, String api_uri) {
        this.id = id;
        this.name = name;
        this.chamber = "";
        this.title = "";
        this.party = "";
        this.state = "";
        this.district = "";
        this.start_date = "";
        this.end_date = "";
        this.office = "";
        this.phone = "";
        this.contact_form = "";
        this.website = website;
        this.twitter_id = twitter_id;
        this.facebook_id = facebook_id;
        this.youtube_id = youtube_id;
        this.api_uri = api_uri;

        this.committeeWrappers = new ArrayList<>();
        this.billWrappers = new ArrayList<>();
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

        this.committeeWrappers = new ArrayList<>();
        this.billWrappers = new ArrayList<>();
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

        this.committeeWrappers = new ArrayList<>();
        in.readTypedList(committeeWrappers, CommitteeWrapper.CREATOR);
        this.billWrappers = new ArrayList<>();
        in.readTypedList(billWrappers, BillWrapper.CREATOR);
    }

    public void roleSectionUpdates(String chamber, String title, String party, String state, String district, String start_date, String end_date, String office, String phone, String contact_form) {
        this.chamber = chamber;
        this.title = title.replace(",", "");
        this.party = party;
        this.state = state;
        this.district = ((district.equals("At-Large")) ? "1" : district);
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
        if (this.office.equals("null"))
            return "No Office Address on File";
        else
            return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getPhone() {
        if (this.phone.equals("null"))
            return "No Phone Number on File";
        else
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

    public List<CommitteeWrapper> getCommitteeWrappers() {
        return committeeWrappers;
    }

    public void setCommitteeWrappers(List<CommitteeWrapper> committees) {
        this.committeeWrappers = committees;
    }

    public List<BillWrapper> getBillWrappers() {
        return billWrappers;
    }

    public void setBillWrappers(List<BillWrapper> billWrappers) {
        this.billWrappers = billWrappers;
    }

    public String getShortTitle() {
        if(this.title.contains(" "))
            return this.title.substring(0, this.title.indexOf(" "));
        else
            return this.title;
    }

    public String getImageURL() {
        return "http://bioguide.congress.gov/bioguide/photo/"+this.id.charAt(0)+"/"+id+".jpg";
    }

    public String getFullTitle() {
        switch (this.chamber) {
            case "Senate":
                return this.title+" of "+STATE_MAP.get(this.state);
            case "House":
                return this.title+" of "+STATE_MAP.get(this.state)+"'s "+this.getFullDistrict();
            default:
                return "null";
        }
    }

    public String getFullChamber() {
        switch (this.chamber) {
            case "Senate":
                return "United States Senate";
            case "House":
                return "United States House of Representatives";
            default:
                return "null";
        }
    }

    public String getFullState() {
        return STATE_MAP.get(this.state);
    }

    public String getFullDistrict() {
        if (this.district.equals(""))
            return "";
        else
            return ordinal(Integer.parseInt(this.district)) + " District";
    }

    public boolean isSenator() {
        return this.chamber.equals("Senate");
    }

    private static String ordinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
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

        parcel.writeTypedList(this.committeeWrappers);
        parcel.writeTypedList(this.billWrappers);
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
