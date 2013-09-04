package br.com.opensig.core.server.exportar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

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
 * Classe que define a exportacao de arquivo no formato de XLSX.
 * 
 * @author Pedro H. Lira
 */
public class Xlsx<E extends Dados> extends AExportacao<E> {

	private SXSSFWorkbook wb;
	private CreationHelper ch;
	private CellStyle cssCabecalho;
	private CellStyle cssRodape;
	private CellStyle cssTexto;
	private CellStyle cssNumero;
	private CellStyle cssInteiro;
	private CellStyle cssData;
	private CellStyle cssDataHora;

	/**
	 * Construtor padrao.
	 */
	public Xlsx() {
		wb = new SXSSFWorkbook(PAGINACAO);
		wb.setCompressTempFiles(true);
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

		cssDataHora = wb.createCellStyle();
		cssDataHora.setDataFormat(ch.createDataFormat().getFormat("dd/mm/yyyy hh:MM:ss"));
	}

	@Override
	public String getArquivo(CoreService<E> service, SisExpImp modo, ExpListagem<E> exp, String[][] enderecos, String[][] contatos) {
		this.expLista = exp;
		String path = UtilServer.PATH_EMPRESA + "tmp/" + new Date().getTime() + ".xlsx";

		try {
			// inicio do arquivo
			Sheet sheet = wb.createSheet(exp.getNome());
			// cabecalho
			getCabecalho(sheet, exp.getMetadados());
			// seleciona os dados
			int inicio = modo.getInicio();
			int limite = modo.getLimite() == 0 || modo.getLimite() > PAGINACAO ? PAGINACAO : modo.getLimite();
			int fim = 0;
			do {
				lista = service.selecionar(exp.getClasse(), inicio, limite, exp.getFiltro(), true);
				// determina o fim do recorte
				if (lista.getTotal() - inicio < limite) {
					fim = lista.getTotal() - inicio;
				} else {
					fim = limite;
				}
				getCorpo(sheet, exp.getMetadados(), service, inicio, fim);
				inicio += limite;
			} while (fim == PAGINACAO && (modo.getLimite() == 0 || modo.getLimite() > PAGINACAO));
			// rodape
			getRodape(sheet, exp.getMetadados(), lista.getTotal());

			// gera o arquivo
			FileOutputStream fos = new FileOutputStream(path);
			wb.write(fos);
			fos.close();
			wb.dispose();
			return path;
		} catch (IOException ex) {
			return null;
		} catch (CoreException e) {
			return null;
		}
	}

	@Override
	public String getArquivo(CoreService<E> service, SisExpImp modo, ExpRegistro<E> exp, String[][] enderecos, String[][] contatos) {
		this.expReg = exp;
		String path = UtilServer.PATH_EMPRESA + "tmp/" + new Date().getTime() + ".xlsx";

		try {
			// inicio do arquivo
			Sheet sheet = wb.createSheet(exp.getNome());
			// cabecalho
			getCabecalho(sheet, exp.getMetadados());
			// seleciona os dados
			int inicio = modo.getInicio();
			int limite = modo.getLimite() == 0 || modo.getLimite() > PAGINACAO ? PAGINACAO : modo.getLimite();
			int fim = 0;
			do {
				lista = service.selecionar(exp.getClasse(), inicio, limite, exp.getFiltro(), true);
				// determina o fim do recorte
				if (lista.getTotal() - inicio < limite) {
					fim = lista.getTotal() - inicio;
				} else {
					fim = limite;
				}
				getCorpo(sheet, exp.getMetadados(), service, inicio, fim);
				inicio += limite;
			} while (fim == PAGINACAO && (modo.getLimite() == 0 || modo.getLimite() > PAGINACAO));
			// rodape
			getRodape(sheet, exp.getMetadados(), lista.getTotal());

			// gera o arquivo
			FileOutputStream fos = new FileOutputStream(path);
			wb.write(fos);
			fos.close();
			wb.dispose();
			return path;
		} catch (IOException e) {
			return null;
		} catch (CoreException e) {
			return null;
		}
	}

	/**
	 * Metodo que gera o cabecalho do registro.
	 * 
	 * @param sheet
	 *            o objeto de planilha.
	 * @param metadados
	 *            listage de metadados.
	 */
	public void getCabecalho(Sheet sheet, List<ExpMeta> metadados) {
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
	 * @param inicio
	 *            o numero inicial de registros.
	 * @param fim
	 *            o numero final de registros.
	 */
	public void getCorpo(Sheet sheet, List<ExpMeta> metadados, CoreService service, int inicio, int fim) {
		for (int i = 0; i < fim; i++) {
			Row lin = sheet.createRow(inicio + i + 1);
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
						Sheet folha = wb.createSheet(expLista.getNome() + (inicio + i + 1));
						// cabecalho
						getCabecalho(folha, expLista.getMetadados());
						// corpo
						getCorpoSub(folha, expLista.getMetadados(), reg.getDados());
						// rodape
						getRodape(folha, expLista.getMetadados(), reg.getTotal());
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
	public void getCorpoSub(Sheet sheet, List<ExpMeta> metadados, String[][] dados) {
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
	 * @param fim
	 *            o final das linhas.
	 */
	public void getRodape(Sheet sheet, List<ExpMeta> metadados, int fim) {
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
		// valida se e data/hora
		Pattern padrao = Pattern.compile("^([0-9]{2}/){2}[0-9]{4} ([0-9]{2}:){2}[0-9]{2}$");
		Matcher mat = padrao.matcher(valor);
		if (mat.find()) {
			try {
				col.setCellStyle(cssDataHora);
				col.setCellValue(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", UtilServer.LOCAL).parse(valor));
			} catch (Exception e) {
				col.setCellValue(valor);
			}
		} else {
			// valida se e data
			padrao = Pattern.compile("^([0-9]{2}/){2}[0-9]{4}$");
			mat = padrao.matcher(valor);
			if (mat.find()) {
				try {
					col.setCellStyle(cssData);
					col.setCellValue(new SimpleDateFormat("dd/MM/yyyy", UtilServer.LOCAL).parse(valor));
				} catch (Exception e) {
					col.setCellValue(valor);
				}
			} else {
				// valida se é decimal
				padrao = Pattern.compile("^[0-9]+(\\.[0-9]{3})*\\,[0-9]{2}$");
				mat = padrao.matcher(valor);
				if (mat.find()) {
					valor = valor.replace(".", "").replace(",", ".");
					col.setCellStyle(cssNumero);
					col.setCellValue(Double.parseDouble(valor));
				} else {
					// valida se é numero
					padrao = Pattern.compile("^[0-9]+$");
					mat = padrao.matcher(valor);
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
}
