package com.accenture.clavemovil;

import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;

/** Clase de pruebas.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s. */
public final class Demo {

	/** Main para pruebas.
	 * @param args No se usa.
	 * @throws Exception EN cualquier error. */
	public static void main(final String[] args) throws Exception {

		final KeyStore ks = new KeyStoreHelperJseTest().getKeyStore(null);
		final String alias = ks.aliases().nextElement();

		System.out.println(alias);

		final String saml = ClaveSamlAdquirer.getSamlToken(
			AuthProvider.AFIRMA,
			(PrivateKeyEntry) ks.getEntry(
				alias,
				new KeyStore.PasswordProtection("12341234".toCharArray()) //$NON-NLS-1$
			)
		);
		System.out.println(saml);
	}

}
