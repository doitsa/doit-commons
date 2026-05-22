# Bugfix Requirements Document

## Introduction

The `LabelerUtils.formatAddress` method fails to correctly parse addresses where the 4-part comma-split produces a format with city+state combined in the second segment and zip code alone in the third segment. For example, the address `"126 East Lincoln Avenue P.O Box 2000, Rahway New Jersey (NJ), 07065, United States (USA)"` splits into `["126 East Lincoln Avenue P.O Box 2000", "Rahway New Jersey (NJ)", "07065", "United States (USA)"]`. The current `length == 4` branch expects the state abbreviation and zip code to be combined in `addressSplited[2]` (e.g., `"New Jersey (NJ) 07065"`), but in this format the state is part of `addressSplited[1]` alongside the city, and `addressSplited[2]` contains only the zip code. This causes a parsing failure (exception caught internally) and the method returns `null`.

## Bug Analysis

### Current Behavior (Defect)

1.1 WHEN a 4-part address has the format `[street, city state(abbr), zipCode, country(code)]` (e.g., `"126 East Lincoln Avenue P.O Box 2000, Rahway New Jersey (NJ), 07065, United States (USA)"`) THEN the system throws a StringIndexOutOfBoundsException because it looks for `"("` and `") "` in `addressSplited[2]` which is just a zip code like `"07065"`

1.2 WHEN the parsing exception occurs for the above format THEN the system catches the exception and returns `null` instead of a valid parsed dictionary

### Expected Behavior (Correct)

2.1 WHEN a 4-part address has the format `[street, city state(abbr), zipCode, country(code)]` THEN the system SHALL detect that `addressSplited[2]` does not contain `"("` and recognize this as the alternative format where the state is embedded in `addressSplited[1]`

2.2 WHEN the alternative 4-part format is detected THEN the system SHALL extract the city from `addressSplited[1]` as the text before the state name, extract the state abbreviation from the parentheses in `addressSplited[1]`, use `addressSplited[2]` as the zip code, and extract the country from `addressSplited[3]`, returning a correctly populated dictionary

### Unchanged Behavior (Regression Prevention)

3.1 WHEN a 4-part address has the standard format `[street, city, state(abbr) zipCode, country(code)]` (e.g., `"123 Main St, Springfield, Illinois (IL) 62704, United States (USA)"`) THEN the system SHALL CONTINUE TO correctly parse city from `addressSplited[1]`, state and zip from `addressSplited[2]`, and country from `addressSplited[3]`

3.2 WHEN a 5-part address is provided (e.g., `"123 Main St, Apt 4, Springfield, Illinois (IL) 62704, United States (USA)"`) THEN the system SHALL CONTINUE TO correctly parse the address using the existing 5-part logic

3.3 WHEN the address contains "Denmark (DNK)" THEN the system SHALL CONTINUE TO use the Denmark-specific formatting logic

3.4 WHEN the address is `null` THEN the system SHALL CONTINUE TO return an empty string

---

## Bug Condition (Formal)

```pascal
FUNCTION isBugCondition(X)
  INPUT: X of type AddressInput (address string split by ", " into 4 parts)
  OUTPUT: boolean

  // Returns true when the 4-part split has state embedded in segment[1]
  // and segment[2] is a plain zip code (no parentheses)
  RETURN X.splitLength = 4 AND NOT contains(X.addressSplited[2], "(")
END FUNCTION
```

```pascal
// Property: Fix Checking - Alternative 4-part format handling
FOR ALL X WHERE isBugCondition(X) DO
  result ← formatAddress'(X.address, X.countryCode)
  ASSERT result IS NOT NULL
  ASSERT result.city = extractCityFromCityStateSegment(X.addressSplited[1])
  ASSERT result.state = extractAbbreviationFromParentheses(X.addressSplited[1])
  ASSERT result.zipCode = X.addressSplited[2]
  ASSERT result.country = extractCountryName(X.addressSplited[3])
END FOR
```

```pascal
// Property: Preservation Checking
FOR ALL X WHERE NOT isBugCondition(X) DO
  ASSERT formatAddress(X.address, X.countryCode) = formatAddress'(X.address, X.countryCode)
END FOR
```
