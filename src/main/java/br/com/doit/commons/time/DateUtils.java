package br.com.doit.commons.time;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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

    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
