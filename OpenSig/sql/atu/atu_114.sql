# Atualizacoes da versao 1.1.4

# Criando a tabela IBPT para ser usada nos calculos de impostos
DROP TABLE IF EXISTS ibpt;
CREATE TABLE `ibpt` (
  `ibpt_codigo` varchar(10) NOT NULL,
  `ibpt_ex` varchar(100) DEFAULT NULL,
  `ibpt_tabela` int(11) NOT NULL,
  `ibpt_descricao` varchar(500) NOT NULL,
  `ibpt_aliqNac` decimal(10,2) NOT NULL,
  `ibpt_aliqImp` decimal(10,2) NOT NULL,
  `ibpt_versao` varchar(5) DEFAULT NULL
) ENGINE=InnoDB;

# Limpando e inserindo os dados
DELETE FROM ibpt;
LOAD DATA LOCAL INFILE "ibpt.csv" INTO TABLE ibpt COLUMNS TERMINATED BY ';' LINES TERMINATED BY '\n' IGNORE 1 LINES;

# Adicionando parametro de configuracao para esta funcionalidade do IBPT
INSERT INTO `sis_configuracao` (`emp_empresa_id`, `sis_configuracao_chave`, `sis_configuracao_valor`, `sis_configuracao_descricao`, `sis_configuracao_ativo`, `sis_configuracao_sistema`) VALUES ('1', 'NFE.IBPT', 'SIM', 'INFORMA SE SERÁ MOSTRADO NAS INFORMAÇÕES ADICIONAIS DA NFE O VALOR APROXIMADO DOS TRIBUTOS.', '1', '0');
INSERT INTO `sis_configuracao` (`emp_empresa_id`, `sis_configuracao_chave`, `sis_configuracao_valor`, `sis_configuracao_descricao`, `sis_configuracao_ativo`, `sis_configuracao_sistema`) VALUES ('2', 'NFE.IBPT', 'SIM', 'INFORMA SE SERÁ MOSTRADO NAS INFORMAÇÕES ADICIONAIS DA NFE O VALOR APROXIMADO DOS TRIBUTOS.', '1', '0');

# Arrumando tabelas que nao foram criadas como INNODB
ALTER TABLE com_ecf_documento ENGINE=InnoDB;
ALTER TABLE com_ecf_nota ENGINE=InnoDB;
ALTER TABLE com_ecf_nota_produto ENGINE=InnoDB;
ALTER TABLE com_consumo ENGINE=InnoDB;
ALTER TABLE prod_tipo ENGINE=InnoDB;
ALTER TABLE prod_composicao ENGINE=InnoDB;
ALTER TABLE prod_pis ENGINE=InnoDB;
ALTER TABLE prod_cofins ENGINE=InnoDB;
ALTER TABLE prod_grade_tipo ENGINE=InnoDB;
ALTER TABLE prod_grade ENGINE=InnoDB;
ALTER TABLE prod_estoque_grade ENGINE=InnoDB;

