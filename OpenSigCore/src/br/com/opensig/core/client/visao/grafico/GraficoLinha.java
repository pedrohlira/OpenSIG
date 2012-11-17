package br.com.opensig.core.client.visao.grafico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EData;

import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.LineChart;

/**
 * Classe que inicializa o grafico de Linha.
 * 
 * @param <E>
 *            classe genérica do gráfico.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class GraficoLinha<E extends Dados> extends AGraficoDados<E> {

	private String campoX;
	private String campoSubX;
	private String campoY;
	private double min;
	private double max;

	public GraficoLinha(String campoX, String campoSubX, String campoY, EData parteData, EBusca grupoValor) {
		this.campoX = campoX;
		this.campoSubX = campoSubX;
		this.campoY = campoY;
		this.parteData = parteData;
		this.grupoValor = grupoValor;
	}

	public void setData(ChartData data) {
		List<String> rotulos;
		List<Number> valores;
		min = 0.00;
		max = 1.00;
		int itens = 0;
		limite = limite > 0 && limite < dados.size() ? limite : dados.size();

		if (campoSubX == null) {
			if (lista.validarCampoData(campoX)) {
				rotulos = getRotulos();
				setValores(data, rotulos.size(), campoY);
			} else {
				rotulos = new ArrayList<String>();
				valores = new ArrayList<Number>();

				for (String[] valor : dados) {
					if (itens++ < limite) {
						double val = getValor(valor).doubleValue();
						rotulos.add(valor[0]);
						valores.add(val);
						if (val < min) {
							min = val;
						}
						if (val > max) {
							max = val;
						}
					}
				}
				data.addElements(getLinha(campoY, getCores(0)[0], valores));
			}
		} else {
			rotulos = getRotulos();
			setValores(data, rotulos.size(), "");
		}

		YAxis ya = new YAxis();
		ya.setMin(min);
		ya.setSteps((int) (max / limite));
		ya.setMax(max * 1.1);
		data.setYAxis(ya);

		XAxis xa = new XAxis();
		xa.setLabels(rotulos);
		xa.setSteps(1);
		
		try {
			xa.setMin(Integer.valueOf(rotulos.get(0)));
		} catch (Exception ex) {
			xa.setMin(0);
		}
		try {
			xa.setMax(Integer.valueOf(rotulos.get(rotulos.size() - 1)));
		} catch (Exception ex) {
			xa.setMax(rotulos.size());
		}

		if (xa.getMax().intValue() == xa.getMin().intValue()) {
			xa.setMax(xa.getMax().intValue() + 1);
		}

		data.setXAxis(xa);
	}

	private void setValores(ChartData data, int tamanho, String legenda) {
		Number[] valores = new Number[tamanho];
		String reg = "";
		int cor = 0;
		int base = parteData == EData.ANO ? Integer.valueOf(ANOS[0]) - 1 : 0;
		int itens = 1;
		limite = limite > 0 && limite < dados.size() ? limite : dados.size();
		String[] cores = getCores(dados.size());

		for (String[] valor : dados) {
			if (!valor[0].equals(reg) && !reg.equals("") && !valor[0].equals(valor[1])) {
				if (itens++ < limite) {
					data.addElements(getLinha(reg, cores[cor++], Arrays.asList(valores)));
					valores = new Number[tamanho];
				} else {
					break;
				}
			}

			int pos = Integer.valueOf(valor[1]) - 1;
			double val = getValor(valor).doubleValue();
			valores[pos - base] = val;
			reg = valor[0];

			if (val < min || min == 0) {
				min = val;
			}
			if (val > max) {
				max = val;
			}
		}

		reg = legenda.equals("") ? reg : legenda;
		data.addElements(getLinha(reg, cores[cor], Arrays.asList(valores)));
	}

	private LineChart getLinha(String tex, String cor, Collection<Number> val) {
		LineChart linha = new LineChart();
		linha.setText(tex);
		linha.setTooltip("#x_label#<br>#val#");
		linha.setColour(cor);
		linha.setValues(val);
		linha.setKeyToggleOnClick(true);
		return linha;
	}

	public String getCampoX() {
		return campoX;
	}

	public void setCampoX(String campoX) {
		this.campoX = campoX;
	}

	public String getCampoSubX() {
		return campoSubX;
	}

	public void setCampoSubX(String campoSubX) {
		this.campoSubX = campoSubX;
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