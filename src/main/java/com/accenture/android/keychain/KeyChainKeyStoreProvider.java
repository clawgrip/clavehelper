package com.accenture.android.keychain;

import java.security.Provider;

/** Proveedor para <code>KeyStore</code> de una &uacute;nica entrada de llavero.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s. */
public final class KeyChainKeyStoreProvider extends Provider {

	private static final long serialVersionUID = 7480379973684145974L;

	/** Tipo del <code>KeyStore</code>. */
	public static final String TYPE = "ONEENTRY"; //$NON-NLS-1$

    /** Construye un <code>KeyStore</code> de una &uacute;nica entrada de
     * llavero. */
    public KeyChainKeyStoreProvider() {
        super(
            "OneEntryKeyStoreProvider", //$NON-NLS-1$
            0.1d,
            "Proveedor de KeyStore para una unica entrada de llavero" //$NON-NLS-1$
        );

        put("KeyStore." + TYPE, "com.accenture.android.keychain.KeyChainKeyStoreImpl"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