# Arrumando tabelas de grade
DELETE FROM prod_grade WHERE prod_produto_id NOT IN (SELECT prod_produto_id FROM prod_produto);
ALTER TABLE `prod_grade` 
  ADD CONSTRAINT `FK_prod_grade_1`
  FOREIGN KEY (`prod_produto_id` )
  REFERENCES `prod_produto` (`prod_produto_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE
, ADD INDEX `FK_prod_grade_1_idx` USING BTREE (`prod_produto_id` ASC) 
, DROP INDEX `FK_prod_grade_idx` ;

# Arrumando tabelas de grade_estoque
DELETE FROM prod_estoque_grade WHERE prod_grade_id NOT IN (SELECT prod_grade_id FROM prod_grade);
ALTER TABLE `prod_estoque_grade` 
DROP INDEX `FK_ prod_estoque_grade_2_idx` 
, DROP INDEX `FK_ prod_estoque_grade_1_idx` ;
ALTER TABLE `prod_estoque_grade` 
  ADD CONSTRAINT `FK_prod_estoque_grade_1`
  FOREIGN KEY (`emp_empresa_id` )
  REFERENCES `emp_empresa` (`emp_empresa_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_prod_estoque_grade_2`
  FOREIGN KEY (`prod_grade_id` )
  REFERENCES `prod_grade` (`prod_grade_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE
, ADD INDEX `FK_prod_estoque_grade_1_idx` USING BTREE (`emp_empresa_id` ASC) 
, ADD INDEX `FK_prod_estoque_grade_2_idx` USING BTREE (`prod_grade_id` ASC) ;

# Arrumando tabela de com_consumo
ALTER TABLE `com_consumo` 
  ADD CONSTRAINT `FK_com_consumo_1`
  FOREIGN KEY (`emp_fornecedor_id` )
  REFERENCES `emp_fornecedor` (`emp_fornecedor_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_com_consumo_2`
  FOREIGN KEY (`emp_empresa_id` )
  REFERENCES `emp_empresa` (`emp_empresa_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_com_consumo_3`
  FOREIGN KEY (`fin_pagar_id` )
  REFERENCES `fin_pagar` (`fin_pagar_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_com_consumo_1_idx` USING BTREE (`emp_fornecedor_id` ASC) 
, ADD INDEX `FK_com_consumo_2_idx` USING BTREE (`emp_empresa_id` ASC) 
, ADD INDEX `FK_com_consumo_3_idx` USING BTREE (`fin_pagar_id` ASC) 
, DROP INDEX `FK_ com_consumo_3_idx` 
, DROP INDEX `FK_ com_consumo_2_idx` 
, DROP INDEX `FK_ com_consumo_1_idx` ;

#  Arrumando tabela de prod_composicao
DELETE FROM `prod_composicao` WHERE `prod_produto_principal` NOT IN (SELECT prod_produto_id FROM prod_produto);
ALTER TABLE `prod_composicao` 
DROP INDEX `FK_prod_composicao_3` 
, DROP INDEX `FK_prod_composicao_2` 
, DROP INDEX `FK_prod_composicao_1` ;
ALTER TABLE `prod_composicao` 
  ADD CONSTRAINT `FK_prod_composicao_1`
  FOREIGN KEY (`prod_produto_principal` )
  REFERENCES `prod_produto` (`prod_produto_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE, 
  ADD CONSTRAINT `FK_prod_composicao_2`
  FOREIGN KEY (`prod_produto_id` )
  REFERENCES `prod_produto` (`prod_produto_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_prod_composicao_3`
  FOREIGN KEY (`prod_embalagem_id` )
  REFERENCES `prod_embalagem` (`prod_embalagem_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_prod_composicao_1_idx` USING BTREE (`prod_produto_principal` ASC) 
, ADD INDEX `FK_prod_composicao_2_idx` USING BTREE (`prod_produto_id` ASC) 
, ADD INDEX `FK_prod_composicao_3_idx` USING BTREE (`prod_embalagem_id` ASC) ;

# Arrumando a tabela de com_ecf_documento
ALTER TABLE `com_ecf_documento` 
DROP INDEX `FK_com_ecf_documento_1_idx` ;
ALTER TABLE `com_ecf_documento` 
  ADD CONSTRAINT `FK_com_ecf_documento_1`
  FOREIGN KEY (`com_ecf_id` )
  REFERENCES `com_ecf` (`com_ecf_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_com_ecf_documento_1_idx` USING BTREE (`com_ecf_id` ASC) ;

# Arrumando a tabela de com_ecf_nota
ALTER TABLE `com_ecf_nota` 
DROP INDEX `FK_com_ecf_nota_2` 
, DROP INDEX `FK_com_ecf_nota_1` ;
ALTER TABLE `com_ecf_nota` 
  ADD CONSTRAINT `FK_com_ecf_nota_1`
  FOREIGN KEY (`emp_cliente_id` )
  REFERENCES `emp_cliente` (`emp_cliente_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_com_ecf_nota_2`
  FOREIGN KEY (`emp_empresa_id` )
  REFERENCES `emp_empresa` (`emp_empresa_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_com_ecf_nota_1_idx` USING BTREE (`emp_cliente_id` ASC) 
, ADD INDEX `FK_com_ecf_nota_2_idx` USING BTREE (`emp_empresa_id` ASC) ;

# Arrumando a tabela de com_ecf_nota_produto
ALTER TABLE `com_ecf_nota_produto` 
DROP INDEX `FK_com_ecf_nota_produto_3` 
, DROP INDEX `FK_com_ecf_nota_produto_2` 
, DROP INDEX `FK_com_ecf_nota_produto_1` ;
ALTER TABLE `com_ecf_nota_produto` 
  ADD CONSTRAINT `FK_com_ecf_nota_produto_1`
  FOREIGN KEY (`com_ecf_nota_id` )
  REFERENCES `com_ecf_nota` (`com_ecf_nota_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE, 
  ADD CONSTRAINT `FK_com_ecf_nota_produto_2`
  FOREIGN KEY (`prod_produto_id` )
  REFERENCES `prod_produto` (`prod_produto_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_com_ecf_nota_produto_3`
  FOREIGN KEY (`prod_embalagem_id` )
  REFERENCES `prod_embalagem` (`prod_embalagem_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_com_ecf_nota_produto_1_idx` (`com_ecf_nota_id` ASC) 
, ADD INDEX `FK_com_ecf_nota_produto_2_idx` (`prod_produto_id` ASC) 
, ADD INDEX `FK_com_ecf_nota_produto_3_idx` (`prod_embalagem_id` ASC) ;

# Adicionando o campo de receber ao com_ecf_nota
ALTER TABLE `com_ecf_nota` ADD COLUMN `fin_receber_id` INT NULL  AFTER `emp_empresa_id`;
UPDATE `com_ecf_nota`, `fin_receber` SET `com_ecf_nota`.`fin_receber_id` = `fin_receber`.`fin_receber_id` 
WHERE `com_ecf_nota`.`com_ecf_nota_numero` = `fin_receber`.`fin_receber_nfe` AND `fin_receber`.`fin_receber_observacao` = 'NFC EMITIDO PELO ECF.';
ALTER TABLE `com_ecf_nota` CHANGE COLUMN `fin_receber_id` `fin_receber_id` INT(11) NOT NULL  , 
  ADD CONSTRAINT `FK_com_ecf_nota_3`
  FOREIGN KEY (`fin_receber_id` )
  REFERENCES `fin_receber` (`fin_receber_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE
, ADD INDEX `FK_com_ecf_nota_3_idx` USING BTREE (`fin_receber_id` ASC) ;

# Criando a tabela de com_troca
CREATE TABLE `com_troca` (
  `com_troca_id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_empresa_id` int(11) NOT NULL,
  `com_compra_id` int(11) DEFAULT NULL,
  `com_troca_cliente` varchar(18) NOT NULL,
  `com_troca_data` datetime NOT NULL,
  `com_troca_valor` decimal(10,2) NOT NULL,
  `com_troca_ecf` INT NOT NULL,
  `com_troca_coo` INT NOT NULL,
  `com_troca_ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`com_troca_id`),
  KEY `FK_ com_troca_1_idx` (`emp_empresa_id`) USING BTREE,
  KEY `FK_ com_troca_2_idx` (`com_compra_id`) USING BTREE,
  CONSTRAINT `FK_ com_troca_1` FOREIGN KEY (`emp_empresa_id`) REFERENCES `emp_empresa` (`emp_empresa_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ com_troca_2` FOREIGN KEY (`com_compra_id`) REFERENCES `com_compra` (`com_compra_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

# Criando a tabela de com_troca_produto
CREATE  TABLE `com_troca_produto` (
  `com_troca_produto_id` INT NOT NULL AUTO_INCREMENT ,
  `com_troca_id` INT NOT NULL ,
  `prod_produto_id` INT NOT NULL ,
  `prod_embalagem_id` INT NOT NULL ,
  `com_troca_produto_barra` VARCHAR(14) NULL ,
  `com_troca_produto_quantidade` DECIMAL(10,3) NOT NULL ,
  `com_troca_produto_valor` DECIMAL(10,2) NOT NULL ,
  `com_troca_produto_total` DECIMAL(10,2) NOT NULL ,
  `com_troca_produto_ordem` INT NOT NULL ,
  PRIMARY KEY (`com_troca_produto_id`) ,
  INDEX `FK_com_troca_produto_1_idx` USING BTREE (`com_troca_id` ASC) ,
  INDEX `FK_com_troca_produto_2_idx` USING BTREE (`prod_produto_id` ASC) ,
  INDEX `FK_com_troca_produto_3_idx` USING BTREE (`prod_embalagem_id` ASC) ,
  CONSTRAINT `FK_com_troca_produto_1`
    FOREIGN KEY (`com_troca_id` )
    REFERENCES `com_troca` (`com_troca_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_com_troca_produto_2`
    FOREIGN KEY (`prod_produto_id` )
    REFERENCES `prod_produto` (`prod_produto_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_com_troca_produto_3`
    FOREIGN KEY (`prod_embalagem_id` )
    REFERENCES `prod_embalagem` (`prod_embalagem_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)ENGINE=InnoDB;

# Adicionando o campo de troca a venda ecf, para caso a mesma tenha alguma vinculada
ALTER TABLE `com_ecf_venda` ADD COLUMN `com_troca_id` INT NULL  AFTER `fin_receber_id` , 
  ADD CONSTRAINT `FK_com_ecf_venda_8`
  FOREIGN KEY (`com_troca_id` )
  REFERENCES `com_troca` (`com_troca_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_com_ecf_venda_8_idx` USING BTREE (`com_troca_id` ASC) ;

# Adicionando as funcoes de troca ao sistema
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('5', 'br.com.opensig.comercial.client.controlador.comando.ComandoTroca', '5', '1', '1');
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('5', 'br.com.opensig.comercial.client.controlador.comando.ComandoTrocaProduto', '5', '2', '1');
UPDATE `sis_funcao` SET `sis_funcao_ordem` = 9 WHERE `sis_funcao_classe` = 'br.com.opensig.comercial.client.controlador.comando.ComandoConsumo';

# Adicionando a acao de gerar compra com as trocas selecionadas
INSERT INTO `sis_acao` (`sis_funcao_id`, `sis_acao_classe`, `sis_acao_ordem`, `sis_acao_subordem`, `sis_acao_ativo`, `sis_acao_visivel`) VALUES ('0', 'br.com.opensig.comercial.client.controlador.comando.acao.ComandoGerarCompra', '13', '0', '1', '1');
UPDATE `sis_acao` SET `sis_funcao_id` = (SELECT `sis_funcao_id` FROM `sis_funcao` WHERE `sis_funcao_classe` = 'br.com.opensig.comercial.client.controlador.comando.ComandoTroca') WHERE `sis_acao_classe` = 'br.com.opensig.comercial.client.controlador.comando.acao.ComandoGerarCompra';
INSERT INTO `com_natureza` (`emp_empresa_id`, `com_natureza_nome`, `com_natureza_descricao`, `com_natureza_cfop_trib`, `com_natureza_cfop_sub`, `com_natureza_icms`, `com_natureza_ipi`, `com_natureza_pis`, `com_natureza_cofins`) VALUES ('1', 'DEVOLUCAO CLIENTE', 'DEVOLUCAO DE VENDA DE MERCADORIA', '1202', '1410', '1', '0', '0', '0');
INSERT INTO `com_natureza` (`emp_empresa_id`, `com_natureza_nome`, `com_natureza_descricao`, `com_natureza_cfop_trib`, `com_natureza_cfop_sub`, `com_natureza_icms`, `com_natureza_ipi`, `com_natureza_pis`, `com_natureza_cofins`) VALUES ('2', 'DEVOLUCAO CLIENTE', 'DEVOLUCAO DE VENDA DE MERCADORIA', '1202', '1410', '1', '0', '0', '0');

# Alterando o campo de numero do endereço para texto
ALTER TABLE `emp_endereco` CHANGE COLUMN `emp_endereco_numero` `emp_endereco_numero` VARCHAR(10) NOT NULL  ;

