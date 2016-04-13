package br.com.doit.commons.l10n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 * <p>
 * A classe {@code Messages} tem como objetivo ajudar a traduzir (internacionalizar) mensagens internas do sistema. Essa
 * classe foi desenvolvida para ser usada de forma parecida com um Logger. Exemplo:
 * </p>
 *
 * <pre>
 * private static final Messages messages = Messages.getMessages("caminho/para/arquivo/com/mensagens");
 * </pre>
 * <p>
 * Por padrão, essa classe vai buscar por mensagens no {@code Locale} padrão. É possível fornecer um
 * {@code Supplier<Locale>} para obter o {@code Locale} de outra forma. Exemplo:
 * </p>
 *
 * <pre>
 * Messages.setSupplierOfLocale(() -> Locale.FRANCE);
 * </pre>
 * <p>
 * O {@code Locale} será determnado a cada chamada a um dos métodos para obter uma mensagem.
 * </p>
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class Messages {
    private static boolean isCacheEnabled = true;

    private static Supplier<Locale> localeSupplier;

    /**
     * Factory method para obter uma instância de {@code Messages}.
     *
     * @param resourceBundleName
     *            O nome do {@code ResourceBundle} que será carregado por esse localizer.
     * @return Retorna um {@code Messages} capaz de retornar as mensagens presentes no {@code ResourceBundle}
     *         informado.
     */
    public static Messages getMessages(String resourceBundleName) {
        // Verifica se o ResourceBundle existe
        ResourceBundle.getBundle(resourceBundleName);

        return new Messages(resourceBundleName);
    }

    /**
     * Habilita/desabilita o cache do localizer. Quando o cache está desabilitado, os arquivos serão recarregados toda
     * vez que uma mensagem for obtida.
     *
     * @param isCacheEnabled
     *            Determina se o cache de ser habilitado ou não.
     */
    public static void setIsCacheEnabled(boolean isCacheEnabled) {
        Messages.isCacheEnabled = isCacheEnabled;
    }

    /**
     * Permite configurar um {@code Supplier} de {@code Locale}. Dessa forma, na hora de obter uma mensagem, o idioma
     * correto é utilizado.
     *
     * @param localeSupplier
     *            Um {@code Supplier} de {@code Locale}.
     */
    public static void setSupplierOfLocale(Supplier<Locale> localeSupplier) {
        Messages.localeSupplier = localeSupplier;
    }

    private final String resourceBundleName;

    private Messages(String resourceBundleName) {
        this.resourceBundleName = resourceBundleName;
    }

    /**
     * Formata a mensagem obtida a partir da chave informada usando os objetos passados por parâmetro.
     *
     * @param key
     *            A chave que será utilizada para obter a mensagem.
     * @param args
     *            Os parâmetros que serão utilizados para formatar a mensagem.
     * @return Retorna a mensagem de texto formatada.
     */
    public String format(String key, Object... args) {
        return new MessageFormat(get(key)).format(args);
    }

    /**
     * Obtém a mensagem a partir de uma chave.
     *
     * @param key
     *            A chave que será utilizada para obter a mensagem.
     * @return Retorna a mensagem de texto correspondente à chave informada ou {@code null} caso não seja encontrada uma
     *         mensagem com a chave informada.
     */
    public String get(String key) {
        ResourceBundle messages = messages();

        if (!messages.containsKey(key)) {
            return null;
        }

        return messages.getString(key);
    }

    private ResourceBundle messages() {
        Locale locale = localeSupplier != null ? localeSupplier.get() : Locale.getDefault();

        if (!isCacheEnabled) {
            ResourceBundle.clearCache();
        }

        return ResourceBundle.getBundle(resourceBundleName, locale);
    }
}
