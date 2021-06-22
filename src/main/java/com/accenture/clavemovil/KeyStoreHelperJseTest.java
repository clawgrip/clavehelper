package com.accenture.clavemovil;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.security.auth.callback.PasswordCallback;

import es.gob.afirma.keystores.callbacks.CachePasswordCallback;

final class KeyStoreHelperJseTest implements KeyStoreHelper {

	@Override
	public KeyStore getKeyStore(final KeyStore.PrivateKeyEntry pke) throws NoSuchAlgorithmException,
			                                                               CertificateException,
			                                                               IOException,
			                                                               KeyStoreException {
		final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
		try (
			final InputStream is = Demo.class.getResourceAsStream("/fnmt.p12") //$NON-NLS-1$
		) {
			ks.load(is, "12341234".toCharArray()); //$NON-NLS-1$
		}
		return ks;
	}

	@Override
	public PasswordCallback getPasswordCallback() {
		return new CachePasswordCallback("12341234".toCharArray()); //$NON-NLS-1$
	}

}
