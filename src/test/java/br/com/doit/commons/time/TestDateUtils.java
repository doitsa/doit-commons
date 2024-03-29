package br.com.doit.commons.time;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.webobjects.foundation.NSTimestamp;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestDateUtils {
    @Test
    public void createDateWithDefaultTimeZoneWhenConvertingFromLocalDate() throws Exception {
        LocalDate localDate = LocalDate.of(1983, 1, 7);

        Date result = DateUtils.toDate(localDate);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(1983));
        assertThat(calendar.get(Calendar.MONTH), is(0));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(7));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(0));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(0));
    }

    @Test
    public void createDateWithGivenTimeZoneIdWhenConvertingFromLocalDate() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));

        LocalDate localDate = LocalDate.of(1983, 1, 7);

        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        Date result = DateUtils.toDateWithUserZoneId(localDate, zoneId);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(1983));
        assertThat(calendar.get(Calendar.MONTH), is(0));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(6));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(22));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(0));
    }

    @Test
    public void returnNullWhenCovertingFromLocalDateAndNullZoneToDate() throws Exception {
        LocalDate localDate = LocalDate.of(1983, 1, 7);

        Date result = DateUtils.toDateWithUserZoneId(localDate, null);

        assertThat(result, nullValue());
    }

    @Test
    public void returnNullWhenConvertingFromNullLocalDateToDate() throws Exception {
        Date result = DateUtils.toDate(null);

        assertThat(result, nullValue());
    }

    @Test
    public void createLocalDateWithDefaultTimeZoneWhenConvertingFromDate() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1983-01-07");

        LocalDate result = DateUtils.toLocalDate(date);

        assertThat(result, is(LocalDate.of(1983, 1, 7)));
    }

    @Test
    public void returnNullWhenConvertingFromNullDateToLocalDate() throws Exception {
        LocalDate result = DateUtils.toLocalDate(null);

        assertThat(result, nullValue());
    }

    @Test
    public void createDateWithDefaultTimeZoneWhenConvertingFromLocalDateTime() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(1991, 2, 16, 9, 0, 10);

        Date result = DateUtils.toDateTime(localDateTime);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(1991));
        assertThat(calendar.get(Calendar.MONTH), is(1));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(16));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(9));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(10));
    }

    @Test
    public void returnNullWhenConvertingFromNullLocalDateToDateTime() throws Exception {
        Date result = DateUtils.toDate(null);

        assertThat(result, nullValue());
    }

    @Test
    public void createLocalDateWithDefaultTimeZoneWhenConvertingFromDateTime() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1991-02-16 09:00:10");

        LocalDateTime result = DateUtils.toLocalDateTime(date);

        assertThat(result, is(LocalDateTime.of(1991, 2, 16, 9, 0, 10)));
    }

    @Test
    public void returnNullWhenConvertingFromNullDateToLocalDateTime() throws Exception {
        LocalDate result = DateUtils.toLocalDate(null);

        assertThat(result, nullValue());
    }

    @Test
    public void createDateWithDefaultTimeZoneWhenConvertingFromLocalTime() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1991-02-16 00:00:00");
        LocalTime localTime = LocalTime.of(9, 0, 31);

        Date result = DateUtils.toDateTime(date, localTime);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(1991));
        assertThat(calendar.get(Calendar.MONTH), is(1));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(16));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(9));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(31));
    }

    @Test
    public void returnNullWhenConvertingFromNullLocalDateToLocalTime() throws Exception {
        Date result = DateUtils.toDateTime(null, null);

        assertThat(result, nullValue());
    }

    @Test
    public void createLocalDateWithDefaultTimeZoneWhenConvertingFromLocalTime() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1991-02-16 09:00:10");

        LocalTime result = DateUtils.toLocalTime(date);

        assertThat(result, is(LocalTime.of(9, 0, 10)));
    }

    @Test
    public void returnNullWhenConvertingFromNullDateToLocalTime() throws Exception {
        LocalTime result = DateUtils.toLocalTime(null);

        assertThat(result, nullValue());
    }

    @Test
    public void returnMidnightWhenConvertingFromNullLocalTimeAndPassingAValidDate() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1991-02-16 00:00:00");

        Date result = DateUtils.toDateTime(date, null);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(1991));
        assertThat(calendar.get(Calendar.MONTH), is(1));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(16));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(0));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(0));
    }

    @Test
    public void returnNSTimestampWhenConvertingFromLocalDateTime() throws Exception {
        NSTimestamp result = DateUtils.toNSTimestamp(LocalDateTime.of(2016, 5, 3, 2, 45));

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(2016));
        assertThat(calendar.get(Calendar.MONTH), is(4));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(3));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(2));
        assertThat(calendar.get(Calendar.MINUTE), is(45));
        assertThat(calendar.get(Calendar.SECOND), is(0));
    }

    @Test
    public void returnNullNSTimestampWhenConvertingFromNullLocalDateTime() throws Exception {
        NSTimestamp result = DateUtils.toNSTimestamp((LocalDateTime) null);

        assertThat(result, nullValue());
    }

    @Test
    public void returnNSTimestampWhenConvertingFromLocalDate() throws Exception {
        NSTimestamp result = DateUtils.toNSTimestamp(LocalDate.of(2016, 5, 3));

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(2016));
        assertThat(calendar.get(Calendar.MONTH), is(4));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(3));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(0));
        assertThat(calendar.get(Calendar.MINUTE), is(0));
        assertThat(calendar.get(Calendar.SECOND), is(0));
    }

    @Test
    public void returnNullNSTimestampWhenConvertingFromNullLocalDate() throws Exception {
        NSTimestamp result = DateUtils.toNSTimestamp((LocalDate) null);

        assertThat(result, nullValue());
    }

    @Test
    public void createOffsetDateTimeWithDefaultTimeZoneOffsetWhenConvertingFromDate() throws Exception {
        TimeZone timezoneBefore = TimeZone.getDefault();
        TimeZone timezone = TimeZone.getTimeZone("America/Sao_Paulo");
        TimeZone.setDefault(timezone);
        Date date = new Date();

        OffsetDateTime result = DateUtils.toOffsetDateTime(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        assertThat(result.getYear(), is(calendar.get(Calendar.YEAR)));
        assertThat(result.getMonthValue(), is(calendar.get(Calendar.MONTH) + 1));
        assertThat(result.getDayOfMonth(), is(calendar.get(Calendar.DAY_OF_MONTH)));
        assertThat(result.getHour(), is(calendar.get(Calendar.HOUR_OF_DAY)));
        assertThat(result.getMinute(), is(calendar.get(Calendar.MINUTE)));
        assertThat(result.getSecond(), is(calendar.get(Calendar.SECOND)));
        assertThat(result.getOffset().getTotalSeconds(), is(timezone.getOffset(date.getTime()) / 1000));

        TimeZone.setDefault(timezoneBefore);
    }

    @Test
    public void createOffsetDateTimeWithGivenZoneIdWhenConvertingFromDate() throws Exception {
        TimeZone timezoneBefore = TimeZone.getDefault();
        TimeZone timezone = TimeZone.getTimeZone("US/Eastern");
        TimeZone.setDefault(timezone);
        Date date = new Date(1699311985580L);

        OffsetDateTime result = DateUtils.toOffsetDateTime(date, ZoneId.of("US/Pacific"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        assertThat(result.getYear(), is(calendar.get(Calendar.YEAR)));
        assertThat(result.getMonthValue(), is(calendar.get(Calendar.MONTH) + 1));
        assertThat(result.getDayOfMonth(), is(calendar.get(Calendar.DAY_OF_MONTH)));
        assertThat(result.getHour(), is(calendar.get(Calendar.HOUR_OF_DAY) - 3));
        assertThat(result.getMinute(), is(calendar.get(Calendar.MINUTE)));
        assertThat(result.getSecond(), is(calendar.get(Calendar.SECOND)));
        assertThat(result.getOffset().getTotalSeconds(), is(-28800));

        TimeZone.setDefault(timezoneBefore);
    }

    @Test
    public void returnNullWhenConvertingFromNullDateToOffsetDateTime() throws Exception {
        OffsetDateTime result = DateUtils.toOffsetDateTime(null);

        assertThat(result, nullValue());
    }

    @Test
    public void returnNSTimestampWhenConvertingFromOffsetDateTime() throws Exception {
        Instant instant = Instant.now();
        ZoneId systemZone = ZoneId.systemDefault();
        ZoneOffset currentOffset = systemZone.getRules().getOffset(instant);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2016, 5, 3, 10, 56, 2, 0, currentOffset);
        NSTimestamp result = DateUtils.toNSTimestamp(offsetDateTime);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(result);

        assertThat(calendar.get(Calendar.YEAR), is(2016));
        assertThat(calendar.get(Calendar.MONTH), is(4));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(3));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(10));
        assertThat(calendar.get(Calendar.MINUTE), is(56));
        assertThat(calendar.get(Calendar.SECOND), is(2));
    }

    @Test
    public void returnNullNSTimestampWhenConvertingFromNullOffsetDateTime() throws Exception {
        NSTimestamp result = DateUtils.toNSTimestamp((OffsetDateTime) null);

        assertThat(result, nullValue());
    }
}
