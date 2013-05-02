# Atualizacoes da versao 1.1.3

# Adicionando os tipos de exportacoes das NFe
UPDATE `sis_exp_imp` SET `sis_exp_imp_imagem`='xml.png' WHERE `sis_exp_imp_id`='1';
UPDATE `sis_exp_imp` SET `sis_exp_imp_imagem`='xml.png' WHERE `sis_exp_imp_id`='9';

# Adicionando campos a NFe de saida
ALTER TABLE `fis_nota_saida` CHANGE COLUMN `fis_nota_saida_protocolo` `fis_nota_saida_protocolo` VARCHAR(15) NOT NULL  AFTER `fis_nota_saida_cofins` , 
ADD COLUMN `fis_nota_saida_protocolo_carta` VARCHAR(15) NOT NULL  AFTER `fis_nota_saida_xml_cancelado` , ADD COLUMN `fis_nota_saida_xml_carta` TEXT NOT NULL  AFTER `fis_nota_saida_protocolo_carta` , 
ADD COLUMN `fis_nota_saida_evento` INT NOT NULL  AFTER `fis_nota_saida_xml_carta` ;

# Adicionando campos a NFe de entrada
ALTER TABLE `fis_nota_entrada` CHANGE COLUMN `fis_nota_entrada_protocolo` `fis_nota_entrada_protocolo` VARCHAR(15) NOT NULL  AFTER `fis_nota_entrada_cofins` , 
ADD COLUMN `fis_nota_entrada_protocolo_carta` VARCHAR(15) NOT NULL  AFTER `fis_nota_entrada_xml_cancelado` , ADD COLUMN `fis_nota_entrada_xml_carta` TEXT NOT NULL  AFTER `fis_nota_entrada_protocolo_carta` , 
ADD COLUMN `fis_nota_entrada_evento` INT NOT NULL  AFTER `fis_nota_entrada_xml_carta` ;

# Adicionando as acoes de CCe
INSERT INTO `sis_acao` (`sis_funcao_id`, `sis_acao_classe`, `sis_acao_ordem`, `sis_acao_subordem`, `sis_acao_ativo`, `sis_acao_visivel`) VALUES ('49', 'br.com.opensig.fiscal.client.controlador.comando.acao.ComandoCartaSaida', '13', '0', '1', '1');
INSERT INTO `sis_acao` (`sis_funcao_id`, `sis_acao_classe`, `sis_acao_ordem`, `sis_acao_subordem`, `sis_acao_ativo`, `sis_acao_visivel`) VALUES ('50', 'br.com.opensig.fiscal.client.controlador.comando.acao.ComandoCartaEntrada', '13', '0', '1', '1');
