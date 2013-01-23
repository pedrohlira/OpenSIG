# Atualizacoes da versao 1.0.13

# Arrumando a classe que executa o bloco
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoD.RegistroD500' WHERE `fis_sped_bloco_id`='106';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoD.RegistroD590' WHERE `fis_sped_bloco_id`='109';

# Arrumando a funcao que contem a exportacao/importacao
UPDATE `sis_exp_imp` SET `sis_exp_imp_funcao`='br.com.opensig.fiscal.client.controlador.comando.ComandoSped' WHERE `sis_exp_imp_funcao`='br.com.opensig.fiscal.client.controlador.comando.ComandoSpedFiscal';