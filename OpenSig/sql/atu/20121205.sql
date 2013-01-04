# Atualizacoes da versao 1.0.12

# adicionando campo de IM aos cadastros
ALTER TABLE `emp_entidade` ADD COLUMN `emp_entidade_documento3` VARCHAR(20) NOT NULL  AFTER `emp_entidade_documento2` ;
UPDATE `emp_entidade` SET `emp_entidade_documento3` = 'ISENTO';
UPDATE `emp_entidade` SET `emp_entidade_documento2` = 'ISENTO' WHERE `emp_entidade_documento2` = '';

# adicionando tabela de consumos
CREATE  TABLE `com_consumo` (
  `com_consumo_id` INT NOT NULL AUTO_INCREMENT,
  `emp_fornecedor_id` INT NOT NULL ,
  `emp_empresa_id` INT NOT NULL ,
  `fin_pagar_id` INT NULL ,
  `com_consumo_tipo` VARCHAR(10) NOT NULL ,
  `com_consumo_documento` INT NOT NULL ,
  `com_consumo_data` DATE NOT NULL ,
  `com_consumo_valor` DECIMAL(10,2) NOT NULL ,
  `com_consumo_cfop` INT NOT NULL ,
  `com_consumo_base` DECIMAL(10,2) NOT NULL ,
  `com_consumo_aliquota` INT NOT NULL ,
  `com_consumo_icms` DECIMAL(10,2) NOT NULL ,
  `com_consumo_fechada` TINYINT(1) NOT NULL ,
  `com_consumo_paga` TINYINT(1) NOT NULL ,
  `com_consumo_observacao` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`com_consumo_id`) ,
  INDEX `FK_ com_consumo_1_idx` (`emp_fornecedor_id` ASC) ,
  INDEX `FK_ com_consumo_2_idx` (`emp_empresa_id` ASC) ,
  INDEX `FK_ com_consumo_3_idx` (`fin_pagar_id` ASC) ,
  CONSTRAINT `FK_ com_consumo_1`
    FOREIGN KEY (`emp_fornecedor_id` )
    REFERENCES `emp_fornecedor` (`emp_fornecedor_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ com_consumo_2`
    FOREIGN KEY (`emp_empresa_id` )
    REFERENCES `emp_empresa` (`emp_empresa_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ com_consumo_3`
    FOREIGN KEY (`fin_pagar_id` )
    REFERENCES `fin_pagar` (`fin_pagar_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

# atualizando a funcao e acao
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('5', 'br.com.opensig.comercial.client.controlador.comando.ComandoConsumo', '5', '0', '1');
UPDATE `sis_funcao` SET `sis_funcao_ordem`='6' WHERE `sis_funcao_id`='65';
UPDATE `sis_funcao` SET `sis_funcao_ordem`='7' WHERE `sis_funcao_id`='23';
UPDATE `sis_funcao` SET `sis_funcao_ordem`='8' WHERE `sis_funcao_id`='64';

INSERT INTO `sis_acao` (`sis_funcao_id`, `sis_acao_classe`, `sis_acao_ordem`, `sis_acao_subordem`, `sis_acao_ativo`, `sis_acao_visivel`) VALUES ('82', 'br.com.opensig.comercial.client.controlador.comando.acao.ComandoFecharConsumo', '13', '0', '1', '1');
INSERT INTO `sis_acao` (`sis_funcao_id`, `sis_acao_classe`, `sis_acao_ordem`, `sis_acao_subordem`, `sis_acao_ativo`, `sis_acao_visivel`) VALUES ('82', 'br.com.opensig.comercial.client.controlador.comando.acao.ComandoPagarConsumo', '14', '0', '1', '1');
# adicionar permissao aos grupos diretores e administradores

# atualizando os blocos do Sped
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_nivel`='2', `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC500' WHERE `fis_sped_bloco_id`='65';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_nivel`='3', `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC590' WHERE `fis_sped_bloco_id`='67';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_nivel`='2', `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroD500' WHERE `fis_sped_bloco_id`='106';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_nivel`='3', `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroD590' WHERE `fis_sped_bloco_id`='109';

# atualizando a tabela do sped
UPDATE `sis_funcao` SET `sis_funcao_classe`='br.com.opensig.fiscal.client.controlador.comando.ComandoSped' WHERE `sis_funcao_classe`='br.com.opensig.fiscal.client.controlador.comando.ComandoSpedFiscal';

ALTER TABLE `fis_sped_fiscal` DROP FOREIGN KEY `FK_fis_sped_fiscal_1` ;
ALTER TABLE `fis_sped_fiscal` CHANGE COLUMN `fis_sped_fiscal_id` `fis_sped_id` INT(11) NOT NULL AUTO_INCREMENT  , CHANGE COLUMN `fis_sped_fiscal_ano` `fis_sped_ano` INT(11) NOT NULL  , CHANGE COLUMN `fis_sped_fiscal_mes` `fis_sped_mes` INT(11) NOT NULL  , CHANGE COLUMN `fis_sped_fiscal_tipo` `fis_sped_tipo` VARCHAR(20) NOT NULL  , CHANGE COLUMN `fis_sped_fiscal_data` `fis_sped_data` DATE NOT NULL  , CHANGE COLUMN `fis_sped_fiscal_ativo` `fis_sped_ativo` TINYINT(1) NOT NULL  , CHANGE COLUMN `fis_sped_fiscal_protocolo` `fis_sped_protocolo` VARCHAR(15) NOT NULL  , 
  ADD CONSTRAINT `FK_fis_sped_1`
  FOREIGN KEY (`emp_empresa_id` )
  REFERENCES `emp_empresa` (`emp_empresa_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `FK_fis_sped_fiscal_1` 
, ADD INDEX `FK_fis_sped_1` USING BTREE (`emp_empresa_id` ASC) , RENAME TO  `fis_sped` ;
