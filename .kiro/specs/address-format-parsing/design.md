# Address Format Parsing Bugfix Design

## Overview

The `LabelerUtils.formatAddress` method fails when a 4-part address has the city and state combined in the second segment with the zip code alone in the third segment. The current code unconditionally looks for parentheses in `addressSplited[2]` to extract the state abbreviation and zip code, but in the alternative format `addressSplited[2]` is a plain zip code (e.g., `"07065"`), causing a `StringIndexOutOfBoundsException` that is caught and results in `null`. The fix adds format detection by checking whether `addressSplited[2]` contains `"("` — if it does not, the code uses the alternative parsing path that extracts state from `addressSplited[1]`.

## Glossary

- **Bug_Condition (C)**: The condition that triggers the bug — a 4-part comma-split address where `addressSplited[2]` does not contain `"("`, meaning the state is embedded in `addressSplited[1]` alongside the city
- **Property (P)**: The desired behavior — correctly parse city, state abbreviation, zip code, and country from the alternative 4-part format, returning a valid `NSDictionary`
- **Preservation**: Existing parsing behavior for standard 4-part, 5-part, Denmark, and null addresses must remain unchanged
- **formatAddress**: The method in `LabelerUtils.java` that parses a raw address string into a structured dictionary of address components
- **addressSplited**: The `String[]` produced by splitting the address on `", "`
- **Alternative 4-part format**: `[street, city state(abbr), zipCode, country(code)]` — state is in segment[1], zip alone in segment[2]
- **Standard 4-part format**: `[street, city, state(abbr) zipCode, country(code)]` — state+zip combined in segment[2]

## Bug Details

### Bug Condition

The bug manifests when a 4-part address has the state abbreviation embedded in the second segment (alongside the city) and the third segment contains only a zip code. The `formatAddress` method unconditionally calls `addressSplited[2].indexOf("(")` and `addressSplited[2].indexOf(")")` which return -1 on a plain zip code string, causing `substring` to throw `StringIndexOutOfBoundsException`.

**Formal Specification:**

```
FUNCTION isBugCondition(input)
  INPUT: input of type AddressInput (raw address string)
  OUTPUT: boolean

  addressSplited := input.address.split(", ")

  RETURN addressSplited.length = 4
         AND NOT contains(addressSplited[2], "(")
END FUNCTION
```

### Examples

- `"126 East Lincoln Avenue P.O Box 2000, Rahway New Jersey (NJ), 07065, United States (USA)"` → splits into `["126 East Lincoln Avenue P.O Box 2000", "Rahway New Jersey (NJ)", "07065", "United States (USA)"]`. Current code tries to find `"("` in `"07065"`, fails with exception, returns `null`. Expected: valid dictionary with city=Rahway, state=NJ, zip=07065, country=United States.
- `"500 Broadway, New York New York (NY), 10012, United States (USA)"` → same pattern. Current: returns `null`. Expected: city=New York, state=NY, zip=10012, country=United States.
- `"911 North Davis Avenue, Cleveland, Mississippi (MS) 38732, United States (USA)"` → standard format, `addressSplited[2]` is `"Mississippi (MS) 38732"` which contains `"("`. This is NOT the bug condition and must continue working as before.
- `null` address → returns `""`. Not affected by this bug.

## Expected Behavior

### Preservation Requirements

**Unchanged Behaviors:**

- Standard 4-part addresses (state+zip in segment[2]) must continue to parse correctly
- 5-part addresses must continue to parse correctly using the existing 5-part logic
- Denmark-formatted addresses must continue to use the Denmark-specific formatting path
- Null addresses must continue to return an empty string
- The `validateAddress` method must continue to return `true` for unparseable addresses and `false` for parseable ones
- Street number extraction (digit-first addresses) must remain unchanged
- Country code inclusion in the result dictionary must remain unchanged

**Scope:**
All inputs that do NOT match the alternative 4-part format (i.e., where `addressSplited[2]` contains `"("` or the address has a different number of segments) should be completely unaffected by this fix. This includes:

- Standard 4-part addresses with state+zip in segment[2]
- 5-part addresses
- Denmark addresses
- Null addresses
- 3-part addresses (like `"75 Adams Ave, Hauppauge, NY 11788"`)

## Hypothesized Root Cause

Based on the bug description and code analysis, the root cause is:

1. **Missing Format Detection**: The `length == 4` branch assumes a single format where `addressSplited[2]` always contains the state abbreviation in parentheses followed by the zip code. There is no check to detect the alternative format where state is in `addressSplited[1]` and zip is alone in `addressSplited[2]`.

