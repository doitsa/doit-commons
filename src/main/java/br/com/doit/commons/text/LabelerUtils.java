package br.com.doit.commons.text;

import java.util.logging.Logger;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

import org.apache.commons.lang.StringUtils;

public class LabelerUtils {

    private static Logger LOGGER = Logger.getLogger(LabelerUtils.class.getName());

    private static final String STATE_KEY = "state";
    private static final String STREET_NUM_KEY = "streetNum";
    private static final String STREET_NAME_KEY = "streetName";
    private static final String CITY_KEY = "city";
    private static final String COUNTRY_KEY = "country";
    private static final String ZIP_CODE_KEY = "zipCode";
    private static final String MORE_KEY = "more";

    public static Object formatAddress(String address, String countryCode) {
        try {
            if (address == null) {
                return "";
            }

            String country = "";

            NSDictionary<String, Object> data = new NSMutableDictionary<>();

            String[] addressSplited = address.split(", ");

            char firstCharacter = address.charAt(0);

            if (Character.isDigit(firstCharacter)) {
                data.put(STREET_NAME_KEY, addressSplited[0].split(" ", 2)[1]);
                data.put(STREET_NUM_KEY, addressSplited[0].split(" ", 2)[0]);

            } else {
                data.put(STREET_NAME_KEY, addressSplited[0]);
            }

            if (addressSplited.length == 4) {
                country = addressSplited[3].substring(0, addressSplited[3].indexOf(" ("));

                data.put(STATE_KEY, addressSplited[2].substring(addressSplited[2].indexOf("(") + 1, addressSplited[2].indexOf(")")));
                data.put(CITY_KEY, addressSplited[1]);
                data.put(COUNTRY_KEY, country);
                data.put(ZIP_CODE_KEY, addressSplited[2].substring(addressSplited[2].indexOf(") ") + 2, addressSplited[2].length()));
            }

            if (addressSplited.length == 5) {
                country = addressSplited[4].substring(0, addressSplited[4].indexOf(" ("));

                data.put(STATE_KEY, addressSplited[3].substring(addressSplited[3].indexOf("(") + 1, addressSplited[3].indexOf(")")));
                data.put(CITY_KEY, addressSplited[2]);
                data.put(COUNTRY_KEY, country);
                data.put(MORE_KEY, addressSplited[1]);
                data.put(ZIP_CODE_KEY, addressSplited[3].substring(addressSplited[3].indexOf(") ") + 2, addressSplited[3].length()));
            }

            if (StringUtils.isNotEmpty(countryCode)) {
                data.put("countryCode", countryCode);
            }

            return data;
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.ALL, "ERROR formatting address " + address + ", with country code " + countryCode);
            return null;
        }
    }

    public static boolean validateAddress(String address, String countryCode) {
        return formatAddress(address, countryCode) == null;
    }
}
