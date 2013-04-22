# Atualizacoes da versao 1.1.2

# Arrumando a configuracao do sped
UPDATE `sis_configuracao` SET `sis_configuracao_chave`='SPED.CONTRIBUICAO.0110.IND_REG_CUM' WHERE `sis_configuracao_chave`='SPED.CONTRIBUICAO.0000.IND_REG_CUM';
UPDATE `sis_configuracao` SET `sis_configuracao_valor`='007' WHERE `sis_configuracao_chave`='SPED.FISCAL.0000.COD_VER';

# Arrumando os blocos do sped
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='55' WHERE `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.fiscal.bloco1.Registro1990';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='56' WHERE `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.fiscal.bloco9.Registro9001';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='57' WHERE `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.fiscal.bloco9.Registro9900';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='58' WHERE `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.fiscal.bloco9.Registro9990';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='54' WHERE `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.fiscal.bloco1.Registro1010';
DELETE FROM `fis_sped_bloco` WHERE `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.fiscal.bloco0.Registro0450';
DELETE FROM `fis_sped_bloco` WHERE `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.fiscal.blocoC.RegistroC110';

# Colocando os novos campos nos produtos vendidos
ALTER TABLE `com_venda_produto` CHANGE COLUMN `com_venda_produto_icms` `com_venda_produto_icms` DECIMAL(4,2) NOT NULL  , 
CHANGE COLUMN `com_venda_produto_ipi` `com_venda_produto_ipi` DECIMAL(4,2) NOT NULL  , ADD COLUMN `com_venda_produto_cfop` INT NOT NULL  AFTER `com_venda_produto_total_liquido` , 
ADD COLUMN `com_venda_produto_icms_cst` VARCHAR(3) NOT NULL  AFTER `com_venda_produto_cfop` , ADD COLUMN `com_venda_produto_ipi_cst` VARCHAR(2) NOT NULL  AFTER `com_venda_produto_icms` , 
ADD COLUMN `com_venda_produto_pis_cst` VARCHAR(2) NOT NULL  AFTER `com_venda_produto_ipi` , ADD COLUMN `com_venda_produto_pis` DECIMAL(4,2) NOT NULL  AFTER `com_venda_produto_pis_cst` , 
ADD COLUMN `com_venda_produto_cofins_cst` VARCHAR(2) NOT NULL  AFTER `com_venda_produto_pis` , ADD COLUMN `com_venda_produto_cofins` DECIMAL(4,2) NOT NULL  AFTER `com_venda_produto_cofins_cst` ;

# Colando os novos campos nos produtos comprados
ALTER TABLE `com_compra_produto` CHANGE COLUMN `com_compra_produto_icms` `com_compra_produto_icms` DECIMAL(4,2) NOT NULL  , 
CHANGE COLUMN `com_compra_produto_ipi` `com_compra_produto_ipi` DECIMAL(4,2) NOT NULL  , ADD COLUMN `com_compra_produto_icms_cst` VARCHAR(3) NOT NULL  AFTER `com_compra_produto_cfop` , 
ADD COLUMN `com_compra_produto_ipi_cst` VARCHAR(2) NOT NULL  AFTER `com_compra_produto_icms` , ADD COLUMN `com_compra_produto_pis_cst` VARCHAR(2) NOT NULL  AFTER `com_compra_produto_ipi` , 
ADD COLUMN `com_compra_produto_pis` DECIMAL(4,2) NOT NULL  AFTER `com_compra_produto_pis_cst` , ADD COLUMN `com_compra_produto_cofins_cst` VARCHAR(2) NOT NULL  AFTER `com_compra_produto_pis` , 
ADD COLUMN `com_compra_produto_cofins` DECIMAL(4,2) NOT NULL  AFTER `com_compra_produto_cofins_cst` ;

# Arrumando os campos de natureza de operacao
ALTER TABLE `com_natureza` CHANGE COLUMN `com_natureza_pis` `com_natureza_pis2` DECIMAL(4,2) NOT NULL  , CHANGE COLUMN `com_natureza_cofins` `com_natureza_cofins2` DECIMAL(4,2) NOT NULL  ;
ALTER TABLE `com_natureza` ADD COLUMN `com_natureza_pis` TINYINT(1) NOT NULL  AFTER `com_natureza_cofins2` , ADD COLUMN `com_natureza_cofins` TINYINT(1) NOT NULL  AFTER `com_natureza_pis` ;
UPDATE `com_natureza` SET `com_natureza_pis` = 1 WHERE `com_natureza_pis2` > 0.00;
UPDATE `com_natureza` SET `com_natureza_cofins` = 1 WHERE `com_natureza_cofins2` > 0.00;
ALTER TABLE `com_natureza` DROP COLUMN `com_natureza_cofins2` , DROP COLUMN `com_natureza_pis2` ;

