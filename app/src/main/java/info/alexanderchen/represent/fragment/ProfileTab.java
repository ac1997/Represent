package info.alexanderchen.represent.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import info.alexanderchen.represent.R;
import info.alexanderchen.represent.Typefaces;
import info.alexanderchen.represent.data.CongressMemberWrapper;

public class ProfileTab extends Fragment {

    CongressMemberWrapper congressMemberWrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        assert getArguments() != null;
        congressMemberWrapper = getArguments().getParcelable("congressMemberWrapper");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.linearLayoutProfileFragment).setBackgroundColor(Color.WHITE);

        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        TextView textViewChamber = view.findViewById(R.id.textViewChamber);
        TextView textViewOffice = view.findViewById(R.id.textViewOffice);
        TextView textViewPhone = view.findViewById(R.id.textViewPhone);
        TextView textViewStartDate = view.findViewById(R.id.textViewStartDate);
        TextView textViewEndDate = view.findViewById(R.id.textViewEndDate);

        textViewTitle.setText(congressMemberWrapper.getFullTitle());
        textViewChamber.setText(congressMemberWrapper.getFullChamber());
        textViewOffice.setText(congressMemberWrapper.getOffice());
        textViewPhone.setText(congressMemberWrapper.getPhone());
        textViewStartDate.setText(congressMemberWrapper.getStart_date());
        textViewEndDate.setText(congressMemberWrapper.getEnd_date());

        Button emailButton = view.findViewById(R.id.buttonEmailProfile);
        Button websiteButton = view.findViewById(R.id.buttonWebsiteProfile);
        Button twitterButton = view.findViewById(R.id.buttonTwitterProfile);
        Button facebookButton = view.findViewById(R.id.buttonFacebookProfile);
        Button youtubeButton = view.findViewById(R.id.buttonYoutubeProfile);

        Typeface font = Typefaces.get(view.getContext(), "fa-solid-900.ttf");
        emailButton.setTypeface(font);
        websiteButton.setTypeface(font);

        font = Typefaces.get(view.getContext(), "fa-brands-400.ttf");
        twitterButton.setTypeface(font);
        facebookButton.setTypeface(font);
        youtubeButton.setTypeface(font);

        setButtonListener(view, emailButton, congressMemberWrapper.getContact_form(), "contact form");
        setButtonListener(view, websiteButton, congressMemberWrapper.getWebsite(), "website URL");
        setButtonListener(view, twitterButton, congressMemberWrapper.getTwitter_id(), "Twitter profile");
        setButtonListener(view, facebookButton, congressMemberWrapper.getFacebook_id(), "Facebook profile");
        setButtonListener(view, youtubeButton, congressMemberWrapper.getYoutube_id(), "YouTube profile");

    }

    private void setButtonListener(View view, Button button, final String id, final String socialMedia) {
        if (id.equals("null"))
            button.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.round_button_invalid));

        String url = null;

        switch (socialMedia) {
            case "contact form":
            case "website URL":
                url = id;
                break;
            case "Twitter profile":
                url = "https://twitter.com/" + id;
                break;
            case "Facebook profile":
                url = "https://www.facebook.com/" + id;
                break;
            case "YouTube profile":
                url = "https://www.youtube.com/" + id;
                break;
        }

        final String actual_url = url;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals("null"))
                    Toast.makeText(v.getContext(), "Missing "+socialMedia, Toast.LENGTH_SHORT).show();
                else
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(actual_url)));
            }
        });

    }
}
