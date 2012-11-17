package br.com.opensig.core.shared.modelo;

import java.util.List;

import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

/**
 * Interface que generaliza o favorito de listagens.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IFavorito {

	// Gets e Seteres
	
    public String getSisFavoritoBusca();

    public String getSisFavoritoDescricao();

    public int getSisFavoritoId();

    public String getSisFavoritoNome();

    public String getSisFavoritoOrdem();

    public String getSisFavoritoOrdemDirecao();

    public int getSisFavoritoPaginacao();
    
    public SisFuncao getSisFuncao();

    public List<IFavoritoCampo> getSisFavoritoCampos();
    
    public List<IFavoritoGrafico> getSisFavoritoGrafico();
}
