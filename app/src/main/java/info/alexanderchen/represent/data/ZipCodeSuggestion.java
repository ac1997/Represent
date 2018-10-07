package info.alexanderchen.represent.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class ZipCodeSuggestion implements SearchSuggestion {

    private String mZipCode;
    private boolean mIsHistory;

    public ZipCodeSuggestion(String suggestion, boolean isHistory) {
        this.mZipCode = suggestion;
        this.mIsHistory = isHistory;
    }

    public ZipCodeSuggestion(Parcel source) {
        this.mZipCode = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public String getmZipCode() {
        return mZipCode;
    }

    public void setmZipCode(String mZipCode) {
        this.mZipCode = mZipCode;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return this.mZipCode;
    }

    public static final Creator<ZipCodeSuggestion> CREATOR = new Creator<ZipCodeSuggestion>() {
        @Override
        public ZipCodeSuggestion createFromParcel(Parcel in) {
            return new ZipCodeSuggestion(in);
        }

        @Override
        public ZipCodeSuggestion[] newArray(int size) {
            return new ZipCodeSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mZipCode);
        dest.writeInt(this.mIsHistory ? 1 : 0);
    }

    @Override
    public int hashCode() {
        return this.mZipCode.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ||
                obj instanceof ZipCodeSuggestion &&
                        this.mZipCode.hashCode() == ((ZipCodeSuggestion) obj).mZipCode.hashCode();

    }
}