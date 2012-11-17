package br.com.opensig.comercial.shared.rest;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe modelo que representa o cliente no sistema OpenPDV.
 *
 * @author Pedro H. Lira
 */
@XmlRootElement
public class SisCliente extends Dados implements Serializable {

    private Integer sisClienteId;
    private String sisClienteDoc;
    private String sisClienteNome;
    private String sisClienteEndereco;
    private Date sisClienteCadastrado;
    
    /**
     * Construtor padrao
     */
    public SisCliente() {
        this(0);
    }

    /**
     * Construtor padrao passando o id
     *
     * @param sisClienteId o id do cliente
     */
    public SisCliente(Integer sisClienteId) {
        super("", "", "");
        this.sisClienteId = sisClienteId;
    }
	@Override
	public Number getId() {
		return this.sisClienteId;
	}

	@Override
	public void setId(Number id) {
		this.sisClienteId = id.intValue();
	}

	public Integer getSisClienteId() {
		return sisClienteId;
	}

	public void setSisClienteId(Integer sisClienteId) {
		this.sisClienteId = sisClienteId;
	}

	public String getSisClienteDoc() {
		return sisClienteDoc;
	}

	public void setSisClienteDoc(String sisClienteDoc) {
		this.sisClienteDoc = sisClienteDoc;
	}

	public String getSisClienteNome() {
		return sisClienteNome;
	}

	public void setSisClienteNome(String sisClienteNome) {
		this.sisClienteNome = sisClienteNome;
	}

	public String getSisClienteEndereco() {
		return sisClienteEndereco;
	}

	public void setSisClienteEndereco(String sisClienteEndereco) {
		this.sisClienteEndereco = sisClienteEndereco;
	}

	public Date getSisClienteCadastrado() {
		return sisClienteCadastrado;
	}

	public void setSisClienteCadastrado(Date sisClienteCadastrado) {
		this.sisClienteCadastrado = sisClienteCadastrado;
	}

	@Override
	public String[] toArray() {
		return null;
	}

}
