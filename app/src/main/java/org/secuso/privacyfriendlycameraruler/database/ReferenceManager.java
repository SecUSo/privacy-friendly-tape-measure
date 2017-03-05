/**
 * Privacy Friendly Camera Ruler is licensed under the GPLv3. Copyright (C) 2016 Roberts Kolosovs

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 The icons used in the nagivation drawer are licensed under the CC BY 2.5.
 In addition to them the app uses icons from Google Design Material Icons licensed under Apache
 License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo and the
 header in the navigation drawer) copyright Technische Universtität Darmstadt (2016).
 */

package org.secuso.privacyfriendlycameraruler.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.secuso.privacyfriendlycameraruler.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Class for providing the list of predefined references.
 * Add more references here. Be sure to group your references thematically via a fitting ObjectType.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 14.02.17.
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
        predefinedReferenceObjects.add(new ReferenceObject(R.string.us_penny_coin, usCoin, 19.05f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.us_nickel_coin, usCoin, 21.21f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.us_dime_coin, usCoin, 17.91f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.us_quarter_coin, usCoin, 24.26f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.us_dollar_coin, usCoin, 26.5f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.fifty_usd_cent_coin, usCoin, 30.61f));

        //Euro Coins:
        predefinedReferenceObjects.add(new ReferenceObject(R.string.one_euro_cent_coin, euCoin, 16.25f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.two_euro_cent_coin, euCoin, 18.75f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.five_euro_cent_coin, euCoin, 21.25f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.ten_euro_cent_coin, euCoin, 19.75f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.twenty_euro_cent_coin, euCoin, 22.25f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.fifty_euro_cent_coin, euCoin, 24.25f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.one_euro_coin, euCoin, 23.25f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.two_euro_coin, euCoin, 25.27f));

        //British Pound Sterling Coins:
        predefinedReferenceObjects.add(new ReferenceObject(R.string.gb_penny_coin, gbCoin, 20.3f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.two_pence_coin, gbCoin, 25.9f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.five_pence_coin, gbCoin, 18f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.ten_pence_coin, gbCoin, 24.5f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.twenty_pence_coin, gbCoin, 21.4f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.fifty_pence_coin, gbCoin, 27.3f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.one_pound_coin, gbCoin, 22.5f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.two_pound_coin, gbCoin, 28.4f));

        //US-dollar Notes:
        predefinedReferenceObjects.add(new ReferenceObject(R.string.us_dollar_note, usNotes, 156f*66.3f));

        //Euro Notes:
        predefinedReferenceObjects.add(new ReferenceObject(R.string.five_euro_note, euNotes, 120f*62f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.ten_euro_note, euNotes, 127f*67f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.twenty_euro_note, euNotes, 133f*72f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.fifty_euro_note, euNotes, 140f*77f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.hundred_euro_note, euNotes, 147f*82f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.two_hundred_euro_note, euNotes, 153f*82f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.five_hundred_euro_note, euNotes, 160f*82f));

        //British Pound Sterling Notes:
        predefinedReferenceObjects.add(new ReferenceObject(R.string.five_pound_note, gbNotes, 125f*65f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.ten_pound_note, gbNotes, 142f*75f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.twenty_pound_note, gbNotes, 149f*80f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.fifty_pound_note, gbNotes, 156f*85f));

        //US-Paper:
        predefinedReferenceObjects.add(new ReferenceObject(R.string.letter_paper, usPaper, 216f*279f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.legal_paper, usPaper, 216f*356f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.tabloid_paper, usPaper, 279f*432f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.ledger_paper, usPaper, 432f*279f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.junior_legal_paper, usPaper, 127f*203f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.memo_paper, usPaper, 140f*216f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.government_letter_paper, usPaper, 203f*267f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.government_legal_paper, usPaper, 216f*330f));

        //ISO 216 Paper (A-Series):
        predefinedReferenceObjects.add(new ReferenceObject(R.string.a0_paper, iso216Paper, 841f*1189f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.a1_paper, iso216Paper, 594f*841f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.a2_paper, iso216Paper, 420f*594f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.a3_paper, iso216Paper, 297f*420f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.a4_paper, iso216Paper, 210f*297f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.a5_paper, iso216Paper, 148f*210f));
        predefinedReferenceObjects.add(new ReferenceObject(R.string.a6_paper, iso216Paper, 105f*148f));
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
            if (active.contains(ro.type.name)) {
                result.add(ro);
            }
        }

        return result;
    }

}
