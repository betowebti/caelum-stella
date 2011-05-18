package br.com.caelum.stella.validation.ie;

import java.util.regex.Pattern;

import br.com.caelum.stella.MessageProducer;
import br.com.caelum.stella.SimpleMessageProducer;
import br.com.caelum.stella.validation.DigitoVerificadorInfo;
import br.com.caelum.stella.validation.RotinaDeDigitoVerificador;
import br.com.caelum.stella.validation.ValidadorDeDV;

/**
 * <p>
 * Documentação de referência:
 * </p>
 * <a href="http://www.pfe.fazenda.sp.gov.br/consist_ie.shtm">Secretaria da
 * Fazenda do Estado de São Paulo</a> <a
 * href="http://www.sintegra.gov.br/Cad_Estados/cad_AL.html">SINTEGRA - ROTEIRO
 * DE CRÍTICA DA INSCRIÇÃO ESTADUAL </a>
 * 
 * @author Leonardo Bessa
 * 
 */
public class IEAlagoasValidator extends AbstractIEValidator {

    private static final int MOD = 11;

    private static final int DVX_POSITION = 5 + 9;

    private static final Integer[] DVX_MULTIPLIERS = IEConstraints.P1;

    private static final RotinaDeDigitoVerificador[] rotinas = { IEConstraints.Rotina.B, IEConstraints.Rotina.D,
            IEConstraints.Rotina.POS_IE };

    private static final DigitoVerificadorInfo DVX_INFO = new DigitoVerificadorInfo(0, rotinas, MOD, DVX_MULTIPLIERS,
            DVX_POSITION);

    private static final ValidadorDeDV DVX_CHECKER = new ValidadorDeDV(DVX_INFO);

    /*
     * FORMAÇÃO: 24XNNNNND, sendo:
     * 
     * 24 – Código do Estado
     * 
     * X – Tipo de empresa (0-Normal, 3-Produtor Rural,5-Substituta, 7-
     * Micro-Empresa Ambulante, 8-Micro-Empresa)
     * 
     * NNNNN – Número da empresa
     * 
     * D – Dígito de verificação calculado pelo Módulo11,pêsos 2 à 9 da direita
     * para a esquerda, exceto D
     * 
     * Exemplo: 2 4 0 0 0 0 0 4 8
     */

    public static final Pattern FORMATED = Pattern.compile("([2][4])[.](\\d{3})[.](\\d{3})[-](\\d{1})");

    public static final Pattern UNFORMATED = Pattern.compile("([2][4])(\\d{3})(\\d{3})(\\d{1})");

	/**
	 * Este considera, por padrão, que as cadeias estão formatadas e utiliza um
	 * {@linkplain SimpleMessageProducer} para geração de mensagens.
	 */
	public IEAlagoasValidator() {
		super(true);
	}

	/**
	 * O validador utiliza um {@linkplain SimpleMessageProducer} para geração de
	 * mensagens.
	 * 
	 * @param isFormatted
	 *            considerar cadeia formatada quando <code>true</code>
	 */
	public IEAlagoasValidator(boolean isFormatted) {
		super(isFormatted);
	}

	public IEAlagoasValidator(MessageProducer messageProducer, boolean isFormatted) {
		super(messageProducer, isFormatted);
	}


	@Override
	protected Pattern getUnformattedPattern() {
		return UNFORMATED;
	}

	@Override
	protected Pattern getFormattedPattern() {
		return FORMATED;
	}
	
    protected boolean hasValidCheckDigits(String value) {
        String testedValue = IEConstraints.PRE_VALIDATION_FORMATTER.format(value);
        return DVX_CHECKER.isDVValid(testedValue);
    }

}
