package com.example.myevents;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }

    //Arrays
    public int[] slide_images={
            R.drawable.help_logo,
            R.drawable.help_organize,
            R.drawable.help_oldevents,
            R.drawable.help_settings,
            R.drawable.help_maps,
            R.drawable.help_ratings,
            R.drawable.help_previouscomments,
            R.drawable.help_refresh,
            R.drawable.help_filters,
            R.drawable.help_contact


    };

    public String[]slide_headings={
            "Μy Events",
            "Πως μπορώ να δημιουργήσω μία εκδήλωση;",
            "Πως μπορώ να δω τις παλαιότερες εκδηλώσεις που έχω δημιουργήσει;",
            "Πως μπορώ να απενεργοποιήσω τις ειδοποιήσεις;",
            "Πως μπορώ να ανοίξω τους χάρτες;",
            "Πως μπορώ να βαθμολογήσω μια εκδήλωση;",
            "Πως μπορώ να δω τα σχόλια άλλων χρηστών;",
            "Ανανέωση εκδηλώσεων",
            "Φιλτράρισμα εκδηλώσεων",
            "Επικοινωνήστε μαζί μας!"
    };
    public String[]slide_decs={
            "Καλωσήρθατε στον Βοηθητικό Οδηγό της εφαρμογής μας!\n \n \n Για να συνεχίσετε σύρετε δεξιά",
            "Για να δημιουργήσετε μια εκδήλωση, κάνετε κλικ στην πάνω δεξιά γωνία της οθόνης και επιλέγετε \"Δημιουργία νέας εκδήλωσης\".",
            "Για να δείτε αλλά και να επεξεργαστείτε τις προηγούμενες εκδηλώσεις σας κάνετε κλικ στην πάνω δεξιά γωνία της οθόνης και επιλέγετε \"Παλαιότερες εκδηλώσεις\".",
            "Για να απενεργοποιήσετε τις ειδοποιήσεις και τον ήχο τους κάνετε κλικ στο εικονίδιο \"Ρυθμίσεις\" στην κάτω δεξιά γωνία της οθόνης.\n  Βέβαια δεν θα σας το συνιστούσαμε, καθώς θα χάσετε τις ενημερώσεις για κάθε νέα εκδήλωση κοντά σας!",
            "Στους χάρτες μπορείτε να δείτε την ακριβή τοποθεσία όλων των εκδηλώσεων στην περιοχή σας. Για να τους ανοίξετε, κάνετε κλικ στο εικονίδιο \"Χάρτες\", το δεύτερο από την κάτω αριστερή γωνία της οθόνης.",
            "Η γνώμη σας μετράει, για αυτό μπορείτε να βαθμολογήσετε αλλά και να σχολιάσετε την κάθε εκδήλωση. Αυτό γίνεται κάνοντας κλικ στην εκδήλωση και κατεβαίνοντας προς το  κάτω μέρος της οθόνης όπου μπορείτε να συμπληρώσετε τα αντίστοιχα πεδία.",
            "Για να δείτε τι έγραψαν οι άλλοι χρήστες για μία συγκεκριμένη εκδήλωση, κατεβείτε προς το κάτω μέρος της οθόνης της εκδήλωσης και κάντε κλικ στο εικονίδιο στην δεξιά γωνία.",
            "Οι νέες εκδηλώσεις προστίθενται αυτόματα κάθε φορά που ανοίγετε την εφαρμογή. Παρ'όλα αυτά, μπορείτε να πατήσετε το κουμπί Ανανέωση στην πάνω δεξιά γωνία και θα φορτώσουν όλες οι καινούργιες εκδηλώσεις που προστέθηκαν, χωρίς να βγείτε από την εφαρμογή.",
            "Μπορείτε να φιλτράρετε τις εκδηλώσεις που σας εμφανίζονται ανάλογα με την ημερομηνία (εικονίδιο ημερολόγιο-κόκκινο βελάκι), την κατηγορία της εκδήλωσης (γραμμή εργαλείων-μωβ βελάκι) και την περιοχή (εικονίδιο τοποθεσίας-ροζ βελάκι).",
            "Για να εκφράσετε τις περαιτέρω απορίες,τα παράπονά αλλά και για να μας πείτε τη γνώμη σας για την εφαρμογή, μη διστάσετε να επικοινωνήσετε μαζί μας κάνοντας κλικ στο εικονίδιο \"Επικοινωνία\", το δεύτερο εικονίδιο από το κάτω δεξιά μέρος της οθόνης. Θα σας ανοίξει το ηλεκτρονικό ταχυδρομείο, απ'όπου μπορείτε να μας αποστείλετε ένα email."
    };

    @Override
    public int getCount(){
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o){
        return view== (RelativeLayout) o;
    }

    @Override
    public  Object instantiateItem(ViewGroup container,int position){
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView=(ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading=(TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription=(TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_decs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView((RelativeLayout)object);
    }
}

