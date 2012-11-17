package br.com.opensig.core.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServlet;
import javax.swing.text.MaskFormatter;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jasypt.util.text.BasicTextEncryptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;

import br.com.opensig.core.client.servico.OpenSigException;

public class UtilServer extends HttpServlet {
	// tabela com vinculos das letras
	private static char[] tabela = new char[256];
	// path local
	private static String PATH = "";

	/**
	 * Log do sistema
	 */
	public static Logger LOG;
	/**
	 * Dados de configuracao do sistema.
	 */
	private static Map<String, String> CONF = new HashMap<String, String>();
	/**
	 * Localizacao para formatacao de lingua.
	 */
	public static Locale LOCAL = Locale.getDefault();
	/**
	 * Chave mestre para criptografar
	 */
	public static String CHAVE;
	/**
	 * Path das empresas
	 */
	public static String PATH_EMPRESA;

	/**
	 * Metodo que inicializa variazeis globais
	 */
	public void init() {
		// setando a tabela de letras
		String acentuado = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
		String semAcento = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";

		for (int i = 0; i < tabela.length; ++i) {
			tabela[i] = (char) i;
		}
		for (int i = 0; i < acentuado.length(); ++i) {
			tabela[acentuado.charAt(i)] = semAcento.charAt(i);
		}

		// pegando os dados de log
		Properties log4j = new Properties();
		Enumeration<String> param = getInitParameterNames();
		for (; param.hasMoreElements();) {
			String nome = param.nextElement();
			String valor = getInitParameter(nome);
			log4j.put(nome, valor);
		}
		// configurando o LOG
		PropertyConfigurator.configure(log4j);
		LOG = Logger.getLogger(UtilServer.class);

		// setando a chave/senha
		CHAVE = getServletContext().getInitParameter("sistema.chave");

		// setando o path local
		PATH = getServletContext().getRealPath("/");

		// configurando o as opcoes do app
		if (getServletContext().getInitParameter("sistema.empresas") != null) {
			PATH_EMPRESA = getServletContext().getInitParameter("sistema.empresas");
			CONF.put("sistema.empresas", PATH_EMPRESA);
			File dir = new File(PATH_EMPRESA + "conf/");

			if (dir.exists() && dir.isDirectory()) {
				for (File conf : dir.listFiles()) {
					if (conf.isFile() && conf.getName().endsWith(".conf")) {
						Properties prop = new Properties();
						try {
							FileInputStream fis = new FileInputStream(conf);
							prop.load(fis);
							fis.close();
						} catch (Exception ex) {
							LOG.error("Nao leu os dados do arquivo -> " + conf.getName(), ex);
						} finally {
							// adicionando os valores
							for (Entry<Object, Object> entry : prop.entrySet()) {
								CONF.put(entry.getKey().toString(), entry.getValue().toString());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Retorna o conf com os dados padroes lidos.
	 * 
	 * @return um mapa de configuracoes.
	 */
	public static Map<String, String> getConf() {
		return CONF;
	}

	/**
	 * Metodo que retorna o local absoluto do arquivo.
	 * 
	 * @param relativo
	 *            o local relativo do arquivo.
	 * @return o local absoluto do arquivo.
	 */
	public static String getRealPath(String relativo) {
		return PATH + relativo;
	}

	/**
	 * Metodo que normaliza os caracteres removendo os acentos.
	 * 
	 * @param texto
	 *            o texto acentuado.
	 * @return o texto sem acentos.
	 */
	public static String normaliza(String texto) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < texto.length(); ++i) {
			char ch = texto.charAt(i);
			if (ch < 256) {
				sb.append(tabela[ch]);
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * Metodo que transforma os bytes de um html em pdf.
	 * 
	 * @param obj
	 *            html em bytes.
	 * @param formato
	 *            o tipo de apresentacao.
	 * @return bytes do pdf.
	 */
	public static byte[] getPDF(byte[] obj, String formato) {
		// define as variaveis
		String nome = new Date().getTime() + "";
		String comando = PATH_EMPRESA + "htmltopdf.sh";
		String pathHtml = PATH_EMPRESA + nome + ".html";
		String pathPdf = PATH_EMPRESA + nome + ".pdf";

		// salva o html em arquivo
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(pathHtml));
			bw.write(new String(obj));
			bw.close();
		} catch (Exception ex) {
			UtilServer.LOG.error("Nao salvaou o html.", ex);
			obj = null;
		}

		// gera o pdf usando o wkhtmltopdf
		try {
			ProcessBuilder pb = new ProcessBuilder(comando, formato, nome); 
			pb.redirectErrorStream(true); 
			Process process = pb.start(); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line = reader.readLine()) != null) 
			{ 
			    System.out.println(line); 
			} 
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro na geracao do pdf.", ex);
			obj = null;
		}

		// le o arquivo pdf em arquivo
		try {
			File f = new File(pathPdf);
			obj = new byte[(int) f.length()];
			InputStream inStream = new FileInputStream(pathPdf);
			inStream.read(obj);
			inStream.close();
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro na leitura do pdf.", ex);
			obj = null;
		}

		// delete os arquivos temporarios
		new File(pathHtml).delete();
		new File(pathPdf).delete();

		return obj;
	}

	/**
	 * Metodo que compacta os arquivos passado em forma de bytes com seus nomes.
	 * 
	 * @param arquivos
	 *            um mapa de nomes e bytes dos arquivos a serem compactados.
	 * @return um array de bytes que representa um arquivo compactado.
	 */
	public static byte[] getZIP(Map<String, byte[]> arquivos) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zout = new ZipOutputStream(baos);

		try {
			for (Entry<String, byte[]> arquivo : arquivos.entrySet()) {
				ZipEntry e = new ZipEntry(arquivo.getKey());
				zout.putNextEntry(e);
				zout.write(arquivo.getValue());
				zout.closeEntry();
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Nao recuperou o zip.", ex);
			return null;
		} finally {
			try {
				zout.close();
			} catch (IOException e) {
				// nada
			}
		}

		return baos.toByteArray();
	}

	/**
	 * Metodo que descompacta os arquivos dentro de um zip.
	 * 
	 * @param zip
	 *            arquivo compactado contendo os arquivos.
	 * @return um mapa de nomes e bytes dos arquivos descompactados.
	 */
	public static SortedMap<String, byte[]> getArquivos(byte[] zip) {
		SortedMap<String, byte[]> arquivos = new TreeMap<String, byte[]>();
		ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(zip));

		try {
			ZipEntry arquivo;
			while ((arquivo = zin.getNextEntry()) != null) {
				if (!arquivo.isDirectory()) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					for (int c = zin.read(); c != -1; c = zin.read()) {
						baos.write(c);
					}
					arquivos.put(arquivo.getName(), baos.toByteArray());
				}
				zin.closeEntry();
			}
			zin.close();
		} catch (Exception ex) {
			UtilServer.LOG.error("Nao pegou os arquivos", ex);
			return null;
		}

		return arquivos;
	}

	/**
	 * Metodo que transforma em string um documento xml.
	 * 
	 * @param node
	 *            o documento xml tipo DOM.
	 * @return uma String do xml.
	 */
	public static String getXml(Node node) {
		UtilServer.LOG.debug("Doc enviado: ");
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			trans.transform(new DOMSource(node), new StreamResult(os));
			return os.toString();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao formatar em string o doc.", e);
			return null;
		}
	}

	/**
	 * Metodo que transforma uma string em documento xml.
	 * 
	 * @param xml
	 *            na forma de texto.
	 * @return um DOM do xml.
	 */
	public static Document getXml(String xml) {
		return getXml(xml, null, null);
	}

	/**
	 * Metodo que transforma uma string em documento xml.
	 * 
	 * @param xml
	 *            na forma de texto.
	 * @param xsd
	 *            caminho real do arquivo xsd.
	 * @param error
	 *            interceptador de erros do parse.
	 * @return um DOM do xml.
	 */
	public static Document getXml(String xml, String xsd, ErrorHandler error) {
		UtilServer.LOG.debug("Xml enviado: " + xml);
		try {
			// gera um objeto DOM do xml
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			if (xsd != null) {
				dbf.setValidating(true);
				dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", XMLConstants.W3C_XML_SCHEMA_NS_URI);
				dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsd);
			}
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			docBuilder.setErrorHandler(error);
			return docBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao formatar a string em xml.", e);
			return null;
		}
	}

