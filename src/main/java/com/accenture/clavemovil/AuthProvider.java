package com.accenture.clavemovil;

/** Tipo de autenticaci&oacute;n de Cl&#64;ve.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s. */
public enum AuthProvider {
	/** Certificado. */
	AFIRMA,
	/** Cl&#64;ve PIN (PIN24H, AEAT). */
	PIN24H,
	/** eIDAS (iedntidad europea). */
	EIDAS,
	/** Cl&#64;ve Permanente (Seguridad Social). */
	SEGSOC
}
