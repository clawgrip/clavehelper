package com.accenture.android.keychain;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import es.gob.afirma.core.misc.AOUtil;

/** Implementaci&oacute;n de un <code>KeyStore</code> para una entrada de
 * llavero dada.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s. */
public final class KeyChainKeyStoreImpl extends KeyStoreSpi {

    private final PrivateKey privateKey;
    private final Certificate[] certChain;

    /** Construye un <code>KeyStore</code> para una entrada de
     * llavero dada.
     * @param pke Entrada de llavero que contendr&aacute; el
     *            <code>KeyStore</code>. */
    public KeyChainKeyStoreImpl(final KeyStore.PrivateKeyEntry pke) {
        if (pke == null) {
            throw new IllegalArgumentException(
                "Es necesario proporcionar una entrada de llavero" //$NON-NLS-1$
            );
        }
        this.privateKey = pke.getPrivateKey();
        this.certChain = pke.getCertificateChain();
    }

    @Override
    public Key engineGetKey(final String alias, final char[] password) {
        return this.privateKey;
    }

    @Override
    public Certificate[] engineGetCertificateChain(final String alias) {
        return this.certChain;
    }

    @Override
    public Certificate engineGetCertificate(final String alias) {
        return this.certChain[0];
    }

    @Override
    public Date engineGetCreationDate(final String alias) {
        return new Date();
    }

    @Override
    public void engineSetKeyEntry(final String alias,
                                  final Key key,
                                  final char[] password,
                                  final Certificate[] chain) {
        throw new UnsupportedOperationException("No implementado"); //$NON-NLS-1$
    }

    @Override
    public void engineSetKeyEntry(final String alias,
                                  final byte[] key,
                                  final Certificate[] chain) {
        throw new UnsupportedOperationException("No implementado"); //$NON-NLS-1$
    }

    @Override
    public void engineSetCertificateEntry(final String alias, final Certificate cert) {
        throw new UnsupportedOperationException("No implementado"); //$NON-NLS-1$
    }

    @Override
    public void engineDeleteEntry(final String alias) {
        throw new UnsupportedOperationException("No implementado"); //$NON-NLS-1$
    }

    @Override
    public Enumeration<String> engineAliases() {
        final List<String> list = new ArrayList<>();
        list.add(
            AOUtil.getCN((X509Certificate) this.certChain[0])
        );
        return Collections.enumeration(list);
    }

    @Override
    public boolean engineContainsAlias(final String alias) {
        return true;
    }

    @Override
    public int engineSize() {
        return 1;
    }

    @Override
    public boolean engineIsKeyEntry(final String alias) {
        return true;
    }

    @Override
    public boolean engineIsCertificateEntry(final String alias) {
        return false;
    }

    @Override
    public String engineGetCertificateAlias(final Certificate cert) {
        return null;
    }

    @Override
    public void engineStore(final OutputStream stream, final char[] password) {
        // Vacio
    }

    @Override
    public void engineLoad(final InputStream stream, final char[] password) {
        // Vacio
    }
}
