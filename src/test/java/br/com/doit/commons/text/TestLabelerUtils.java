package br.com.doit.commons.text;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.webobjects.foundation.NSDictionary;

import org.junit.Test;

public class TestLabelerUtils {
    @Test
    public void formatCorrectFormattedAddress() throws Exception {
        String address = "911 North Davis Avenue, Cleveland, Mississippi (MS) 38732, United States (USA)";
        String countryCode = "USA";

        NSDictionary<String, Object> result = (NSDictionary<String, Object>) LabelerUtils.formatAddress(address, countryCode);
        assertThat(result.get("streetName"), is("North Davis Avenue"));
        assertThat(result.get("streetNum"), is("911"));
        assertThat(result.get("state"), is("MS"));
        assertThat(result.get("city"), is("Cleveland"));
        assertThat(result.get("country"), is("United States"));
        assertThat(result.get("zipCode"), is("38732"));
    }

    @Test
    public void formatOtherCorrectFormattedAddress() throws Exception {
        String address = "75 Adams Ave, Hauppauge, NY 11788";
        String countryCode = "USA";

        NSDictionary<String, Object> result = (NSDictionary<String, Object>) LabelerUtils.formatAddress(address, countryCode);
        assertThat(result.get("streetName"), is("Adams Ave"));
        assertThat(result.get("streetNum"), is("75"));
    }

    @Test
    public void returnNullIfFormatNotAccepted() {
        String address = "5 Giralda Farms, Dodge Dr, Madison, NJ 07940";
        String countryCode = "USA";

        NSDictionary<String, Object> result = (NSDictionary<String, Object>) LabelerUtils.formatAddress(address, countryCode);
        assertTrue(result == null);
    }
}
