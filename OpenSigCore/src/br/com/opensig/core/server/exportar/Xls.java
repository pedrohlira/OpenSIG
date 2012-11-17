package br.com.opensig.core.server.exportar;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

/**
 * Classe que define a exportacao de arquivo no formato de XLS.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Xls<E extends Dados> extends AExportacao<E> {

	private int fim;
	private HSSFWorkbook wb;
	private CreationHelper ch;
	private CellStyle cssCabecalho;
	private CellStyle cssRodape;
	private CellStyle cssTexto;
	private CellStyle cssNumero;
	private CellStyle cssInteiro;
	private CellStyle cssData;

	/**
	 * Construtor padrao.
	 */
	public Xls() {
		wb = new HSSFWorkbook();
		ch = wb.getCreationHelper();

		// estilo
		Font font1 = wb.createFont();
		font1.setFontName("Arial");
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);

		cssCabecalho = wb.createCellStyle();
		cssCabecalho.setAlignment(CellStyle.ALIGN_CENTER);
		cssCabecalho.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cssCabecalho.setFont(font1);

		cssRodape = wb.createCellStyle();
		cssRodape.setAlignment(CellStyle.ALIGN_CENTER);
		cssRodape.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cssRodape.setFont(font1);
		cssRodape.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));

		Font font2 = wb.createFont();
		font2.setFontName("Arial");
		cssTexto = wb.createCellStyle();
		cssTexto.setFont(font2);

		cssNumero = wb.createCellStyle();
		cssNumero.setDataFormat(ch.createDataFormat().getFormat("#,##0.00"));

		cssInteiro = wb.createCellStyle();
		cssInteiro.setDataFormat(ch.createDataFormat().getFormat("0"));

		cssData = wb.createCellStyle();
		cssData.setDataFormat(ch.createDataFormat().getFormat("dd/mm/yyyy"));
	}

	@Override
	public byte[] getArquivo(CoreService<E> service, SisExpImp modo, ExpListagem<E> exp, String[][] enderecos, String[][] contatos) {
		// seleciona os dados
		try {
			this.lista = service.selecionar(exp.getClasse(), modo.getInicio(), modo.getLimite(), exp.getFiltro(), true);
		} catch (CoreException e) {
			return null;
		} finally {
			this.modo = modo;
			this.expLista = exp;
		}

		// definindo o final
		if (modo.getLimite() == 0) {
			fim = lista.getDados().length;
		} else if (lista.getTotal() - modo.getInicio() < modo.getLimite()) {
			fim = lista.getTotal() - modo.getInicio();
		} else {
			fim = modo.getLimite();
		}

		// inicio do arquivo
		HSSFSheet sheet = wb.createSheet(exp.getNome());
		// cabecalho
		getCabecalho(sheet, exp.getMetadados());
		// registro
		getCorpo(sheet, exp.getMetadados(), service);
		// rodape
		getRodape(sheet, exp.getMetadados());

		// retorno
		return wb.getBytes();
	}

	@Override
	public byte[] getArquivo(CoreService<E> service, SisExpImp modo, ExpRegistro<E> exp, String[][] enderecos, String[][] contatos) {
		// seleciona os dados
		try {
			this.lista = service.selecionar(exp.getClasse(), modo.getInicio(), modo.getLimite(), exp.getFiltro(), true);
		} catch (CoreException e) {
			return null;
		} finally {
			this.modo = modo;
			this.expReg = exp;
		}

		// definindo o final
		if (modo.getLimite() == 0) {
			fim = lista.getDados().length;
		} else if (lista.getTotal() - modo.getInicio() < modo.getLimite()) {
			fim = lista.getTotal() - modo.getInicio();
		} else {
			fim = modo.getLimite();
		}

		// inicio do arquivo
		HSSFSheet sheet = wb.createSheet(exp.getNome());
		// cabecalho
		getCabecalho(sheet, exp.getMetadados());
		// registro
		getCorpo(sheet, exp.getMetadados(), service);
		// rodape
		getRodape(sheet, exp.getMetadados());

		// retorno
		return wb.getBytes();
	}

	/**
	 * Metodo que gera o cabecalho do registro.
	 * 
	 * @param sheet
	 *            o objeto de planilha.
	 * @param metadados
	 *            listage de metadados.
	 */
	public void getCabecalho(HSSFSheet sheet, List<ExpMeta> metadados) {
		Row lin = sheet.createRow(0);
		lin.setHeightInPoints(30);

		int colunas = 0;
		for (ExpMeta meta : metadados) {
			if (meta != null) {
				Cell col = lin.createCell(colunas);
				col.setCellStyle(cssCabecalho);
				col.setCellValue(meta.getRotulo());
				sheet.autoSizeColumn(colunas);
				colunas++;
			}
		}
	}

	/**
	 * Metodo que gera o corpo do registro.
	 * 
	 * @param sheet
	 *            o objeto de planilha.
	 * @param metadados
	 *            listage de metadados.
	 * @param service
	 *            o servico de busca de dados.
	 */
	public void getCorpo(HSSFSheet sheet, List<ExpMeta> metadados, CoreService service) {
		for (int i = 0; i < fim; i++) {
			Row lin = sheet.createRow(i + 1);
			int colunas = 0;
			int j = 0;

			for (ExpMeta meta : metadados) {
				if (meta != null) {
					Cell col = lin.createCell(colunas);
					setValor(lista.getDados()[i][j], col);
					colunas++;
				}
				j++;
			}

			// sub-lista de cada registro
			if (expReg != null && expReg.getExpLista() != null) {
				for (ExpListagem expLista : expReg.getExpLista()) {
					try {
						FiltroObjeto filtro = (FiltroObjeto) expLista.getFiltro();
						filtro.getValor().setId(Integer.valueOf(lista.getDados()[i][0]));
						Lista reg = service.selecionar(expLista.getClasse(), 0, 0, filtro, true);
						// inicio do planilha
						HSSFSheet folha = wb.createSheet(expLista.getNome() + (i + 1));
						// cabecalho
						getCabecalho(folha, expLista.getMetadados());
						// corpo
						getCorpoSub(folha, expLista.getMetadados(), reg.getDados());
						// rodape
						getRodape(folha, expLista.getMetadados());
					} catch (CoreException e) {
						// sem folhas
					}
				}
			}
		}
	}

	/**
	 * Metodo que gera o corpo do registro.
	 * 
	 * @param sheet
	 *            o objeto de planilha.
	 * @param metadados
	 *            listage de metadados.
	 * @param dados
	 *            os dados da sublistagem.
	 */
	public void getCorpoSub(HSSFSheet sheet, List<ExpMeta> metadados, String[][] dados) {
		for (int i = 0; i < dados.length; i++) {
			Row lin = sheet.createRow(i + 1);
			int colunas = 0;
			int j = 0;

			for (ExpMeta meta : metadados) {
				if (meta != null) {
					Cell col = lin.createCell(colunas);
					setValor(dados[i][j], col);
					colunas++;
				}
				j++;
			}
		}
	}

	/**
	 * Metodo que gera o rodape da listagem.
	 * 
	 * @param sheet
	 *            o objeto de planilha.
	 * @param metadados
	 *            listage de metadados.
	 */
	public void getRodape(HSSFSheet sheet, List<ExpMeta> metadados) {
		Row lin = sheet.createRow(fim + 1);
		lin.setHeightInPoints(30);
		int colunas = 0;

		for (ExpMeta meta : metadados) {
			if (meta != null) {
				if (meta.getGrupo() != null) {
					Cell col = lin.createCell(colunas);
					col.setCellStyle(cssRodape);
					char letra = (char) (65 + colunas);
					col.setCellFormula(meta.getGrupo().toString() + "(" + letra + "2:" + letra + (fim + 1) + ")");
				}
				colunas++;
			}
		}
	}

	/**
	 * @see AExportacao#getValor(String)
	 */
	public void setValor(String valor, Cell col) {
		valor = super.getValor(valor);
		// valida se e data
		Pattern data = Pattern.compile("^[0-9]{2}/[0-9]{2}/[0-9]{4}$");
		Matcher mat = data.matcher(valor);
		if (mat.find()) {
			try {
				col.setCellStyle(cssData);
				col.setCellValue(new SimpleDateFormat("dd/MM/yyyy", UtilServer.LOCAL).parse(valor));
			} catch (Exception e) {
				col.setCellValue(valor);
			}
		} else {
			// valida se é decimal
			Pattern decimal = Pattern.compile("^[0-9]+(\\.[0-9]{3})*\\,[0-9]{2}$");
			mat = decimal.matcher(valor);
			if (mat.find()) {
				valor = valor.replace(".", "").replace(",", ".");
				col.setCellStyle(cssNumero);
				col.setCellValue(Double.parseDouble(valor));
			} else {
				// valida se é numero
				Pattern numero = Pattern.compile("^[0-9]+$");
				mat = numero.matcher(valor);
				if (mat.find()) {
					col.setCellStyle(cssInteiro);
					col.setCellValue(Double.parseDouble(valor));
				} else { // texto e outros
					col.setCellStyle(cssTexto);
					col.setCellValue(valor);
				}
			}
		}
	}
}