	/**
	 * Metodo que faz o cálculo de modulo 11.
	 * 
	 * @param fonte
	 *            o numero a ser usado para calculo.
	 * @param dig
	 *            quantos digitos de retorno, 1 ou 2.
	 * @param limite
	 *            quantos digitos usados para multiplicacao.
	 * @return o dv do fonte.
	 */
	public static String modulo11(String fonte, int dig, int limite) {
		for (int n = 1; n <= dig; n++) {
			int soma = 0;
			int mult = 2;

			for (int i = fonte.length() - 1; i >= 0; i--) {
				soma += (mult * Integer.valueOf(fonte.substring(i, i + 1)));
				if (++mult > limite) {
					mult = 2;
				}
			}
			fonte += ((soma * 10) % 11) % 10;
		}
		return fonte.substring(fonte.length() - dig);
	}

	/**
	 * @see #formataNumero(double, int, int, boolean)
	 */
	public static String formataNumero(String valor, int inteiros, int decimal, boolean grupo) {
		return formataNumero(Double.valueOf(valor), inteiros, decimal, grupo);
	}

	/**
	 * Metodo que faz a formatacao de numeros com inteiros e fracoes
	 * 
	 * @param valor
	 *            o valor a ser formatado
	 * @param inteiros
	 *            o minimo de inteiros, que serao completados com ZEROS se preciso
	 * @param decimal
	 *            o minimo de decimais, que serao completados com ZEROS se preciso
	 * @param grupo
	 *            se sera colocado separador de grupo de milhar
	 * @return uma String com o numero formatado
	 */
	public static String formataNumero(double valor, int inteiros, int decimal, boolean grupo) {
		NumberFormat nf = NumberFormat.getIntegerInstance(LOCAL);
		nf.setMinimumIntegerDigits(inteiros);
		nf.setMinimumFractionDigits(decimal);
		nf.setMaximumFractionDigits(decimal);
		nf.setGroupingUsed(grupo);
		return nf.format(valor);
	}

