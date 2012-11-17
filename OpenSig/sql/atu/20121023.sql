# versao do layout do SPED Fiscal
INSERT INTO `sis_configuracao` (`emp_empresa_id`,`sis_configuracao_ativo`,`sis_configuracao_chave`,`sis_configuracao_descricao`,`sis_configuracao_sistema`,`sis_configuracao_valor`)
VALUES(1,1,'SPED.0000.COD_VER','CODIGO DO LAYOUT DA VERSAO DO SPED ATUAL',0,'006');

# numero da ultima NFe utilizada para que o sistema possa inciar o controle
INSERT INTO `sis_configuracao` (`emp_empresa_id`,`sis_configuracao_ativo`,`sis_configuracao_chave`,`sis_configuracao_descricao`,`sis_configuracao_sistema`,`sis_configuracao_valor`)
VALUES(1,1,'NFE.NUMERO','NUMERO DA ULTIMA NFE EMITIDA POR FORA DO SISTEMA PARA INICIAR O CONTROLE INTERNO',0,'0');

# numero do suframa para zona franca de manaus
INSERT INTO `sis_configuracao` (`emp_empresa_id`,`sis_configuracao_ativo`,`sis_configuracao_chave`,`sis_configuracao_descricao`,`sis_configuracao_sistema`,`sis_configuracao_valor`)
VALUES(1,1,'SPED.0000.SUFRAMA','CODIGO DA ZONA FRANCA DE MANAUS',0,'');

# adicionando data de conciliacao ao financeiro
ALTER TABLE `fin_pagamento` ADD COLUMN `fin_pagamento_conciliado` DATE NULL DEFAULT NULL  AFTER `fin_pagamento_realizado` ;
ALTER TABLE `fin_pagamento` CHANGE COLUMN `fin_pagamento_quitado` `fin_pagamento_status` VARCHAR(10) NOT NULL  AFTER `fin_pagamento_conciliado` ;
ALTER TABLE `fin_recebimento` ADD COLUMN `fin_recebimento_conciliado` DATE NULL DEFAULT NULL  AFTER `fin_recebimento_realizado` ;
ALTER TABLE `fin_recebimento` CHANGE COLUMN `fin_recebimento_quitado` `fin_recebimento_status` VARCHAR(10) NOT NULL  AFTER `fin_recebimento_conciliado` ;
UPDATE `fin_pagamento` SET `fin_pagamento_status` = 'ABERTO' WHERE `fin_pagamento_status` = '0';
UPDATE `fin_pagamento` SET `fin_pagamento_status` = 'REALIZADO' WHERE `fin_pagamento_status` = '1';
UPDATE `fin_recebimento` SET `fin_recebimento_status` = 'ABERTO' WHERE `fin_recebimento_status` = '0';
UPDATE `fin_recebimento` SET `fin_recebimento_status` = 'REALIZADO' WHERE `fin_recebimento_status` = '1';
# modificando as acoes do financeiro para agrupar
INSERT INTO `sis_acao` (`sis_funcao_id`, `sis_acao_classe`, `sis_acao_ordem`, `sis_acao_subordem`, `sis_acao_ativo`, `sis_acao_visivel`) VALUES ('30', 'br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoConciliarPagamento', '13', '3', '1', '1');
INSERT INTO `sis_acao` (`sis_funcao_id`, `sis_acao_classe`, `sis_acao_ordem`, `sis_acao_subordem`, `sis_acao_ativo`, `sis_acao_visivel`) VALUES ('46', 'br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoConciliarRecebimento', '13', '3', '1', '1');
UPDATE `sis_acao` SET `sis_acao_ordem` = 13, `sis_acao_subordem` = 1 WHERE `sis_acao_classe` LIKE 'br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoQuitar%';
UPDATE `sis_acao` SET `sis_acao_ordem` = 13, `sis_acao_subordem` = 2 WHERE `sis_acao_classe` LIKE 'br.com.opensig.financeiro.client.controlador.comando.financeiro.ComandoEstornar%';
UPDATE `sis_acao` SET `sis_acao_ordem` = 16 WHERE `sis_acao_classe` LIKE 'br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoGerar%';
UPDATE `sis_acao` SET `sis_acao_ordem` = 17 WHERE `sis_acao_classe` LIKE 'br.com.opensig.financeiro.client.controlador.comando.boleto.ComandoRecibo%';
### dar permissao aos grupos e usuarios que tem permissao de quitar

