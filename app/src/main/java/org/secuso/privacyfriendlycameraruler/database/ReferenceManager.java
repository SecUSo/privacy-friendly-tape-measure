package org.secuso.privacyfriendlycameraruler.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Class for providing the list of predefined references.
 * Add more references here.
 *
 * Created by roberts on 14.02.17.
 */

public class ReferenceManager {
    private static ArrayList<ObjectType> objectTypes = new ArrayList<ObjectType>();

    private static ArrayList<ReferenceObject> predefinedReferenceObjects = new ArrayList<ReferenceObject>();

    static {
        //make object types. add new categories here.
        ObjectType usCoin = new ObjectType("us-coins", "circle");
        ObjectType euCoin = new ObjectType("eu-coins", "circle");
        ObjectType gbCoin = new ObjectType("gb-coins", "circle");
        ObjectType usNotes = new ObjectType("us-notes", "tetragon");
        ObjectType euNotes = new ObjectType("eu-notes", "tetragon");
        ObjectType gbNotes = new ObjectType("gb-notes", "tetragon");
        ObjectType usPaper = new ObjectType("us-paper", "tetragon");
        ObjectType iso216Paper = new ObjectType("iso216-paper", "tetragon");

        //fill the list of object types. be sure to also add new categories to the list.
        objectTypes.add(usCoin);
        objectTypes.add(euCoin);
        objectTypes.add(gbCoin);
        objectTypes.add(usNotes);
        objectTypes.add(euNotes);
        objectTypes.add(gbNotes);
        objectTypes.add(usPaper);
        objectTypes.add(iso216Paper);

        //make reference objects and put them in the list. add new reference objects here.
        //US-dollar Coins:
        predefinedReferenceObjects.add(new ReferenceObject("penny", usCoin, 19.05f));
        predefinedReferenceObjects.add(new ReferenceObject("nickel", usCoin, 21.21f));
        predefinedReferenceObjects.add(new ReferenceObject("dime", usCoin, 17.91f));
        predefinedReferenceObjects.add(new ReferenceObject("quarter", usCoin, 24.26f));
        predefinedReferenceObjects.add(new ReferenceObject("dollar", usCoin, 26.5f));
        predefinedReferenceObjects.add(new ReferenceObject("50¢", usCoin, 30.61f));

        //Euro Coins:
        predefinedReferenceObjects.add(new ReferenceObject("0.01€", euCoin, 16.25f));
        predefinedReferenceObjects.add(new ReferenceObject("0.02€", euCoin, 18.75f));
        predefinedReferenceObjects.add(new ReferenceObject("0.05€", euCoin, 21.25f));
        predefinedReferenceObjects.add(new ReferenceObject("0.10€", euCoin, 19.75f));
        predefinedReferenceObjects.add(new ReferenceObject("0.20€", euCoin, 22.25f));
        predefinedReferenceObjects.add(new ReferenceObject("0.50€", euCoin, 24.25f));
        predefinedReferenceObjects.add(new ReferenceObject("1€", euCoin, 23.25f));
        predefinedReferenceObjects.add(new ReferenceObject("2€", euCoin, 25.27f));

        //British Pound Sterling Coins:
        predefinedReferenceObjects.add(new ReferenceObject("1p", gbCoin, 20.3f));
        predefinedReferenceObjects.add(new ReferenceObject("2p", gbCoin, 25.9f));
        predefinedReferenceObjects.add(new ReferenceObject("5p", gbCoin, 18f));
        predefinedReferenceObjects.add(new ReferenceObject("10p", gbCoin, 24.5f));
        predefinedReferenceObjects.add(new ReferenceObject("20p", gbCoin, 21.4f));
        predefinedReferenceObjects.add(new ReferenceObject("50p", gbCoin, 27.3f));
        predefinedReferenceObjects.add(new ReferenceObject("1£", gbCoin, 22.5f));
        predefinedReferenceObjects.add(new ReferenceObject("2£", gbCoin, 28.4f));

        //US-dollar Notes:
        predefinedReferenceObjects.add(new ReferenceObject("USD Banknote", usNotes, 156f*66.3f));

        //Euro Notes:
        predefinedReferenceObjects.add(new ReferenceObject("5€", euNotes, 120f*62f));
        predefinedReferenceObjects.add(new ReferenceObject("10€", euNotes, 127f*67f));
        predefinedReferenceObjects.add(new ReferenceObject("20€", euNotes, 133f*72f));
        predefinedReferenceObjects.add(new ReferenceObject("50€", euNotes, 140f*77f));
        predefinedReferenceObjects.add(new ReferenceObject("100€", euNotes, 147f*82f));
        predefinedReferenceObjects.add(new ReferenceObject("200€", euNotes, 153f*82f));
        predefinedReferenceObjects.add(new ReferenceObject("500€", euNotes, 160f*82f));

        //British Pound Sterling Notes:
        predefinedReferenceObjects.add(new ReferenceObject("5£", gbNotes, 125f*65f));
        predefinedReferenceObjects.add(new ReferenceObject("10£", gbNotes, 142f*75f));
        predefinedReferenceObjects.add(new ReferenceObject("20£", gbNotes, 149f*80f));
        predefinedReferenceObjects.add(new ReferenceObject("50£", gbNotes, 156f*85f));

        //US-Paper:
        predefinedReferenceObjects.add(new ReferenceObject("letter", usPaper, 216f*279f));
        predefinedReferenceObjects.add(new ReferenceObject("legal", usPaper, 216f*356f));
        predefinedReferenceObjects.add(new ReferenceObject("tabloid", usPaper, 279f*432f));
        predefinedReferenceObjects.add(new ReferenceObject("ledger", usPaper, 432f*279f));
        predefinedReferenceObjects.add(new ReferenceObject("junior legal", usPaper, 127f*203f));
        predefinedReferenceObjects.add(new ReferenceObject("memo", usPaper, 140f*216f));
        predefinedReferenceObjects.add(new ReferenceObject("government letter", usPaper, 203f*267f));
        predefinedReferenceObjects.add(new ReferenceObject("government legal", usPaper, 216f*330f));

        //ISO 216 Paper (A-Series):
        predefinedReferenceObjects.add(new ReferenceObject("A0", iso216Paper, 841f*1189f));
        predefinedReferenceObjects.add(new ReferenceObject("A1", iso216Paper, 594f*841f));
        predefinedReferenceObjects.add(new ReferenceObject("A2", iso216Paper, 420f*594f));
        predefinedReferenceObjects.add(new ReferenceObject("A3", iso216Paper, 297f*420f));
        predefinedReferenceObjects.add(new ReferenceObject("A4", iso216Paper, 210f*297f));
        predefinedReferenceObjects.add(new ReferenceObject("A5", iso216Paper, 148f*210f));
        predefinedReferenceObjects.add(new ReferenceObject("A6", iso216Paper, 105f*148f));
    }

    /**
     * Provider for list of object types.
     *
     * @return List of all available ObjectTypes.
     */
    public static ArrayList<ObjectType> getObjectTypes() {
        return objectTypes;
    }

    /**
     * Provider for list of reference objects.
     *
     * @return List of all available ReferenceObjects.
     */
    public static ArrayList<ReferenceObject> getPredefinedReferenceObjects() {
        return predefinedReferenceObjects;
    }

    /**
     * Provider for list of reference objects not disabled in the settings.
     *
     * @return List of all ReferenceObjects not disabled in the settings.
     */
    public static ArrayList<ReferenceObject> getAllActiveRefPredefObjects(Context context) {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> active = sPrefs.getStringSet("pref_type_selection", new HashSet<String>());
        ArrayList<ReferenceObject> result = new ArrayList<ReferenceObject>();

        for (Iterator<ReferenceObject> it = predefinedReferenceObjects.iterator(); it.hasNext();) {
            ReferenceObject ro = it.next();
//            if (ro.type.name.equals("us-coins") || ro.type.name.equals("us-notes")){
            if (active.contains(ro.type.name)) {
                result.add(ro);
            }
        }

        return result;
    }

}
