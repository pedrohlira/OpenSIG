package br.com.opensig.core.client.visao.grafico;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EData;

import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.BarChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;

/**
 * Classe que inicializa o grafico de Barra.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class GraficoBarra<E extends Dados> extends AGraficoDados<E> {

	private String campoX;
	private String campoY;
	private double min;
	private double max;

	public GraficoBarra(String campoX, String campoY, EData parteData, EBusca grupoValor) {
		this.campoX = campoX;
		this.campoY = campoY;
		this.parteData = parteData;
		this.grupoValor = grupoValor;
	}

	@Override
	public void setData(ChartData data) {
		List<String> rotulos = new ArrayList<String>();
		List<Number> valores = new ArrayList<Number>();
		min = 0.00;
		max = 1.00;
		int itens = 0;
		limite = limite > 0 && limite < dados.size() ? limite : dados.size();

		for (String[] valor : dados) {
			if (itens++ < limite) {
				double val = getValor(valor).doubleValue();
				if (val < min) {
					min = val;
				}
				if (val > max) {
					max = val;
				}

				if (lista.validarCampoData(campoX) && parteData == EData.MES) {
					int pos = Integer.valueOf(valor[0]) - 1;
					rotulos.add(MESES[pos]);
				} else {
					rotulos.add(valor[0]);
				}
				valores.add(val);
			} else {
				break;
			}
		}

		XAxis xa = new XAxis();
		xa.setLabels(rotulos);
		xa.setZDepth3D(5);
		data.setXAxis(xa);

		YAxis ya = new YAxis();
		ya.setMin(min);
		ya.setSteps((int) (max / limite));
		ya.setMax(max * 1.1);
		data.setYAxis(ya);

		BarChart barra = new BarChart(BarStyle.THREED);
		barra.setText(campoY);
		barra.setTooltip("#val#");
		barra.addValues(valores);
		barra.setColours(getCores(limite));
		data.addElements(barra);
	}

	// Gets e Seteres
	
	public String getCampoX() {
		return campoX;
	}

	public void setCampoX(String campoX) {
		this.campoX = campoX;
	}

	public String getCampoY() {
		return campoY;
	}

	public void setCampoY(String campoY) {
		this.campoY = campoY;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

}