# criando as novas funcoes do sistema
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('5', 'Separador', '3', '5', '1');
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('5', 'br.com.opensig.comercial.client.controlador.comando.ComandoEcfDocumento', '3', '6', '1');
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('5', 'br.com.opensig.comercial.client.controlador.comando.ComandoEcfNota', '3', '7', '1');
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('5', 'br.com.opensig.comercial.client.controlador.comando.ComandoEcfNotaProduto', '3', '8', '1');
### dar permissao aos grupos e usuarios que tem permissao de ecf

# atualizando as formas de pagamentos
ALTER TABLE `fin_forma` ADD COLUMN `fin_forma_codigo` VARCHAR(2) NOT NULL  AFTER `fin_forma_descricao` , ADD COLUMN `fin_forma_tef` TINYINT(1) NOT NULL  AFTER `fin_forma_codigo` , ADD COLUMN `fin_forma_vinculado` TINYINT(1) NOT NULL  AFTER `fin_forma_tef` , 
ADD COLUMN `fin_forma_debito` TINYINT(1) NOT NULL  AFTER `fin_forma_vinculado` , ADD COLUMN `fin_forma_rede` VARCHAR(20) NOT NULL  AFTER `fin_forma_debito` , ADD COLUMN `fin_forma_pagar` TINYINT(1) NOT NULL  AFTER `fin_forma_rede` , ADD COLUMN `fin_forma_receber` TINYINT(1) NOT NULL  AFTER `fin_forma_pagar` , DROP INDEX `UNIQUE`;
UPDATE `fin_forma` SET `fin_forma_codigo` = '00', `fin_forma_rede` = 'LOJA' WHERE `fin_forma_descricao` <> 'DINHEIRO' && `fin_forma_descricao` <> 'CHEQUE';
UPDATE `fin_forma` SET `fin_forma_codigo` = '01', `fin_forma_rede` = 'LOJA' WHERE `fin_forma_descricao` = 'DINHEIRO';
UPDATE `fin_forma` SET `fin_forma_codigo` = '02', `fin_forma_tef` = 1, `fin_forma_vinculado` = 1, `fin_forma_rede` = 'REDECARD' WHERE `fin_forma_descricao` = 'CHEQUE';
UPDATE `fin_forma` SET `fin_forma_descricao` = UPPER(`fin_forma_descricao`);
INSERT INTO `fin_forma` (`fin_forma_descricao`, `fin_forma_codigo`, `fin_forma_tef`, `fin_forma_vinculado`, `fin_forma_debito`, `fin_forma_rede`, `fin_forma_pagar`, `fin_forma_receber`) VALUES ('MASTER - DEB', '03', '1', '1', '1', 'REDECARD', '1', '1');
INSERT INTO `fin_forma` (`fin_forma_descricao`, `fin_forma_codigo`, `fin_forma_tef`, `fin_forma_vinculado`, `fin_forma_debito`, `fin_forma_rede`, `fin_forma_pagar`, `fin_forma_receber`) VALUES ('MASTER - CRED', '03', '1', '1', '0', 'REDECARD', '1', '1');
INSERT INTO `fin_forma` (`fin_forma_descricao`, `fin_forma_codigo`, `fin_forma_tef`, `fin_forma_vinculado`, `fin_forma_debito`, `fin_forma_rede`, `fin_forma_pagar`, `fin_forma_receber`) VALUES ('VISA - DEB', '03', '1', '1', '1', 'VISANET', '1', '1');
INSERT INTO `fin_forma` (`fin_forma_descricao`, `fin_forma_codigo`, `fin_forma_tef`, `fin_forma_vinculado`, `fin_forma_debito`, `fin_forma_rede`, `fin_forma_pagar`, `fin_forma_receber`) VALUES ('VISA - CRED', '03', '1', '1', '0', 'VISANET', '1', '1');
INSERT INTO `fin_forma` (`fin_forma_descricao`, `fin_forma_codigo`, `fin_forma_tef`, `fin_forma_vinculado`, `fin_forma_debito`, `fin_forma_rede`, `fin_forma_pagar`, `fin_forma_receber`) VALUES ('HIPER - DEB', '03', '1', '1', '1', 'HCARD', '1', '1');
INSERT INTO `fin_forma` (`fin_forma_descricao`, `fin_forma_codigo`, `fin_forma_tef`, `fin_forma_vinculado`, `fin_forma_debito`, `fin_forma_rede`, `fin_forma_pagar`, `fin_forma_receber`) VALUES ('HIPER - CRED', '03', '1', '1', '0', 'HCARD', '1', '1');
INSERT INTO `fin_forma` (`fin_forma_descricao`, `fin_forma_codigo`, `fin_forma_tef`, `fin_forma_vinculado`, `fin_forma_debito`, `fin_forma_rede`, `fin_forma_pagar`, `fin_forma_receber`) VALUES ('AMERICAN EXPRESS', '03', '1', '1', '0', 'AMEX', '1', '1');
# CASO TENHA MAIS CARTOES, VERIFICAR SE PERTENCE A UMA DAS 3 REDES HOMOLOGADAS, SE SIM SO CADASTRAR, SENAO FICA SEM PODER USAR


