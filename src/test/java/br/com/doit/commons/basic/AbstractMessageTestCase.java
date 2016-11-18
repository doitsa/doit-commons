package br.com.doit.commons.basic;

import java.util.Locale;

import org.junit.Before;

import br.com.doit.commons.l10n.Messages;

public abstract class AbstractMessageTestCase {

    @Before
    public void prepareLocale() {
        Messages.setSupplierOfLocale(() -> Locale.ENGLISH);
    }

}
