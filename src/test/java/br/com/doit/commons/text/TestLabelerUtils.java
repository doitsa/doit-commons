package br.com.doit.commons.text;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
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
    public void returnFallbackMapIfFormatNotAccepted() {
        String address = "5 Giralda Farms, Dodge Dr, Madison, NJ 07940";
        String countryCode = "USA";

        NSDictionary<String, Object> result = (NSDictionary<String, Object>) LabelerUtils.formatAddress(address, countryCode);
        assertNotNull(result);
        assertThat(result.get("streetName"), is(address));
        assertThat(result.get("countryCode"), is("USA"));
    }

    // ===== Preservation Property Tests =====
    // These tests capture the baseline behavior of the UNFIXED code for non-buggy inputs.
    // They must PASS on unfixed code and continue to pass after the fix is applied.
    // Validates: Requirements 3.1, 3.2, 3.3, 3.4

    /**
     * **Validates: Requirements 3.1**
     * Preservation: Standard 4-part format addresses with state(abbr) and zipCode
     * in segment[2] must continue to parse correctly.
     */
    @Test
    public void preservationStandard4PartFormatParsesAllFields() {
        String address = "911 North Davis Avenue, Cleveland, Mississippi (MS) 38732, United States (USA)";
        String countryCode = "USA";

        NSDictionary<String, Object> result = (NSDictionary<String, Object>) LabelerUtils.formatAddress(address, countryCode);

        assertNotNull(result);
        assertThat(result.get("streetName"), is("North Davis Avenue"));
        assertThat(result.get("streetNum"), is("911"));
        assertThat(result.get("state"), is("MS"));
        assertThat(result.get("city"), is("Cleveland"));
        assertThat(result.get("country"), is("United States"));
        assertThat(result.get("zipCode"), is("38732"));
        assertThat(result.get("countryCode"), is("USA"));
    }

    /**
     * **Validates: Requirements 3.1**
     * Preservation: Another standard 4-part format with different state.
     */
    @Test
    public void preservationStandard4PartFormatWithDifferentState() {
        String address = "200 Main Street, Springfield, Illinois (IL) 62704, United States (USA)";
        String countryCode = "USA";

        NSDictionary<String, Object> result = (NSDictionary<String, Object>) LabelerUtils.formatAddress(address, countryCode);

        assertNotNull(result);
        assertThat(result.get("streetName"), is("Main Street"));
        assertThat(result.get("streetNum"), is("200"));
        assertThat(result.get("state"), is("IL"));
        assertThat(result.get("city"), is("Springfield"));
        assertThat(result.get("country"), is("United States"));
        assertThat(result.get("zipCode"), is("62704"));
    }

    /**
     * **Validates: Requirements 3.2**
     * Preservation: 3-part format addresses extract only street fields.
     */
    @Test
    public void preservationThreePartFormatExtractsStreetFieldsOnly() {
        String address = "75 Adams Ave, Hauppauge, NY 11788";
        String countryCode = "USA";

        NSDictionary<String, Object> result = (NSDictionary<String, Object>) LabelerUtils.formatAddress(address, countryCode);

        assertNotNull(result);
        assertThat(result.get("streetName"), is("Adams Ave"));
        assertThat(result.get("streetNum"), is("75"));
        assertThat(result.get("countryCode"), is("USA"));
    }

    /**
     * **Validates: Requirements 3.4**
     * Preservation: Null input returns empty string.
     */
    @Test
    public void preservationNullInputReturnsEmptyString() {
        Object result = LabelerUtils.formatAddress(null, "USA");

        assertThat(result, is(""));
    }

    /**
     * **Validates: Requirements 3.3**
     * Preservation: Denmark-formatted addresses use the Denmark-specific path.
     */
    @Test
    public void preservationDenmarkFormatUsesDenmarkPath() {
        String address = "Lyngbyvej 20, Copenhagen, Capital Region 2100, Denmark (DNK)";
        String countryCode = "DNK";

        NSDictionary<String, Object> result = (NSDictionary<String, Object>) LabelerUtils.formatAddress(address, countryCode);

        assertNotNull(result);
        assertThat(result.get("streetName"), is("Lyngbyvej"));
        assertThat(result.get("streetNum"), is("20"));
        assertThat(result.get("city"), is("Copenhagen"));
        assertThat(result.get("zipCode"), is("2100"));
        assertThat(result.get("state"), is("Capital Region"));
        assertThat(result.get("country"), is("Denmark (DNK)"));
    }

    /**
     * **Validates: Requirements 3.1**
     * Preservation: 4-part format without state abbreviation in parentheses returns
     * a fallback map with streetName set to the full address.
     */
    @Test
    public void preservationUnparseable4PartReturnsFallbackMap() {
        String address = "5 Giralda Farms, Dodge Dr, Madison, NJ 07940";
        String countryCode = "USA";

        NSDictionary<String, Object> result = (NSDictionary<String, Object>) LabelerUtils.formatAddress(address, countryCode);

        assertNotNull(result);
        assertThat(result.get("streetName"), is(address));
        assertThat(result.get("countryCode"), is("USA"));
    }

    /**
     * validateAddress still returns true for unparseable addresses
     * (addresses that only produce a fallback streetName map).
     */
    @Test
    public void validateAddressReturnsTrueForUnparseableAddress() {
        String address = "5 Giralda Farms, Dodge Dr, Madison, NJ 07940";
        assertTrue(LabelerUtils.validateAddress(address, "USA"));
    }

    /**
     * validateAddress returns false for fully parseable addresses.
     */
    @Test
    public void validateAddressReturnsFalseForParseableAddress() {
        String address = "911 North Davis Avenue, Cleveland, Mississippi (MS) 38732, United States (USA)";
        assertTrue(!LabelerUtils.validateAddress(address, "USA"));
    }
}