# remove a tabela de bandeira, nao sera mais usada e coloca a forma de pagamento no lugar
DROP TABLE `fin_bandeira`;
UPDATE `sis_funcao` SET `sis_funcao_classe` = 'br.com.opensig.financeiro.client.controlador.comando.ComandoForma' WHERE `sis_funcao_classe` = 'br.com.opensig.financeiro.client.controlador.comando.ComandoBandeira';

# adicionando novos campos ao ECF
ALTER TABLE `com_ecf` ADD COLUMN `com_ecf_mfadicional` VARCHAR(1) NOT NULL  AFTER `com_ecf_codigo` , ADD COLUMN `com_ecf_identificacao` VARCHAR(6) NOT NULL  AFTER `com_ecf_mfadicional` , ADD COLUMN `com_ecf_tipo` VARCHAR(7) NOT NULL  AFTER `com_ecf_identificacao` , ADD COLUMN `com_ecf_marca` VARCHAR(20) NOT NULL  AFTER `com_ecf_tipo` ;
##  editar as ECFs existentes para colocar as novas informacoes
UPDATE `sis_exp_imp` SET `sis_exp_imp_modelo`='<beanio xmlns=\"http://www.beanio.org/2011/01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.beanio.org/2011/01 http://www.beanio.org/2011/01/mapping.xsd\">\n\n	<stream name=\"cat52\" format=\"fixedlength\">\n		<record name=\"ecf\" minOccurs=\"1\" maxOccurs=\"1\" class=\"br.com.opensig.comercial.shared.modelo.ComEcf\">\n			<field name=\"tipo\" rid=\"true\" literal=\"E01\" length=\"3\" ignore=\"true\" />\n			<field name=\"comEcfSerie\" length=\"20\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfMfAdicional\" length=\"1\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfTipo\" length=\"7\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfMarca\" length=\"20\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfModelo\" length=\"20\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"f1\" length=\"24\" ignore=\"true\" />\n			<field name=\"comEcfCaixa\" length=\"3\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"f2\" length=\"68\" ignore=\"true\" />\n		</record>\n		<record name=\"z\" minOccurs=\"1\" maxOccurs=\"1\" class=\"br.com.opensig.comercial.shared.modelo.ComEcfZ\">\n			<field name=\"tipo\" rid=\"true\" literal=\"E12\" length=\"3\" ignore=\"true\" />\n			<field name=\"f1\" length=\"41\" ignore=\"true\" />\r\n			<field name=\"comEcfZUsuario\" length=\"2\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfZCrz\" length=\"6\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfZCooFin\" length=\"6\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfZCro\" length=\"6\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfZMovimento\" length=\"8\" type=\"date\" minOccurs=\"1\" maxOccurs=\"1\" format=\"yyyyMMdd\" />\n			<field name=\"comEcfZEmissao\" length=\"14\" type=\"datetime\" minOccurs=\"1\" maxOccurs=\"1\" format=\"yyyyMMddHHmmss\" />\n			<field name=\"comEcfZBruto\" length=\"14\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"issqn\" length=\"1\" minOccurs=\"1\" maxOccurs=\"1\" />\n		</record>\n		<record name=\"zt\" minOccurs=\"0\" maxOccurs=\"unbounded\" class=\"br.com.opensig.comercial.shared.modelo.ComEcfZTotais\">\n			<field name=\"tipo\" rid=\"true\" literal=\"E13\" length=\"3\" ignore=\"true\" />\n			<field name=\"f1\" length=\"49\" ignore=\"true\" />\n			<field name=\"comEcfZTotaisCodigo\" length=\"7\" minOccurs=\"1\" maxOccurs=\"1\" trim=\"true\" />\n			<field name=\"comEcfZTotaisValor\" length=\"13\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n		</record>\n		<record name=\"venda\" minOccurs=\"0\" maxOccurs=\"unbounded\" class=\"br.com.opensig.comercial.shared.modelo.ComEcfVenda\">\n			<field name=\"tipo\" rid=\"true\" literal=\"E14\" length=\"3\" ignore=\"true\" />\n			<field name=\"f1\" length=\"43\" ignore=\"true\" />\n			<field name=\"comEcfVendaCcf\" length=\"6\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfVendaCoo\" length=\"6\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfVendaData\" length=\"8\" type=\"date\" minOccurs=\"1\" maxOccurs=\"1\" format=\"yyyyMMdd\" />\n			<field name=\"comEcfVendaBruto\" length=\"14\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n                        <field name=\"comEcfVendaDesconto\" length=\"13\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"descIndicador\" length=\"1\" minOccurs=\"1\" maxOccurs=\"1\" trim=\"true\" />\n			<field name=\"comEcfVendaAcrescimo\" length=\"13\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"acresIndicador\" length=\"1\" minOccurs=\"1\" maxOccurs=\"1\" trim=\"true\" />\n			<field name=\"comEcfVendaLiquido\" length=\"14\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"cancelada\" length=\"1\"  minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"f2\" length=\"68\" ignore=\"true\" />\n		</record>\n		<record name=\"produto\" minOccurs=\"0\" maxOccurs=\"unbounded\" class=\"br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto\">\n			<field name=\"tipo\" rid=\"true\" literal=\"E15\" length=\"3\" ignore=\"true\" />\n			<field name=\"f1\" length=\"43\" ignore=\"true\" />\n			<field name=\"comEcfVendaProdutoCoo\" length=\"6\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"f2\" length=\"6\" ignore=\"true\" />\n			<field name=\"comEcfVendaProdutoOrdem\" length=\"3\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfVendaProdutoCodigo\" length=\"14\" minOccurs=\"1\" maxOccurs=\"1\" trim=\"true\" />\n			<field name=\"comEcfVendaProdutoDescricao\" length=\"100\" minOccurs=\"1\" maxOccurs=\"1\" trim=\"true\" />\n			<field name=\"comEcfVendaProdutoQuantidade\" length=\"7\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfVendaProdutoUnd\" length=\"3\" minOccurs=\"1\" maxOccurs=\"1\" trim=\"true\"/>\n			<field name=\"comEcfVendaProdutoBruto\" length=\"8\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfVendaProdutoDesconto\" length=\"8\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfVendaProdutoAcrescimo\" length=\"8\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"comEcfVendaProdutoTotal\" length=\"14\" type=\"double\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"f3\" length=\"7\" ignore=\"true\" />\n			<field name=\"cancelado\" length=\"1\" minOccurs=\"1\" maxOccurs=\"1\" />\n			<field name=\"f4\" length=\"36\" ignore=\"true\" />\n		</record>\r\n		<record name=\"documento\" minOccurs=\"0\" maxOccurs=\"unbounded\" class=\"br.com.opensig.comercial.shared.modelo.ComEcfDocumento\">\r\n			<field name=\"tipo\" rid=\"true\" literal=\"E16\" length=\"3\" ignore=\"true\" />\r\n			<field name=\"f1\" length=\"43\" ignore=\"true\" />\r\n			<field name=\"comEcfDocumentoCoo\" length=\"6\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\r\n			<field name=\"comEcfDocumentoGnf\" length=\"6\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\r\n			<field name=\"comEcfDocumentoGrg\" length=\"6\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\r\n			<field name=\"comEcfDocumentoCdc\" length=\"4\" type=\"int\" minOccurs=\"1\" maxOccurs=\"1\" />\r\n			<field name=\"comEcfDocumentoTipo\" length=\"2\" minOccurs=\"1\" maxOccurs=\"1\" />\r\n			<field name=\"comEcfDocumentoData\" length=\"14\" type=\"datetime\" minOccurs=\"1\" maxOccurs=\"1\" format=\"yyyyMMddHHmmss\" />\r\n		</record>\n	</stream>\n</beanio>' 
WHERE `sis_exp_imp_nome`='CAT-52';

