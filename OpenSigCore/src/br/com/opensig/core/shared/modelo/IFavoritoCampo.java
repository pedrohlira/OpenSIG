package br.com.opensig.core.shared.modelo;

/**
 * Interface que generaliza os campos do favorito de listagens.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IFavoritoCampo {

	// Gets e Seteres
	
	public String getSisFavoritoCampoFiltro1Compara();

	public String getSisFavoritoCampoFiltro1Valor();

	public String getSisFavoritoCampoFiltro2Compara();

	public String getSisFavoritoCampoFiltro2Valor();

	public int getSisFavoritoCampoId();

	public String getSisFavoritoCampoNome();

	public String getSisFavoritoCampoTipo();

	public boolean getSisFavoritoCampoVisivel();
}
