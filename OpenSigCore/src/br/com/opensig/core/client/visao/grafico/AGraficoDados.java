package br.com.opensig.core.client.visao.grafico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EData;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.rednels.ofcgwt.client.model.ChartData;

/**
 * Classe que representa o modelo genérico de dados dos gráficos do sistema.
 * 
 * @param <E>
 *            classe genérica de dados.
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class AGraficoDados<E extends Dados> implements IGraficoDados<E> {

	/**
	 * Colecao de string com os dados do grafico.
	 */
	protected Collection<String[]> dados;
	/**
	 * Listagem vinculada ao grafico.
	 */
	protected IListagem<E> lista;
	/**
	 * Numero limite de registros a serem mostrados.
	 */
	protected int limite;
	/**
	 * O tipo de agrupamente de Data usado.
	 */
	protected EData parteData;
	/**
	 * O tipo de agrupamento de valor usado.
	 */
	protected EBusca grupoValor;
	/**
	 * O array de cores usados.
	 */
	protected static String[] CORES;
	/**
	 * O array de dias do mes.
	 */
	protected static String[] DIAS;
	/**
	 * O array de meses do ano.
	 */
	protected static String[] MESES;
	/**
	 * O array de anos resultante da busca.
	 */
	protected static String[] ANOS;

	static {
		// as cores padroes
		CORES = new String[] { "#0000FF", "#00FF00", "#FF0000", "#FF00FF", "#00FFFF", "#FF7700", "#000077", "#007700", "#770000", "#770077", "#007777", "#777700" };

		// preenche os dias
		DIAS = new String[31];
		for (int i = 0; i < 31; i++) {
			DIAS[i] = (i + 1) + "";
		}

		// preenche os meses
		MESES = new String[12];
		for (int i = 1; i <= 12; i++) {
			Date data = DateTimeFormat.getFormat("M/dd/yyyy").parse(i + "/01/2010");
			MESES[i - 1] = DateTimeFormat.getFormat("MMM").format(data).toUpperCase();
		}
	}

	// Gets e Seteres
	
	@Override
	public String[] getCores(int total) {
		if (total <= 12) {
			return CORES;
		} else {
			Collection<String> cores = new ArrayList<String>();
			for (String cor : CORES) {
				cores.add(cor);
			}
			for (int i = 12; i < total; i++) {
				String cor = "#";
				while (cor.length() < 7) {
					int num = (int) (Math.random() * 15);
					cor += Integer.toHexString(num);
				}
				cores.add(cor);
			}

			return cores.toArray(new String[] {});
		}
	}

	@Override
	public Number getValor(String[] valor) {
		if (grupoValor == EBusca.CONTAGEM) {
			return Double.valueOf(valor[2]);
		} else if (grupoValor == EBusca.SOMA) {
			return Double.valueOf(valor[3]);
		} else {
			return Double.valueOf(valor[4]);
		}
	}

	@Override
	public List<String> getRotulos() {
		if (parteData == EData.DIA) {
			return Arrays.asList(DIAS);
		} else if (parteData == EData.MES) {
			return Arrays.asList(MESES);
		} else {
			int min = 0;
			int max = 0;

			for (String[] valor : dados) {
				int val = Integer.valueOf(valor[1]);
				if (val < min || min == 0) {
					min = val;
				}
				if (val > max || max == 0) {
					max = val;
				}
			}

			ANOS = new String[max - min + 1];
			int base = min;
			for (; min <= max; min++) {
				ANOS[min - base] = min + "";
			}

			return Arrays.asList(ANOS);
		}
	}

	@Override
	public Collection<String[]> getDados() {
		return dados;
	}

	@Override
	public void setDados(Collection<String[]> dados) {
		this.dados = dados;
	}

	@Override
	public IListagem<E> getLista() {
		return lista;
	}

	@Override
	public void setLista(IListagem<E> lista) {
		this.lista = lista;
	}

	@Override
	public EData getParteData() {
		return parteData;
	}

	@Override
	public void setParteData(EData parteData) {
		this.parteData = parteData;
	}

	@Override
	public EBusca getGrupoValor() {
		return grupoValor;
	}

	@Override
	public void setGrupoValor(EBusca grupoValor) {
		this.grupoValor = grupoValor;
	}

	public static String[] getDIAS() {
		return DIAS;
	}

	public static void setDIAS(String[] dIAS) {
		DIAS = dIAS;
	}

	public static String[] getMESES() {
		return MESES;
	}

	public static void setMESES(String[] mESES) {
		MESES = mESES;
	}

	public static String[] getANOS() {
		return ANOS;
	}

	public static void setANOS(String[] aNOS) {
		ANOS = aNOS;
	}

	public static String[] getCORES() {
		return CORES;
	}

	public static void setCORES(String[] cORES) {
		CORES = cORES;
	}

	@Override
	public int getLimite() {
		return limite;
	}

	@Override
	public void setLimite(int limite) {
		this.limite = limite;
	}

	@Override
	public abstract void setData(ChartData data);
}
