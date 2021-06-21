package com.accenture.clavemovil;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.security.auth.callback.PasswordCallback;

final class KeyStoreHelperDefault {

	private static final KeyStoreHelper HELPER;
	static {
		HELPER = new KeyStoreHelperJseTest();
	}

	static KeyStore getKeyStore() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
		return HELPER.getKeyStore();
	}

	static PasswordCallback getPasswordCallback() {
		return HELPER.getPasswordCallback();
	}

}