2. **Unconditional Substring Operations**: Lines that call `addressSplited[2].indexOf("(")` and `addressSplited[2].indexOf(")")` will return -1 when the segment is a plain zip code, causing `substring(-1+1, -1)` → `substring(0, -1)` which throws `StringIndexOutOfBoundsException`.

3. **Silent Exception Swallowing**: The outer `try/catch` catches the exception and returns `null`, masking the parsing failure from callers. The `validateAddress` method then reports the address as "not formatable" even though it is a valid address in a slightly different format.

## Correctness Properties

Property 1: Bug Condition - Alternative 4-Part Format Parsing

_For any_ 4-part address input where `addressSplited[2]` does not contain `"("` (isBugCondition returns true), the fixed `formatAddress` function SHALL return a non-null `NSDictionary` with: city extracted from `addressSplited[1]` (text before the state name), state abbreviation extracted from the parentheses in `addressSplited[1]`, zip code from `addressSplited[2]`, and country name from `addressSplited[3]`.

**Validates: Requirements 2.1, 2.2**

Property 2: Preservation - Standard Format and Other Inputs

_For any_ input where the bug condition does NOT hold (standard 4-part with parentheses in segment[2], 5-part addresses, Denmark addresses, null addresses, or other formats), the fixed `formatAddress` function SHALL produce the same result as the original function, preserving all existing parsing behavior unchanged.

**Validates: Requirements 3.1, 3.2, 3.3, 3.4**

## Fix Implementation

### Changes Required

Assuming our root cause analysis is correct:

**File**: `src/main/java/br/com/doit/commons/text/LabelerUtils.java`

**Function**: `formatAddress`

**Specific Changes**:

1. **Add Format Detection**: Inside the `if (addressSplited.length == 4)` block, before the existing parsing logic, add a check: `if (!addressSplited[2].contains("("))` to detect the alternative format.

2. **Alternative Format Parsing Branch**: When the alternative format is detected:
   - Extract country from `addressSplited[3]` using the same `substring(0, indexOf(" ("))` logic
   - Extract state abbreviation from `addressSplited[1]` by finding text between `"("` and `")"`
   - Extract city from `addressSplited[1]` by taking the text before the state name (text before the last word that precedes `"("`)
   - Use `addressSplited[2].trim()` as the zip code

3. **Preserve Standard Format**: The existing parsing logic for standard 4-part format moves into an `else` branch, keeping it completely unchanged.

4. **City Extraction from Combined Segment**: For the alternative format, the city is the portion of `addressSplited[1]` before the state name. This can be extracted by finding the position of `"("` in `addressSplited[1]`, then taking the substring before the state name that precedes the parenthesis. A practical approach: find the index of `" ("` in `addressSplited[1]`, then find the last space before that to separate city from state name, or take everything up to the state full name.

5. **Trim Whitespace**: Apply `.trim()` to extracted values to handle leading/trailing spaces from the split.

### Pseudocode

```java
if (addressSplited.length == 4) {
    country = addressSplited[3].substring(0, addressSplited[3].indexOf(" ("));
    data.put(COUNTRY_KEY, country);

    if (!addressSplited[2].contains("(")) {
        // Alternative format: [street, city state(abbr), zipCode, country(code)]
        String cityStateSegment = addressSplited[1].trim();
        String stateAbbr = cityStateSegment.substring(
            cityStateSegment.indexOf("(") + 1, cityStateSegment.indexOf(")"));
        // City is everything before " StateName (ABBR)"
        // Find the opening paren, then go back past the state name
        int parenIdx = cityStateSegment.indexOf(" (");
        // Find the space before the state full name
        String beforeParen = cityStateSegment.substring(0, parenIdx);
        int lastSpace = beforeParen.lastIndexOf(" ");
        String city = beforeParen.substring(0, lastSpace).trim();

        data.put(STATE_KEY, stateAbbr);
        data.put(CITY_KEY, city);
        data.put(ZIP_CODE_KEY, addressSplited[2].trim());
    } else {
        // Standard format: [street, city, state(abbr) zipCode, country(code)]
        data.put(STATE_KEY, addressSplited[2].substring(
            addressSplited[2].indexOf("(") + 1, addressSplited[2].indexOf(")")));
        data.put(CITY_KEY, addressSplited[1]);
        data.put(ZIP_CODE_KEY, addressSplited[2].substring(
            addressSplited[2].indexOf(") ") + 2));
    }
}
```

## Testing Strategy

### Validation Approach

The testing strategy follows a two-phase approach: first, surface counterexamples that demonstrate the bug on unfixed code, then verify the fix works correctly and preserves existing behavior.

