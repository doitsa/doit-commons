package br.com.doit.commons.text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

import org.apache.commons.lang.StringUtils;

/**
 * <code>HoursFormat</code> formats integer numbers into hours representation and vice-versa.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class HoursFormat extends Format {
    private static final int MINUTES_PER_HOUR = 60;

    @Override
    public StringBuffer format(Object object, StringBuffer toAppendTo, FieldPosition position) {
        if (object == null) {
            return toAppendTo;
        }

        if (!(object instanceof Integer)) {
            throw new IllegalArgumentException("Cannot parse an object of type " + object.getClass().getName()
                    + " when expecting an Integer.");
        }

        Integer minutes = (Integer) object;

        if (minutes < 0) {
            toAppendTo.append("-");
        }

        minutes = Math.abs(minutes);

        Integer hours = minutes / MINUTES_PER_HOUR;

        String formattedValue;

        if (hours < 1000) {
            formattedValue = String.format("%2d:%2d", hours, minutes % MINUTES_PER_HOUR);
        } else {
            formattedValue = String.format(":%2d", minutes % MINUTES_PER_HOUR);

            DecimalFormat df = new DecimalFormat("#,##0");

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();

            symbols.setGroupingSeparator('.');

            df.setDecimalFormatSymbols(symbols);

            String formatHours = df.format(hours);

            formattedValue = formatHours + formattedValue;
        }

        toAppendTo.append(formattedValue.replaceAll(" ", "0"));

        return toAppendTo;
    }

    @Override
    public Object parseObject(String source) throws ParseException {
        try {
            return super.parseObject(source);
        } catch (Exception exception) {
            throw new ParseException("Cannot parse the String '" + source
                    + "'. Expecting some value in the format hh:mm or hh.", 0);
        }
    }

    @Override
    public Object parseObject(String source, ParsePosition position) {
        if (source == null) {
            position.setIndex(-1);

            return null;
        }

        if (!source.contains(":")) {
            position.setIndex(1);

            source = source.replaceAll("\\.", "");

            return Integer.parseInt(source) * MINUTES_PER_HOUR;
        }

        boolean isNegative = false;

        if (source.startsWith("-")) {
            source = source.replace("-", "");

            isNegative = true;
        }

        String[] splittedValues = StringUtils.split(source, ":");

        String hoursStr = splittedValues[0].replaceAll("\\.", "");

        Integer hours = Integer.parseInt(hoursStr);
        Integer minutes = Integer.parseInt(splittedValues[1]);

        position.setIndex(5);

        int result = hours * MINUTES_PER_HOUR + minutes;

        if (isNegative) {
            result = result * -1;

            position.setIndex(6);
        }

        return result;
    }
}
