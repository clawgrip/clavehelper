package com.accenture.clavemovil;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.security.auth.callback.PasswordCallback;

interface KeyStoreHelper {

	KeyStore getKeyStore() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException;
	PasswordCallback getPasswordCallback();

}