### Exploratory Bug Condition Checking

**Goal**: Surface counterexamples that demonstrate the bug BEFORE implementing the fix. Confirm or refute the root cause analysis. If we refute, we will need to re-hypothesize.

**Test Plan**: Write tests that call `formatAddress` with alternative 4-part format addresses and assert the result is non-null with correct field values. Run these tests on the UNFIXED code to observe failures and confirm the root cause.

**Test Cases**:

1. **Rahway NJ Test**: Call `formatAddress("126 East Lincoln Avenue P.O Box 2000, Rahway New Jersey (NJ), 07065, United States (USA)", "USA")` — will return `null` on unfixed code
2. **New York NY Test**: Call `formatAddress("500 Broadway, New York New York (NY), 10012, United States (USA)", "USA")` — will return `null` on unfixed code
3. **Single-Word City Test**: Call `formatAddress("100 Main St, Springfield Illinois (IL), 62704, United States (USA)", "USA")` — will return `null` on unfixed code
4. **Non-Digit Street Test**: Call `formatAddress("East Lincoln Avenue, Rahway New Jersey (NJ), 07065, United States (USA)", "USA")` — will return `null` on unfixed code (also tests non-digit first character path)

**Expected Counterexamples**:

- All tests return `null` because `addressSplited[2]` (e.g., `"07065"`) does not contain `"("`, causing `indexOf` to return -1 and `substring` to throw `StringIndexOutOfBoundsException`
- Confirms root cause: missing format detection in the `length == 4` branch

### Fix Checking

**Goal**: Verify that for all inputs where the bug condition holds, the fixed function produces the expected behavior.

**Pseudocode:**

```
FOR ALL input WHERE isBugCondition(input) DO
  result := formatAddress_fixed(input.address, input.countryCode)
  ASSERT result IS NOT NULL
  ASSERT result.get("city") = expectedCity(input)
  ASSERT result.get("state") = expectedStateAbbr(input)
  ASSERT result.get("zipCode") = expectedZipCode(input)
  ASSERT result.get("country") = expectedCountryName(input)
END FOR
```

### Preservation Checking

**Goal**: Verify that for all inputs where the bug condition does NOT hold, the fixed function produces the same result as the original function.

**Pseudocode:**

```
FOR ALL input WHERE NOT isBugCondition(input) DO
  ASSERT formatAddress_original(input.address, input.countryCode)
       = formatAddress_fixed(input.address, input.countryCode)
END FOR
```

**Testing Approach**: Property-based testing is recommended for preservation checking because:

- It generates many test cases automatically across the input domain (various city names, state names, zip codes)
- It catches edge cases that manual unit tests might miss (special characters, varying whitespace)
- It provides strong guarantees that behavior is unchanged for all non-buggy inputs

**Test Plan**: Observe behavior on UNFIXED code first for standard 4-part, 5-part, Denmark, and null addresses, then write property-based tests capturing that behavior.

**Test Cases**:

1. **Standard 4-Part Preservation**: Verify `"911 North Davis Avenue, Cleveland, Mississippi (MS) 38732, United States (USA)"` continues to parse correctly after fix
2. **5-Part Preservation**: Verify `"5 Giralda Farms, Dodge Dr, Madison, New Jersey (NJ) 07940, United States (USA)"` continues to parse correctly after fix (if it's a valid 5-part format)
3. **Denmark Preservation**: Verify Denmark-formatted addresses continue to use the Denmark-specific path
4. **Null Input Preservation**: Verify `null` continues to return `""`
5. **3-Part Address Preservation**: Verify `"75 Adams Ave, Hauppauge, NY 11788"` continues to parse as before (only streetName and streetNum extracted)

### Unit Tests

- Test alternative 4-part format with multi-word city names (e.g., "New York")
- Test alternative 4-part format with single-word city names (e.g., "Springfield")
- Test alternative 4-part format with addresses starting with digits and without digits
- Test that standard 4-part format continues to work identically
- Test edge cases: city name that contains parentheses-like characters, very long city names

### Property-Based Tests

- Generate random valid alternative 4-part addresses (random city, random state with abbreviation, random zip, random country) and verify all fields are correctly extracted
- Generate random valid standard 4-part addresses and verify the fix does not alter their parsing
- Generate random 5-part addresses and verify preservation of existing behavior

### Integration Tests

- Test `validateAddress` returns `false` for alternative 4-part format addresses (indicating they ARE formatable)
- Test full round-trip: format an alternative 4-part address and verify all dictionary keys are present and correct
- Test that the `countryCode` parameter is correctly included in the result for both formats