	/**
	 * Metodo que formata a data.
	 * 
	 * @param data
	 *            a data do tipo Date.
	 * @param formato
	 *            o formado desejado.
	 * @return a data formatada como solicidato.
	 */
	public static String formataData(Date data, String formato) {
		if (data == null) {
			return "";
		} else {
			return new SimpleDateFormat(formato, LOCAL).format(data);
		}
	}

	/**
	 * Metodo que formata a data.
	 * 
	 * @param data
	 *            a data do tipo Date.
	 * @param formato
	 *            o formado desejado usando algum padrao local.
	 * @return a data formatada como solicidato.
	 */
	public static String formataData(Date data, int formato) {
		if (data == null) {
			return "";
		} else {
			return DateFormat.getDateInstance(formato, LOCAL).format(data);
		}
	}

	/**
	 * Metodo que formata a data.
	 * 
	 * @param data
	 *            a data em formato string.
	 * @param formato
	 *            o formado desejado.
	 *            <p/>
	 * @return a data como objeto ou null se tiver erro.
	 */
	public static Date formataData(String data, String formato) {
		try {
			return new SimpleDateFormat(formato).parse(data);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Metodo que formata a hora.
	 * 
	 * @param data
	 *            a data do tipo Date.
	 * @param formato
	 *            o formado desejado.
	 * @return a hora formatada como solicidato.
	 */
	public static String formataHora(Date data, String formato) {
		if (data == null) {
			return "";
		} else {
			return new SimpleDateFormat(formato, LOCAL).format(data);
		}
	}

	/**
	 * Metodo que formata a hora.
	 * 
	 * @param data
	 *            a data do tipo Date.
	 * @param formato
	 *            o formado desejado usando algum padrao local.
	 * @return a hora formatada como solicidato.
	 */
	public static String formataHora(Date data, int formato) {
		if (data == null) {
			return "";
		} else {
			return DateFormat.getTimeInstance(formato, LOCAL).format(data);
		}
	}

	/**
	 * Metodo que formata o texto.
	 * 
	 * @param texto
	 *            o texto a ser formatado.
	 * @param caracter
	 *            o caracter que sera repetido.
	 * @param tamanho
	 *            o tamanho total do texto de resposta.
	 * @param direita
	 *            a direcao onde colocar os caracteres.
	 * @return o texto formatado.
	 */
	public static String formataTexto(String texto, String caracter, int tamanho, boolean direita) {
		StringBuffer sb = new StringBuffer();
		int fim = tamanho - texto.length();
		for (int i = 0; i < fim; i++) {
			sb.append(caracter);
		}
		return direita ? texto + sb.toString() : sb.toString() + texto;
	}

	/**
	 * Metodo que formata o texto usando a mascara passada.
	 * 
	 * @param texto
	 *            o texto a ser formatado.
	 * @param mascara
	 *            a mascara a ser usada.
	 * @return o texto formatado.
	 * @throws ParseException
	 *             caso ocorra erro.
	 */
	public static String formataTexto(String texto, String mascara) throws ParseException {
		MaskFormatter mf = new MaskFormatter(mascara);
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(texto);
	}

	/**
	 * Metodo que retorna o valor de uma tag dentro do xml.
	 * 
	 * @param ele
	 *            elemento xml em forma de objeto.
	 * @param tag
	 *            nome da tag que deseja recuperar o valor.
	 * @param excecao
	 *            se passado true dispara a exception se ocorrer erro, se false retorna null
	 * @return valor da tag encontrada ou NULL se nao achada.
	 * @exception NullPointerException
	 *                exceção disparada em caso de erro.
	 */
	public static String getValorTag(Element ele, String tag, boolean excecao) throws NullPointerException {
		try {
			return ele.getElementsByTagName(tag).item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
			if (excecao) {
				UtilServer.LOG.debug("Nao achou a tag -> " + tag);
				throw new NullPointerException(UtilServer.CONF.get("errInvalido") + " - > " + tag);
			}
			return null;
		}
	}

	/**
	 * Metodo que retorna o conteudo de um arquivo de conteudo textual.
	 * 
	 * @param pathArquivo
	 *            local completo fisico do arquivo.
	 * @return o texto interno do arquivo.
	 * @throws OpenSigException
	 *             exceção disparada em caso de erro.
	 */
	public static String getTextoArquivo(String pathArquivo) throws OpenSigException {
		try {
			BufferedReader in = new BufferedReader(new FileReader(pathArquivo));
			StringBuffer sb = new StringBuffer();
			while (in.ready()) {
				sb.append(in.readLine());
			}
			in.close();
			return sb.toString();
		} catch (IOException e) {
			throw new OpenSigException(e.getMessage());
		}
	}

	/**
	 * Metodo que Converte XML em Objeto.
	 * 
	 * @param xml
	 *            o arquivo em formato string.
	 * @param pacote
	 *            o nome do pacote da classe especifica.
	 * @return T o tipo passado de Objeto.
	 * @exception JAXBException
	 *                dispara uma excecao caso ocorra erro.
	 * 
	 */
	public static <T> T xmlToObj(String xml, String pacote) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(pacote);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		JAXBElement<T> element = (JAXBElement<T>) unmarshaller.unmarshal(new StringReader(xml));
		return element.getValue();
	}

	/**
	 * Metodo que converte Objeto em XML.
	 * 
	 * @param <T>
	 *            o tipo passado de Objeto.
	 * @param element
	 *            o Objeto passado.
	 * @param pacote
	 *            o nome do pacote da classe especifica.
	 * @return o arquivo em formato String.
	 * @throws JAXBException
	 *             dispara uma excecao caso ocorra erro.
	 */
	public static <T> String objToXml(JAXBElement<T> element, String pacote) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(pacote);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		StringWriter sw = new StringWriter();
		marshaller.marshal(element, sw);

		// retira ns indesejado
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + sw.toString();
		xml = xml.replace("ns2:", "");
		xml = xml.replace(":ns2", "");
		xml = xml.replace(" xmlns=\"http://www.w3.org/2000/09/xmldsig#\"", "");
		// retira as quebras de linhas
		xml = xml.replace("\r", "");
		xml = xml.replace("\n", "");
		// retira acentos
		xml = UtilServer.normaliza(xml);
		// remove alguns caracteres especiais
		xml = xml.replaceAll(UtilServer.CONF.get("nfe.regexp"), "");
		return xml;
	}

