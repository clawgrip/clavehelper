package com.accenture.clavemovil;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.security.auth.callback.PasswordCallback;

import es.gob.afirma.core.misc.Platform;

final class KeyStoreHelperDefault {

	private static final KeyStoreHelper HELPER;
	static {
		if (Platform.OS.ANDROID.equals(Platform.getOS())) {
			HELPER = new KeyStoreHelperAndroid();
		}
		else {
			//HELPER = new KeyStoreHelperJseTest();
			HELPER = new KeyStoreHelperAndroid();
		}
	}

	static KeyStore getKeyStore(final KeyStore.PrivateKeyEntry pke) throws NoSuchAlgorithmException,
			                                                               CertificateException,
														                   IOException,
			                                                               KeyStoreException {
		return HELPER.getKeyStore(pke);
	}

	static PasswordCallback getPasswordCallback() {
		return HELPER.getPasswordCallback();
	}

}