# Criando a tabela de pis
CREATE  TABLE `prod_pis` (
  `prod_pis_id` INT NOT NULL AUTO_INCREMENT ,
  `prod_pis_nome` VARCHAR(100) NOT NULL ,
  `prod_pis_cst_entrada` VARCHAR(2) NOT NULL ,
  `prod_pis_cst_saida` VARCHAR(2) NOT NULL ,
  `prod_pis_aliquota` DECIMAL(4,2) NOT NULL ,
  `prod_pis_descreto` VARCHAR(1000) NOT NULL ,
  PRIMARY KEY (`prod_pis_id`) ); 
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL COM ALÍQUOTA BÁSICA', '70', '01', '0.65', '');
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL COM ALÍQUOTA DIFERENCIADA', '99', '02', '0.00', '');
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL COM ALÍQUOTA POR UNIDADE DE MEDIDA DE PRODUTO', '99', '03', '0.00', '');
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL MONOFÁSICA - REVENDA A ALÍQUOTA ZERO', '99', '04', '0.00', '');
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL POR SUBSTITUIÇÃO TRIBUTÁRIA', '99', '05', '0.00', '');
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL A ALÍQUOTA ZERO', '99', '06', '0.00', '');
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OPERAÇÃO ISENTA DA CONTRIBUIÇÃO', '99', '07', '0.00', '');
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OPERAÇÃO SEM INCIDÊNCIA DA CONTRIBUIÇÃO', '99', '08', '0.00', '');
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OPERAÇÃO COM SUSPENSãO DA CONTRIBUIÇÃO', '99', '09', '0.00', '');
INSERT INTO `prod_pis` (`prod_pis_nome`, `prod_pis_cst_entrada`, `prod_pis_cst_saida`, `prod_pis_aliquota`, `prod_pis_descreto`) VALUES ('OUTRAS OPERAÇÕES DE SAíDA', '98', '49', '0.00', '');

# Criando a tabela de cofins
CREATE  TABLE `prod_cofins` (
  `prod_cofins_id` INT NOT NULL AUTO_INCREMENT ,
  `prod_cofins_nome` VARCHAR(100) NOT NULL ,
  `prod_cofins_cst_entrada` VARCHAR(2) NOT NULL ,
  `prod_cofins_cst_saida` VARCHAR(2) NOT NULL ,
  `prod_cofins_aliquota` DECIMAL(4,2) NOT NULL ,
  `prod_cofins_descreto` VARCHAR(1000) NOT NULL ,
  PRIMARY KEY (`prod_cofins_id`) );
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL COM ALÍQUOTA BÁSICA', '70', '01', '0.65', '');
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL COM ALÍQUOTA DIFERENCIADA', '99', '02', '0.00', '');
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL COM ALÍQUOTA POR UNIDADE DE MEDIDA DE PRODUTO', '99', '03', '0.00', '');
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL MONOFÁSICA - REVENDA A ALÍQUOTA ZERO', '99', '04', '0.00', '');
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL POR SUBSTITUIÇÃO TRIBUTÁRIA', '99', '05', '0.00', '');
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OPERAÇÃO TRIBUTÁVEL A ALÍQUOTA ZERO', '99', '06', '0.00', '');
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OPERAÇÃO ISENTA DA CONTRIBUIÇÃO', '99', '07', '0.00', '');
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OPERAÇÃO SEM INCIDÊNCIA DA CONTRIBUIÇÃO', '99', '08', '0.00', '');
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OPERAÇÃO COM SUSPENSãO DA CONTRIBUIÇÃO', '99', '09', '0.00', '');
INSERT INTO `prod_cofins` (`prod_cofins_nome`, `prod_cofins_cst_entrada`, `prod_cofins_cst_saida`, `prod_cofins_aliquota`, `prod_cofins_descreto`) VALUES ('OUTRAS OPERAÇÕES DE SAíDA', '98', '49', '0.00', '');

# Mudando a tabela de tributacao para icms
ALTER TABLE `prod_tributacao` CHANGE COLUMN `prod_tributacao_id` `prod_icms_id` INT(11) NOT NULL AUTO_INCREMENT  , CHANGE COLUMN `prod_tributacao_nome` `prod_icms_nome` VARCHAR(100) NOT NULL  , 
CHANGE COLUMN `prod_tributacao_cst` `prod_icms_cst` VARCHAR(2) NOT NULL  , CHANGE COLUMN `prod_tributacao_cson` `prod_icms_cson` VARCHAR(3) NOT NULL  , CHANGE COLUMN `prod_tributacao_cfop` `prod_icms_cfop` INT(11) NOT NULL  , 
CHANGE COLUMN `prod_tributacao_ecf` `prod_icms_ecf` VARCHAR(7) NOT NULL  , CHANGE COLUMN `prod_tributacao_dentro` `prod_icms_dentro` DECIMAL(4,2) NOT NULL  , CHANGE COLUMN `prod_tributacao_fora` `prod_icms_fora` DECIMAL(4,2) NOT NULL  , 
CHANGE COLUMN `prod_tributacao_decreto` `prod_icms_decreto` VARCHAR(1000) NOT NULL  , RENAME TO  `opensig`.`prod_icms` ;

ALTER TABLE `prod_produto` DROP FOREIGN KEY `FK_prod_produto_2` ;
ALTER TABLE `prod_produto` CHANGE COLUMN `prod_tributacao_id` `prod_icms_id` INT(11) NOT NULL  
, DROP INDEX `FK_prod_produto_2` ;

ALTER TABLE `prod_produto` ADD COLUMN `prod_pis_id` INT NOT NULL  AFTER `prod_ipi_id` , ADD COLUMN `prod_cofins_id` INT NOT NULL  AFTER `prod_pis` ;
UPDATE `prod_produto` SET `prod_pis_id` = 1, `prod_cofins_id` = 1;

ALTER TABLE `prod_produto` 
  ADD CONSTRAINT `FK_prod_produto_2`
  FOREIGN KEY (`prod_icms_id` )
  REFERENCES `prod_icms` (`prod_icms_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_prod_produto_8`
  FOREIGN KEY (`prod_pis_id` )
  REFERENCES `prod_pis` (`prod_pis_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_prod_produto_9`
  FOREIGN KEY (`prod_cofins_id` )
  REFERENCES `prod_cofins` (`prod_cofins_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_prod_produto_2` USING BTREE (`prod_icms_id` ASC) 
, ADD INDEX `FK_prod_produto_8` USING BTREE (`prod_pis_id` ASC) 
, ADD INDEX `FK_prod_produto_9` USING BTREE (`prod_cofins_id` ASC) ;