	/**
	 * Metodo que informa se o metodo da classe é do tipo GET.
	 * 
	 * @param method
	 *            usando reflection para descrobrir os metodos.
	 * @return verdadeiro se o metodo for GET, falso caso contrario.
	 */
	public static boolean isGetter(Method method) {
		if (!method.getName().startsWith("get")) {
			return false;
		}
		if (method.getParameterTypes().length != 0) {
			return false;
		}
		if (void.class.equals(method.getReturnType())) {
			return false;
		}
		return true;
	}

	/**
	 * Metodo que informa se o metodo da classe é do tipo SET.
	 * 
	 * @param method
	 *            usando reflection para descrobrir os metodos.
	 * @return verdadeiro se o metodo for SET, falso caso contrario.
	 */
	public static boolean isSetter(Method method) {
		if (!method.getName().startsWith("set")) {
			return false;
		}
		if (method.getParameterTypes().length == 0) {
			return false;
		}
		if (!void.class.equals(method.getReturnType())) {
			return false;
		}
		return true;
	}

	/**
	 * Metodo que criptografa um texto passado usando a chave privada.
	 * 
	 * @param texto
	 *            valor a ser criptografado.
	 * @return o texto informado criptografado.
	 */
	public static String encriptar(String texto) {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(ChavePrivada.VALOR);
		return encryptor.encrypt(texto);
	}

	/**
	 * Metodo que descriptografa um texto passado usando a chave privada.
	 * 
	 * @param texto
	 *            valor a ser descriptografado.
	 * @return o texto informado descriptografado.
	 */
	public static String descriptar(String texto) {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(ChavePrivada.VALOR);
		return encryptor.decrypt(texto);
	}
}
