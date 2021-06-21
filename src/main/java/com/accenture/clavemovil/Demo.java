package com.accenture.clavemovil;

/** Clase de pruebas.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s. */
public final class Demo {

	/** Main para pruebas.
	 * @param args No se usa.
	 * @throws Exception EN cualquier error. */
	public static void main(final String[] args) throws Exception {
		final String saml = ClaveSamlAdquirer.getSamlToken(AuthProvider.AFIRMA);
		System.out.println(saml);
	}

}
