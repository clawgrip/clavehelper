package com.accenture.clavemovil;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;

import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.core.misc.http.UrlHttpManager;
import es.gob.afirma.core.misc.http.UrlHttpManagerFactory;
import es.gob.afirma.core.misc.http.UrlHttpManagerImpl;
import es.gob.afirma.core.misc.http.UrlHttpMethod;

/** Utilidad para obtener un <i>token>/i> de identidad SAML de Clave.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s. */
public final class ClaveSamlAdquirer {

	private static final String URL_START = "https://cambiodomicilio.redsara.es/pcd/public/login.xhtml"; //$NON-NLS-1$
	private static final String URL_ACCED = "https://cambiodomicilio.redsara.es/pcd/clave/claveInit.xhtml?role=CIUDADANO"; //$NON-NLS-1$

	private ClaveSamlAdquirer() {
		// No instanciable
	}

	/** Obtiene un <i>token>/i> de identidad SAML de Clave.
	 * @param authProvider Modo de autenticaci&oacute;n de Clave a usar.
	 * @param pke Entrada de llavero (necesaria solo si se usa
	 *            autenticaci&oacute;n por certificado).
	 * @return <i>token>/i> de identidad SAML de Clave.
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 */
	public static String getSamlToken(final AuthProvider authProvider,
									  final KeyStore.PrivateKeyEntry pke) throws KeyManagementException,
	                                                                             NoSuchAlgorithmException,
	                                                                             IOException,
	                                                                             KeyStoreException,
	                                                                             CertificateException {
		if (AuthProvider.AFIRMA.equals(authProvider) && pke == null) {
			throw new IllegalArgumentException(
				"Para el uso de Certificado es necesario proporcionar una entrada de llavero" //$NON-NLS-1$
			);
		}

		UrlHttpManagerImpl.disableSslChecks();

		final UrlHttpManager uhm = UrlHttpManagerFactory.getInstalledManager();

		byte[] res = uhm.readUrl(URL_START, UrlHttpMethod.GET);
		res = uhm.readUrl(URL_ACCED, UrlHttpMethod.GET);

		final String claveParamsStr = new String(res);

		//System.out.println(claveParamsStr);

		final String claveParams = claveParamsStr.substring(
			claveParamsStr.indexOf("post('") + 5, //$NON-NLS-1$
			claveParamsStr.indexOf(
				");", //$NON-NLS-1$
				claveParamsStr.indexOf("post('") //$NON-NLS-1$
			)
		);

		final String claveUrl = claveParams.substring(
				claveParams.indexOf("'") + 1, //$NON-NLS-1$
				claveParams.indexOf("'", claveParams.indexOf("'") + 1) //$NON-NLS-1$ //$NON-NLS-2$
		);

		final String clavePostParams = claveParams.substring(
			claveParams.indexOf("{") + 1, //$NON-NLS-1$
			claveParams.indexOf("}") //$NON-NLS-1$
		);

		final String claveRelayState = clavePostParams.substring(
			clavePostParams.indexOf("RelayState: '") + "RelayState: '".length(), //$NON-NLS-1$ //$NON-NLS-2$
			clavePostParams.indexOf("',", clavePostParams.indexOf("RelayState: '")) //$NON-NLS-1$ //$NON-NLS-2$
		);

		final String claveSamlRequest = URLEncoder.encode(
			clavePostParams.substring(
				clavePostParams.indexOf("SAMLRequest: '") + "SAMLRequest: '".length(), //$NON-NLS-1$ //$NON-NLS-2$
				clavePostParams.length()-1
			),
			"UTF-8" //$NON-NLS-1$
		);

//		System.out.println(claveUrl);
//		System.out.println(claveRelayState);
//		System.out.println();
//		System.out.println(URLDecoder.decode(claveSamlRequest, "UTF-8")); //$NON-NLS-1$
//		System.out.println();
//		System.out.println(clavePostParams);

		Properties requestProperties = new Properties();
		requestProperties.setProperty("Content-Type", "application/x-www-form-urlencoded"); //$NON-NLS-1$ //$NON-NLS-2$
		requestProperties.setProperty("Origin", "https://cambiodomicilio.redsara.es"); //$NON-NLS-1$ //$NON-NLS-2$
		requestProperties.setProperty("Referer", "https://cambiodomicilio.redsara.es"); //$NON-NLS-1$ //$NON-NLS-2$
		requestProperties.setProperty("Host", "pasarela.clave.gob.es"); //$NON-NLS-1$ //$NON-NLS-2$
		requestProperties.setProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"); //$NON-NLS-1$ //$NON-NLS-2$
		requestProperties.setProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"); //$NON-NLS-1$ //$NON-NLS-2$
		requestProperties.setProperty("DNT", "1"); //$NON-NLS-1$ //$NON-NLS-2$

		res = uhm.readUrl(
			claveUrl +
				"?RelayState=" + claveRelayState + //$NON-NLS-1$
					"&SAMLRequest=" + claveSamlRequest, //$NON-NLS-1$
			-1,
			UrlHttpMethod.POST,
			requestProperties
		);

//		System.out.println(new String(res));

		final String claveServiceRedirectUrl = claveUrl.replace(
			claveUrl.substring(claveUrl.lastIndexOf("/")), //$NON-NLS-1$
			"" //$NON-NLS-1$
		) + "/ServiceRedirect"; //$NON-NLS-1$

//		System.out.println(claveServiceRedirectUrl);

		requestProperties = new Properties();
		requestProperties.setProperty("Content-Type", "application/x-www-form-urlencoded"); //$NON-NLS-1$ //$NON-NLS-2$
		requestProperties.setProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"); //$NON-NLS-1$ //$NON-NLS-2$
		requestProperties.setProperty("DNT", "1"); //$NON-NLS-1$ //$NON-NLS-2$

		res = uhm.readUrl(
			claveServiceRedirectUrl +
				"?RelayState=" + claveRelayState + //$NON-NLS-1$
					"&SAMLRequest=" + claveSamlRequest +  //$NON-NLS-1$
						"&SelectedIdP=" + authProvider, // AFIRMA PIN24H EIDAS SEGSOC //$NON-NLS-1$
			-1,
			UrlHttpMethod.POST,
			requestProperties
		);

		final String serviceRedirectResponse = new String(res);

		final String providerRedirectUrl = getHtmlAttributeValue(
			"<input type=\"hidden\" name=\"idpUrl\" value=\"", //$NON-NLS-1$
			serviceRedirectResponse
		);

//		System.out.println(serviceRedirectResponse);

		final String newSamlRequest = URLEncoder.encode(
			getHtmlAttributeValue("<input type=\"hidden\" name=\"SAMLRequest\" value=\"", serviceRedirectResponse), //$NON-NLS-1$
			"UTF-8" //$NON-NLS-1$
		);

//		System.out.println(newSamlRequest);
//		System.out.println(providerRedirectUrl);

		final String claveResponse;
		switch(authProvider) {
			case PIN24H:
				claveResponse = managePin24(uhm, providerRedirectUrl, claveRelayState, newSamlRequest, requestProperties);
				break;
			case AFIRMA:
				claveResponse = manageCert(
					uhm,
					providerRedirectUrl,
					claveRelayState,
					newSamlRequest,
					requestProperties,
					pke
				);
				break;
			default:
				throw new UnsupportedOperationException();
		}

		final String samlResponse = URLEncoder.encode(
			getHtmlAttributeValue("<input type=\"hidden\" id=\"SAMLResponse\" name=\"SAMLResponse\" value=\"", claveResponse), //$NON-NLS-1$
			"UTF-8" //$NON-NLS-1$
		);

//		System.out.println(getHtmlAttributeValue("<input type=\"hidden\" id=\"SAMLResponse\" name=\"SAMLResponse\" value=\"", claveResponse)); //$NON-NLS-1$
//		System.out.println();
//		System.out.println();

		return new String(
			Base64.decode(
				URLDecoder.decode(samlResponse, "UTF-8") //$NON-NLS-1$
			)
		);

//		final String serviceBackUrl = getHtmlAttributeValue("<form id=\"redirectForm\" name=\"redirectForm\" method=\"post\" action=\"", claveResponse); //$NON-NLS-1$
//
////		System.out.println(serviceBackUrl);
//
//		res = uhm.readUrl(
//			serviceBackUrl +
//				"?RelayState=" + claveRelayState + //$NON-NLS-1$
//				"&SAMLResponse=" + samlResponse, //$NON-NLS-1$
//			-1,
//			UrlHttpMethod.POST,
//			requestProperties
//		);
//
//		final String backRedirectResponse = new String(res);
//
////		System.out.println(backRedirectResponse);
//
//		final String serviceUrl = getHtmlAttributeValue("action=\"", backRedirectResponse); //$NON-NLS-1$
//		final String serviceSaml =  URLEncoder.encode(
//			getHtmlAttributeValue("<input type=\"hidden\" name=\"SAMLResponse\" value=\"", backRedirectResponse), //$NON-NLS-1$
//			"UTF-8" //$NON-NLS-1$
//		);
//
//		System.out.println(serviceUrl);
////		System.out.println(serviceSaml);
//
//		res = uhm.readUrl(
//				serviceUrl +
//				"?RelayState=" + claveRelayState + //$NON-NLS-1$
//				"&SAMLResponse=" + serviceSaml, //$NON-NLS-1$
//			-1,
//			UrlHttpMethod.POST,
//			requestProperties
//		);
//
////		System.out.println(new String(res));

	}

