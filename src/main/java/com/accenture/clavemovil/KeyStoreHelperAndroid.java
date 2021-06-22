package com.accenture.clavemovil;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Security;
import java.util.Enumeration;

import javax.security.auth.callback.PasswordCallback;

import com.accenture.android.keychain.KeyChainKeyStore;
import com.accenture.android.keychain.KeyChainKeyStoreImpl;
import com.accenture.android.keychain.KeyChainKeyStoreProvider;

import es.gob.afirma.keystores.callbacks.NullPasswordCallback;

/** Utilidad para el manejo de <code>KeyStore</code> asociado a una entrada de
 * llavero de Android.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s. */
public final class KeyStoreHelperAndroid implements KeyStoreHelper {

    @Override
    public KeyStore getKeyStore(final KeyStore.PrivateKeyEntry pke) throws KeyStoreException {
        Security.addProvider(new KeyChainKeyStoreProvider());
        final KeyStore ks = new KeyChainKeyStore(
            new KeyChainKeyStoreImpl(pke),
            new KeyChainKeyStoreProvider()
        );

        final Enumeration<String> aliases = ks.aliases();
        while (aliases.hasMoreElements()) {
            System.out.println(aliases.nextElement());
        }

        return ks;
    }

    @Override
    public PasswordCallback getPasswordCallback() {
        return NullPasswordCallback.getInstance();
    }
}
