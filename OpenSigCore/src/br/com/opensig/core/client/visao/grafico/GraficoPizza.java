package br.com.opensig.core.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EData;

import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.elements.PieChart;

/**
 * Classe que inicializa o grafico de Pizza.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class GraficoPizza<E extends Dados> extends AGraficoDados<E> {

	private String campoX;

	public GraficoPizza(String campoX, EData parteData, EBusca grupoValor) {
		this.campoX = campoX;
		this.parteData = parteData;
		this.grupoValor = grupoValor;
	}

	public void setData(ChartData data) {
		// pegando o total
		double total = 0.00;
		int itens = 0;
		limite = limite > 0 && limite < dados.size() ? limite : dados.size();
		for (String[] valor : dados) {
			if (itens++ < limite) {
				total += getValor(valor).doubleValue();
			} else {
				break;
			}
		}

		// setando as propriedades
		PieChart pizza = new PieChart();
		pizza.setAlpha(0.75f);
		pizza.setTooltip("#val#<br>#percent#");
		pizza.setGradientFill(true);
		pizza.setColours(getCores(limite));
		pizza.setAnimation(new PieChart.PieBounceAnimation(25));
		
		// adicionando as fatias
		String rot;
		double val;
		double per;
		itens = 0;
		for (String[] valor : dados) {
			if (itens++ < limite) {
				if (lista.validarCampoData(campoX) && parteData == EData.MES) {
					int pos = Integer.valueOf(valor[0]) - 1;
					rot = MESES[pos];
				} else {
					rot = valor[0];
				}

				val = getValor(valor).doubleValue();
				per = val / total * 100;
				pizza.addSlices(new PieChart.Slice(val, rot + "\n" + IListagem.NDF.format(val) + " - " + IListagem.NDF.format(per) + "%"));
			} else {
				break;
			}
		}

		data.addElements(pizza);
	}

	public String getCampoX() {
		return campoX;
	}

	public void setCampoX(String campoX) {
		this.campoX = campoX;
	}

}