	private static String manageCert(final UrlHttpManager uhm,
			                         final String providerRedirectUrl,
			                         final String claveRelayState,
			                         final String newSamlRequest,
			                         final Properties requestProperties,
									 final KeyStore.PrivateKeyEntry pke) throws KeyStoreException,
	                                                                            NoSuchAlgorithmException,
	                                                                            CertificateException,
	                                                                            IOException,
	                                                                            KeyManagementException {
		final KeyStore ks = KeyStoreHelperDefault.getKeyStore(pke);
		if (ks != null) {
			UrlHttpManagerImpl.setSslKeyStore(ks);
			UrlHttpManagerImpl.setSslKeyStorePasswordCallback(
					KeyStoreHelperDefault.getPasswordCallback()
			);
			UrlHttpManagerImpl.disableSslChecks(); // Para refrescar el KeyManager SSL
		}

		final byte[] res = uhm.readUrl(
			providerRedirectUrl +
				"?RelayState=" + claveRelayState + //$NON-NLS-1$
					"&SAMLRequest=" + newSamlRequest, //$NON-NLS-1$
			-1,
			UrlHttpMethod.POST,
			requestProperties
		);

		return new String(res);
	}

	private static String managePin24(final UrlHttpManager uhm,
			                          final String providerRedirectUrl,
			                          final String claveRelayState,
			                          final String newSamlRequest,
			                          final Properties requestProperties) throws IOException {

		final byte[] res = uhm.readUrl(
			providerRedirectUrl +
				"?RelayState=" + claveRelayState + //$NON-NLS-1$
					"&SAMLRequest=" + newSamlRequest, //$NON-NLS-1$
			-1,
			UrlHttpMethod.POST,
			requestProperties
		);

		System.out.println(new String(res));

		throw new UnsupportedOperationException();
	}

	private static String getHtmlAttributeValue(final String tag, final String html) {
		return html.substring(
			html.indexOf(tag) + tag.length(),
			html.indexOf(
				"\"", //$NON-NLS-1$
				html.indexOf(tag) + tag.length() + 1
			)
		);
	}

}
