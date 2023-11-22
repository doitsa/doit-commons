package br.com.doit.commons.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import com.webobjects.foundation.NSTimestamp;

/**
 * Classe utilitária para ajudar na conversão de objetos da nova API de data de Java 8 (pacote {@code java.time}) para o
 * antigo {@code java.util.Date}.
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class DateUtils {
    /**
     * Converte um {@code java.time.LocalDate} para {@code java.util.Date} usando o timezone definido pelo sistema.
     *
     * @param localDate
     *            Uma data (pode ser nula).
     * @return Retorna um {@code java.util.Date} para a data informada ou {@code null} caso nenhuma data tenha sido
     *         informada.
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converte um {@code java.util.Date} para {@code java.time.LocalDate} usando o timezone definido pelo sistema.
     *
     * @param date
     *            Uma data (pode ser nula).
     * @return Retorna um {@code java.time.LocalDate} para a data informada ou {@code null} caso nenhuma data tenha sido
     *         informada.
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Converte um {@code java.time.LocalDateTime} para {@code java.util.Date} usando o timezone definido pelo sistema.
     *
     * @param localDateTime
     *            Uma data (pode ser nula).
     * @return Retorna um {@code java.util.Date} para a data informada ou {@code null} caso nenhuma data tenha sido
     *         informada.
     */
    public static Date toDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converte um {@code java.time.OffsetDateTime} para {@code java.util.Date} usando o offset definido pelo
     * {@code java.time.OffsetDateTime}.
     *
     * @param offsetDateTime
     *            Uma data/hora (pode ser nula).
     * @return Retorna um {@code java.util.Date} para a data/hora informada ou {@code null} caso nenhuma data tenha sido
     *         informada.
     */
    public static Date toDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }

        return Date.from(offsetDateTime.toInstant());
    }

    /**
     * Converte um {@code java.util.Date} para {@code java.time.LocalDateTime} usando o timezone definido pelo sistema.
     *
     * @param date
     *            Uma data (pode ser nula).
     * @return Retorna um {@code java.time.LocalDateTime} para a data informada ou {@code null} caso nenhuma data tenha
     *         sido
     *         informada.
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Converte um {@code java.time.LocalTime} para {@code java.util.Date} usando o timezone definido pelo sistema.
     *
     * @param localTime
     *            Uma data (pode ser nula).
     * @param localDate
     *            Uma data (pode ser nula).
     * @return Retorna um {@code java.util.Date} para a data informada ou {@code null} caso nenhuma data tenha sido
     *         informada.
     */
    public static Date toDateTime(Date date, LocalTime localTime) {
        if (date == null) {
            return null;
        }

        if (localTime == null) {
            localTime = LocalTime.MIDNIGHT;
        }

        return Date.from(localTime.atDate(toLocalDate(date)).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converte um {@code java.util.Date} para {@code java.time.LocalTime} usando o timezone definido pelo sistema.
     *
     * @param date
     *            Uma data (pode ser nula).
     * @return Retorna um {@code java.time.LocalTime} para a data informada ou {@code null} caso nenhuma data tenha
     *         sido informada.
     */
    public static LocalTime toLocalTime(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * Converte um {@code java.time.LocalDateTime} para {@code com.webobjects.foundation.NSTimestamp} usando o timezone
     * definido pelo sistema.
     *
     * @param dateTime
     *            Uma data (pode ser nula).
     * @return Retorna um {@code com.webobjects.foundation.NSTimestamp} para a data informada ou {@code null} caso
     *         nenhuma
     *         data tenha sido informada.
     */
    public static NSTimestamp toNSTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return new NSTimestamp(toDateTime(dateTime));
    }

    /**
     * Converte um {@code java.time.LocalDate} para {@code com.webobjects.foundation.NSTimestamp} usando o timezone
     * definido pelo sistema.
     *
     * @param localDate
     *            Uma data (pode ser nula).
     * @return Retorna um {@code com.webobjects.foundation.NSTimestamp} para a data informada ou {@code null} caso
     *         nenhuma
     *         data tenha sido informada.
     */
    public static NSTimestamp toNSTimestamp(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        return new NSTimestamp(toDate(localDate));
    }

    /**
     * Converte um {@code java.time.OffsetDateTime} para {@code com.webobjects.foundation.NSTimestamp} usando o offset
     * definido pelo {@code java.time.OffsetDateTime}.
     *
     * @param offsetDateTime
     *            Uma data/hora (pode ser nula).
     * @return Retorna um {@code com.webobjects.foundation.NSTimestamp} para a data/hora informada ou {@code null} caso
     *         nenhuma data/hora tenha sido informada.
     */
    public static NSTimestamp toNSTimestamp(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }

        return new NSTimestamp(toDateTime(offsetDateTime));
    }

    /**
     * Converte um {@code java.util.Date} para {@code java.time.OffsetTime} usando o timezone definido pelo sistema.
     *
     * @param date
     *            Uma data (pode ser nula).
     * @return Retorna um {@code java.time.OffsetTime} para a data informada ou {@code null} caso nenhuma data tenha
     *         sido informada.
     */
    public static OffsetDateTime toOffsetDateTime(Date date) {
        return toOffsetDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Converte um {@code java.util.Date} para {@code java.time.OffsetTime} usando o timezone passado por parâmetro.
     *
     * @param date
     *            Uma data (pode ser nula).
     * @param zoneId
     *            Um timezone.
     * @return Retorna um {@code java.time.OffsetTime} para a data informada ou {@code null} caso nenhuma data tenha
     *         sido informada.
     */
    public static OffsetDateTime toOffsetDateTime(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }

        Instant instant = date.toInstant();
        ZoneOffset offset = zoneId.getRules().getOffset(instant);

        return instant.atOffset(offset);
    }
}
