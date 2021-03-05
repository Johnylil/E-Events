package com.example.myevents;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class Preferences {          //Κλάση για να μπορούν να αποθηκεύεται το αν ο χρήστης έχει κάνει login ή όχι για να μη χρειάζεται να κάνει login συνεχώς (βλέπε messenger)
    // Values for Shared Prefrences
    public static final String LOGGED_IN_PREF = "logged_in_status";  //Αν έχει κάνει login ->loggedIn=true
    static SharedPreferences getPreferences(Context context) {       //Αν δεν έχει κάνει login ->loggedIn=false
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();  //Σε περίπτωση που ο χρήστης εισάγει σώστα τα στοιχεία του
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);                      //Να γίνεται η μεταβλητή loggedIn set to true
        editor.apply();
    }
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);  //Λανθασμένα στοιχεία ->loggedIn=false
    }
}