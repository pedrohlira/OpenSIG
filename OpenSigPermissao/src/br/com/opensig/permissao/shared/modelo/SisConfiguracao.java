package br.com.opensig.permissao.shared.modelo;

import java.io.Serializable;
import javax.persistence.*;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ELetra;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa uma configuracao no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
@Entity
@Table(name = "sis_configuracao")
public class SisConfiguracao extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_configuracao_id")
	private int sisConfiguracaoId;

	@Column(name = "sis_configuracao_chave")
	private String sisConfiguracaoChave;

	@Column(name = "sis_configuracao_descricao")
	private String sisConfiguracaoDescricao;

	@Column(name = "sis_configuracao_valor")
	private String sisConfiguracaoValor;

	@Column(name = "sis_configuracao_ativo")
	private int sisConfiguracaoAtivo;

	@Column(name = "sis_configuracao_sistema")
	private int sisConfiguracaoSistema;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpEmpresa empEmpresa;

	public SisConfiguracao() {
		this(0);
	}

	public SisConfiguracao(int sisConfiguracaoId) {
		super("pu_permissao", "SisConfiguracao", "sisConfiguracaoId", "sisConfiguracaoChave");
		this.sisConfiguracaoId = sisConfiguracaoId;
		setTipoLetra(ELetra.NORMAL);
	}

	public int getSisConfiguracaoId() {
		return this.sisConfiguracaoId;
	}

	public void setSisConfiguracaoId(int sisConfiguracaoId) {
		this.sisConfiguracaoId = sisConfiguracaoId;
	}

	public String getSisConfiguracaoChave() {
		return this.sisConfiguracaoChave;
	}

	public void setSisConfiguracaoChave(String sisConfiguracaoChave) {
		this.sisConfiguracaoChave = sisConfiguracaoChave;
	}

	public String getSisConfiguracaoDescricao() {
		return this.sisConfiguracaoDescricao;
	}

	public void setSisConfiguracaoDescricao(String sisConfiguracaoDescricao) {
		this.sisConfiguracaoDescricao = sisConfiguracaoDescricao;
	}

	public String getSisConfiguracaoValor() {
		return this.sisConfiguracaoValor;
	}

	public void setSisConfiguracaoValor(String sisConfiguracaoValor) {
		this.sisConfiguracaoValor = sisConfiguracaoValor;
	}

	public boolean getSisConfiguracaoAtivo() {
		return sisConfiguracaoAtivo == 0 ? false : true;
	}

	public void setSisConfiguracaoAtivo(boolean sisConfiguracaoAtivo) {
		this.sisConfiguracaoAtivo = sisConfiguracaoAtivo == false ? 0 : 1;
	}

	public boolean getSisConfiguracaoSistema() {
		return sisConfiguracaoSistema == 0 ? false : true;
	}

	public void setSisConfiguracaoSistema(boolean sisConfiguracaoSistema) {
		this.sisConfiguracaoSistema = sisConfiguracaoSistema == false ? 0 : 1;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Number getId() {
		return sisConfiguracaoId;
	}

	public void setId(Number id) {
		sisConfiguracaoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { sisConfiguracaoId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1() + "", sisConfiguracaoChave, sisConfiguracaoValor,
				sisConfiguracaoDescricao, getSisConfiguracaoAtivo() + "", getSisConfiguracaoSistema() + "" };
	}

	public void anularDependencia() {
		empEmpresa = null;
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else {
			return null;
		}
	}
}