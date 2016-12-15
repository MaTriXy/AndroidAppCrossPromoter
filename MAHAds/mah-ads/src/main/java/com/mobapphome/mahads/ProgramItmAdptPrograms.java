package com.mobapphome.mahads;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobapphome.mahads.tools.MAHAdsController;
import com.mobapphome.mahads.tools.Utils;
import com.mobapphome.mahads.tools.gui.AngledLinearLayout;
import com.mobapphome.mahads.types.Program;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProgramItmAdptPrograms extends BaseAdapter implements
        View.OnClickListener {

    private final String TAG = ProgramItmAdptPrograms.class.getName();
    private List<Object> items;
    private static LayoutInflater inflater = null;


    public ProgramItmAdptPrograms(Context context, List<Object> items) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onClick(View arg0) {

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Object obj = items.get(position);
        if (obj instanceof Program) {
            final Program currProgram = (Program) obj;
            final String pckgName = currProgram.getUri().trim();

            // For hole view for click----------------------------------
            final View vi = inflater.inflate(R.layout.program_item_programs, null);
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (Utils.checkPackageIfExists(vi.getContext(), pckgName)) {
                        PackageManager pack = vi.getContext().getPackageManager();
                        Intent app = pack.getLaunchIntentForPackage(pckgName);
                        app.putExtra(MAHAdsController.MAH_ADS_INTERNAL_CALLED, true);
                        vi.getContext().startActivity(app);
                    } else {
                        if (!pckgName.isEmpty()) {
                            try {
                                Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                marketIntent.setData(Uri.parse("market://details?id=" + pckgName));
                                vi.getContext().startActivity(marketIntent);
                            } catch (Exception e) {
                                Log.d("test", e.getMessage());
                            }
                        }
                    }
                }
            });


            TextView nameTV = (TextView) vi
                    .findViewById(R.id.tvProgramNameMAHAds);
            TextView descTV = (TextView) vi
                    .findViewById(R.id.tvProgramDescMAHAds);
            ImageView ivImg = (ImageView) vi.findViewById(R.id.ivProgramImgMAHAds);
            TextView tvOpenGooglePLay = (TextView) vi.findViewById(R.id.tvOpenInstallMAHAds);

            if (Utils.checkPackageIfExists(vi.getContext(), pckgName)) {
                tvOpenGooglePLay.setText(vi.getContext().getResources().getString(R.string.mah_ads_open_program));
            } else {
                tvOpenGooglePLay.setText(vi.getContext().getResources().getString(R.string.mah_ads_install_program));
            }
            // Setting all values in listview
            nameTV.setText(currProgram.getName());
            descTV.setText(currProgram.getDesc());

            Log.i("Test",MAHAdsController.urlRootOnServer + currProgram.getImg() );
            Picasso.with(vi.getContext())
                    .load(MAHAdsController.urlRootOnServer + currProgram.getImg())
                    .placeholder(R.drawable.img_place_holder_normal)
                    .error(R.drawable.img_not_found)
                    .into(ivImg);

            AngledLinearLayout lytProgramNewText = (AngledLinearLayout) vi.findViewById(R.id.lytProgramNewTextMAHAds);
            if (currProgram.isNewPrgram()) {
                lytProgramNewText.setVisibility(View.VISIBLE);
            } else {
                lytProgramNewText.setVisibility(View.GONE);
            }


            ImageView ivMore = (ImageButton) vi.findViewById(R.id.btnOverflowMAHAds);
            if (MAHAdsController.isLightTheme()) {
                ivMore.setImageResource(R.drawable.ic_more_vert_grey600_24dp);
            } else {
                ivMore.setImageResource(R.drawable.ic_more_vert_grey600_24dp_white);
            }

            ivMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(vi.getContext(), v);
                    // Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.program_popup_menu, popup.getMenu());
                    // registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.popupMenuOpenOnGoogleP) {
                                if (!pckgName.isEmpty()) {
                                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                    marketIntent.setData(Uri.parse("market://details?id=" + pckgName));
                                    vi.getContext().startActivity(marketIntent);
                                }
                            }
                            return true;
                        }
                    });

                    popup.show();// showing popup menu
                }
            });

            MAHAdsController.setFontTextView((TextView) vi.findViewById(R.id.tvNewText));
            MAHAdsController.setFontTextView(nameTV);
            MAHAdsController.setFontTextView(descTV);
            MAHAdsController.setFontTextView(tvOpenGooglePLay);
            return vi;
        } else {
            return null;
        }

    }
}
