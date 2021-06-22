package com.accenture.android.keychain;

import java.security.KeyStore;

/** <code>KeyStore</code> de una &uacute;nica entrada de llavero.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s. */
public final class KeyChainKeyStore extends KeyStore {

	/** Construye un <code>KeyStore</code> de una &uacute;nica entrada de
	 * llavero.
	 * @param keyStoreSpi Implementaci&oacute;n del <code>KeyStore</code>.
	 * @param provider Proveedor del <code>KeyStore</code>. */
    public KeyChainKeyStore(final KeyChainKeyStoreImpl keyStoreSpi,
                            final KeyChainKeyStoreProvider provider) {
        super(keyStoreSpi, provider, KeyChainKeyStoreProvider.TYPE);
        try {
            load(null, null);
        }
        catch (final Exception e) {
            // Se ignora
        }
    }
}