# criando tabela de documento emitidos pelo ECF
CREATE  TABLE `com_ecf_documento` (
  `com_ecf_documento_id` INT NOT NULL AUTO_INCREMENT ,
  `com_ecf_id` INT NOT NULL ,
  `com_ecf_documento_usuario` INT NOT NULL ,
  `com_ecf_documento_coo` INT NOT NULL ,
  `com_ecf_documento_gnf` INT NOT NULL ,
  `com_ecf_documento_grg` INT NOT NULL ,
  `com_ecf_documento_cdc` INT NOT NULL ,
  `com_ecf_documento_tipo` VARCHAR(2) NOT NULL ,
  `com_ecf_documento_data` DATETIME NOT NULL ,
  PRIMARY KEY (`com_ecf_documento_id`) ,
  INDEX `FK_com_ecf_documento_1_idx` USING BTREE (`com_ecf_id` ASC) ,
  CONSTRAINT `FK_com_ecf_documento_1` FOREIGN KEY (`com_ecf_id` ) REFERENCES `com_ecf` (`com_ecf_id` ) ON DELETE NO ACTION ON UPDATE NO ACTION
);

# criando a tabela de nota avulsas
CREATE TABLE `com_ecf_nota` (
  `com_ecf_nota_id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_cliente_id` int(11) NOT NULL,
  `emp_empresa_id` int(11) NOT NULL,
  `com_ecf_nota_serie` varchar(3) NOT NULL,
  `com_ecf_nota_subserie` varchar(3) NOT NULL,
  `com_ecf_nota_numero` int(11) NOT NULL,
  `com_ecf_nota_data` date NOT NULL,
  `com_ecf_nota_bruto` decimal(10,2) NOT NULL,
  `com_ecf_nota_desconto` decimal(10,2) NOT NULL,
  `com_ecf_nota_liquido` decimal(10,2) NOT NULL,
  `com_ecf_nota_pis` decimal(10,2) NOT NULL,
  `com_ecf_nota_cofins` decimal(10,2) NOT NULL,
  `com_ecf_nota_cancelada` tinyint(1) NOT NULL,
  PRIMARY KEY (`com_ecf_nota_id`),
  UNIQUE KEY `UK_com_ecf_nota_1` (`com_ecf_nota_serie`,`com_ecf_nota_subserie`,`com_ecf_nota_numero`) USING BTREE,
  KEY `FK_com_ecf_nota_1` (`emp_cliente_id`) USING BTREE,
  KEY `FK_com_ecf_nota_2` (`emp_empresa_id`) USING BTREE,
  CONSTRAINT `FK_com_ecf_nota_1` FOREIGN KEY (`emp_cliente_id`) REFERENCES `emp_cliente` (`emp_cliente_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_com_ecf_nota_2` FOREIGN KEY (`emp_empresa_id`) REFERENCES `emp_empresa` (`emp_empresa_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

# criando a tabela de produtos das notas avulsas
CREATE TABLE `com_ecf_nota_produto` (
  `com_ecf_nota_produto_id` int(11) NOT NULL AUTO_INCREMENT,
  `com_ecf_nota_id` int(11) NOT NULL,
  `prod_produto_id` int(11) NOT NULL,
  `prod_embalagem_id` int(11) NOT NULL,
  `com_ecf_nota_produto_quantidade` decimal(10,3) NOT NULL,
  `com_ecf_nota_produto_bruto` decimal(10,2) NOT NULL,
  `com_ecf_nota_produto_desconto` decimal(10,2) NOT NULL,
  `com_ecf_nota_produto_liquido` decimal(10,2) NOT NULL,
  `com_ecf_nota_produto_icms` decimal(4,2) NOT NULL,
  `com_ecf_nota_produto_ipi` decimal(4,2) NOT NULL,
  `com_ecf_nota_produto_ordem` int(11) NOT NULL,
  PRIMARY KEY (`com_ecf_nota_produto_id`),
  KEY `FK_com_ecf_nota_produto_1` (`com_ecf_nota_id`) USING BTREE,
  KEY `FK_com_ecf_nota_produto_2` (`prod_produto_id`) USING BTREE,
  KEY `FK_com_ecf_nota_produto_3` (`prod_embalagem_id`) USING BTREE,
  CONSTRAINT `FK_com_ecf_nota_produto_1` FOREIGN KEY (`com_ecf_nota_id`) REFERENCES `com_ecf_nota` (`com_ecf_nota_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_com_ecf_nota_produto_2` FOREIGN KEY (`prod_produto_id`) REFERENCES `prod_produto` (`prod_produto_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_com_ecf_nota_produto_3` FOREIGN KEY (`prod_embalagem_id`) REFERENCES `prod_embalagem` (`prod_embalagem_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

# atualizando a tabela das reducoes Z
ALTER TABLE `com_ecf_z` CHANGE COLUMN `com_ecf_z_coo` `com_ecf_z_coo_fin` INT(6) NOT NULL  , CHANGE COLUMN `com_ecf_z_data` `com_ecf_z_movimento` DATE NOT NULL  , CHANGE COLUMN `com_ecf_z_total` `com_ecf_z_gt` DECIMAL(14,2) NOT NULL  , ADD COLUMN `com_ecf_z_usuario` INT NOT NULL  AFTER `com_ecf_id` , ADD COLUMN `com_ecf_z_coo_ini` INT(6) NOT NULL  AFTER `com_ecf_z_usuario` , ADD COLUMN `com_ecf_z_emissao` DATETIME NOT NULL  AFTER `com_ecf_z_movimento` , ADD COLUMN `com_ecf_z_issqn` TINYINT(1) NOT NULL  AFTER `com_ecf_z_gt` 
, DROP INDEX `SEARCH_2` 
, DROP INDEX `SEARCH_1` 
, DROP INDEX `UNIQUE_1` ;

# atualizando a tabela de vendas do ECF
# 1- remove todas as chaves e indices
ALTER TABLE `com_ecf_venda` DROP FOREIGN KEY `FK_com_ecf_venda_4` , DROP FOREIGN KEY `FK_com_ecf_venda_3` , DROP FOREIGN KEY `FK_com_ecf_venda_2` , DROP FOREIGN KEY `FK_com_ecf_venda_1` ;
ALTER TABLE `com_ecf_venda` 
DROP INDEX `FK_com_ecf_venda_4` 
, DROP INDEX `FK_com_ecf_venda_1` 
, DROP INDEX `FK_com_ecf_venda_3` 
, DROP INDEX `FK_com_ecf_venda_2` 
, DROP INDEX `SEARCH_2` 
, DROP INDEX `SEARCH_1` 
, DROP INDEX `UNIQUE` ;
# 2- modifica os campos, adicionando e removendo
ALTER TABLE `com_ecf_venda` DROP COLUMN `com_ecf_venda_observacao` , CHANGE COLUMN `emp_cliente_id` `emp_cliente_id` INT(11) NULL DEFAULT NULL, CHANGE COLUMN `sis_usuario_id` `sis_usuario_id` INT(11) NOT NULL  AFTER `com_ecf_venda_id` , ADD COLUMN `com_ecf_z_id` INT(11) NULL DEFAULT NULL AFTER `com_ecf_id` , CHANGE COLUMN `com_ecf_venda_data` `com_ecf_venda_data` DATETIME NOT NULL  , ADD COLUMN `com_ecf_venda_ccf` INT(6) NOT NULL  AFTER `fin_receber_id` , ADD COLUMN `com_ecf_venda_acrescimo` DECIMAL(10,2) NOT NULL  AFTER `com_ecf_venda_desconto` ;
# 3- adiciona as chaves e indices novos
ALTER TABLE `com_ecf_venda` 
  ADD CONSTRAINT `FK_com_ecf_venda_1` FOREIGN KEY (`sis_usuario_id` ) REFERENCES `sis_usuario` (`sis_usuario_id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_com_ecf_venda_2` FOREIGN KEY (`com_ecf_id` ) REFERENCES `com_ecf` (`com_ecf_id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_com_ecf_venda_3` FOREIGN KEY (`com_ecf_z_id` ) REFERENCES `com_ecf_z` (`com_ecf_z_id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_com_ecf_venda_4` FOREIGN KEY (`emp_cliente_id` ) REFERENCES `emp_cliente` (`emp_cliente_id` ) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_com_ecf_venda_5` FOREIGN KEY (`fin_receber_id` ) REFERENCES `fin_receber` (`fin_receber_id` ) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD INDEX `FK_com_ecf_venda_1_idx` USING BTREE (`sis_usuario_id` ASC), 
  ADD INDEX `FK_com_ecf_venda_2_idx` USING BTREE (`com_ecf_id` ASC), 
  ADD INDEX `FK_com_ecf_venda_3_idx` USING BTREE (`com_ecf_z_id` ASC), 
  ADD INDEX `FK_com_ecf_venda_4_idx` USING BTREE (`emp_cliente_id` ASC),
  ADD INDEX `FK_com_ecf_venda_5_idx` USING BTREE (`fin_receber_id` ASC) ;

# seta a Z das vendas antigas
UPDATE `com_ecf_venda` SET `com_ecf_z_id` = (SELECT `com_ecf_z_id` FROM `com_ecf_z` WHERE com_ecf_z.com_ecf_id = com_ecf_venda.com_ecf_id AND com_ecf_z_movimento = DATE_FORMAT(com_ecf_venda_data,'%Y-%m-%d')) WHERE `com_ecf_z_id` IS NULL;

# marca o Z como NOT NULL
ALTER TABLE `com_ecf_venda` DROP FOREIGN KEY `FK_com_ecf_venda_3` ;
ALTER TABLE `com_ecf_venda` CHANGE COLUMN `com_ecf_z_id` `com_ecf_z_id` INT(11) NOT NULL  , 
  ADD CONSTRAINT `FK_com_ecf_venda_3`
  FOREIGN KEY (`com_ecf_z_id` )
  REFERENCES `com_ecf_z` (`com_ecf_z_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

# adicionando novo campo pra tributacao e igualar ao ECF
ALTER TABLE `prod_tributacao` ADD COLUMN `prod_tributacao_ecf` VARCHAR(7) NOT NULL  AFTER `prod_tributacao_cfop` ;
UPDATE prod_tributacao SET prod_tributacao_ecf = '01T1700' WHERE prod_tributacao_cst = '00' OR prod_tributacao_cson = '102';
UPDATE prod_tributacao SET prod_tributacao_ecf = 'F1' WHERE prod_tributacao_cst = '10' OR prod_tributacao_cst = '60' OR prod_tributacao_cson = '202' OR prod_tributacao_cson = '500';
UPDATE prod_tributacao SET prod_tributacao_ecf = 'I1' WHERE prod_tributacao_cst = '30' OR prod_tributacao_cst = '40' OR prod_tributacao_cson = '103';
UPDATE prod_tributacao SET prod_tributacao_ecf = 'N1' WHERE prod_tributacao_cst = '41' OR prod_tributacao_cson = '400';

# adicionando novos blocos do SPED
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC110',`fis_sped_bloco_nivel`='3' WHERE `fis_sped_bloco_registro`='C110';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC425',`fis_sped_bloco_nivel`='5' WHERE `fis_sped_bloco_registro`='C425';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC300',`fis_sped_bloco_nivel`='2' WHERE `fis_sped_bloco_registro`='C300';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC310',`fis_sped_bloco_nivel`='3' WHERE `fis_sped_bloco_registro`='C310';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC320',`fis_sped_bloco_nivel`='3' WHERE `fis_sped_bloco_registro`='C320';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC321',`fis_sped_bloco_nivel`='4' WHERE `fis_sped_bloco_registro`='C321';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC350',`fis_sped_bloco_nivel`='2' WHERE `fis_sped_bloco_registro`='C350';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC370',`fis_sped_bloco_nivel`='3' WHERE `fis_sped_bloco_registro`='C370';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoC.RegistroC390',`fis_sped_bloco_nivel`='3' WHERE `fis_sped_bloco_registro`='C390';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.bloco1.Registro1010',`fis_sped_bloco_obrigatorio`='1' WHERE `fis_sped_bloco_registro`='1010';

# removendo campos da lista do SPED sem uso
ALTER TABLE `fis_sped_fiscal` DROP COLUMN `fis_sped_fiscal_ecf` , DROP COLUMN `fis_sped_fiscal_vendas` , DROP COLUMN `fis_sped_fiscal_frete` , DROP COLUMN `fis_sped_fiscal_compras` ;
