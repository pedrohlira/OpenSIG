# Atualizacoes da versao 1.1.0

# Arrumando a classe que executa o bloco
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoD.RegistroD500' WHERE `fis_sped_bloco_id`='106';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoD.RegistroD590' WHERE `fis_sped_bloco_id`='109';

# Arrumando a funcao que contem a exportacao/importacao
UPDATE `sis_exp_imp` SET `sis_exp_imp_funcao`='br.com.opensig.fiscal.client.controlador.comando.ComandoSped' WHERE `sis_exp_imp_funcao`='br.com.opensig.fiscal.client.controlador.comando.ComandoSpedFiscal';
UPDATE `sis_exp_imp` SET `sis_exp_imp_descricao`='Importação da compra diretamente pela NFe emitida.', `sis_exp_imp_classe`='br.com.opensig.comercial.server.acao.ImportarCompra' WHERE `sis_exp_imp_id`='1';
INSERT INTO `sis_exp_imp` (`sis_exp_imp_nome`, `sis_exp_imp_extensoes`, `sis_exp_imp_descricao`, `sis_exp_imp_imagem`, `sis_exp_imp_classe`, `sis_exp_imp_funcao`, `sis_exp_imp_tipo`, `sis_exp_imp_modelo`) VALUES ('NFe', 'xml', 'Importação da venda diretamente pela NFe emitida.', 'nfe.png', 'br.com.opensig.comercial.server.acao.ImportarVenda', 'br.com.opensig.comercial.client.controlador.comando.ComandoVenda', 'I', ' ');

# Arrumando os valores dos itens das vendas do ecf que tiveram descontos
UPDATE com_ecf_venda_produto SET com_ecf_venda_produto_liquido = com_ecf_venda_produto_bruto - (com_ecf_venda_produto_bruto * com_ecf_venda_produto_desconto / 100) WHERE com_ecf_venda_produto_desconto > 0.00;
UPDATE com_ecf_venda_produto SET com_ecf_venda_produto_liquido = 0.01 WHERE com_ecf_venda_produto_liquido = 0.00;
UPDATE com_ecf_venda_produto SET com_ecf_venda_produto_total = com_ecf_venda_produto_liquido * com_ecf_venda_produto_quantidade WHERE com_ecf_venda_produto_desconto > 0.00;

# Arrumando os dados de cadastros dos clientes oriundos do OpenPDV
UPDATE `emp_entidade` SET emp_entidade_nome1 = 'CONSUMIDOR' WHERE emp_entidade_nome1 = '' AND emp_entidade_observacao LIKE 'IMPORTADO DO OPENPDV%';
UPDATE `emp_entidade` SET emp_entidade_nome2 = 'CONSUMIDOR' WHERE emp_entidade_observacao LIKE 'IMPORTADO DO OPENPDV%';
ALTER TABLE `emp_entidade` ADD COLUMN `emp_entidade_data` DATETIME NOT NULL  AFTER `emp_entidade_ativo` ;
UPDATE `emp_entidade` SET `emp_entidade_data` = CURRENT_DATE();

INSERT INTO emp_endereco
(`emp_entidade_id`,`emp_endereco_tipo_id`,`emp_municipio_id`,`emp_endereco_logradouro`,`emp_endereco_numero`,`emp_endereco_bairro`,`emp_endereco_complemento`,`emp_endereco_cep`)
SELECT emp_entidade_id, 2, 1695, 'NAO INFORMADO', 0, 'NAO INFORMADO', '', '00000000' FROM emp_entidade WHERE emp_entidade_observacao LIKE 'IMPORTADO DO OPENPDV%';

INSERT INTO emp_contato
(`emp_entidade_id`,`emp_contato_tipo_id`,`emp_contato_descricao`,`emp_contato_pessoa`)
SELECT emp_entidade_id, 1, '(00) 0000-0000', emp_entidade_nome1 FROM emp_entidade WHERE emp_entidade_observacao LIKE 'IMPORTADO DO OPENPDV%';

# Alterando a tabele z_totais para ter o campo codigo unico
ALTER TABLE `com_ecf_z_totais` 
ADD UNIQUE INDEX `UK_com_ecf_z_totais_1` (`com_ecf_z_id` ASC, `com_ecf_z_totais_codigo` ASC) ;

# Removendo o campo de incentivo dos produtos e deletando a tabela de incentivo fiscal e sua funcao
ALTER TABLE `prod_produto` DROP COLUMN `prod_produto_incentivo`, DROP COLUMN `prod_produto_sinc` ;
DROP TABLE `fis_incentivo_estado`;
DELETE FROM `sis_funcao` WHERE `sis_funcao_classe`='br.com.opensig.fiscal.client.controlador.comando.ComandoIncentivo';

# Atualizando a tabela de origem dos produtos
ALTER TABLE `prod_origem` CHANGE COLUMN `prod_origem_descricao` `prod_origem_descricao` VARCHAR(255) NOT NULL  , ADD COLUMN `prod_origem_valor` INT NOT NULL  AFTER `prod_origem_id` 
, DROP INDEX `UNIQUE` ;
UPDATE `prod_origem` SET `prod_origem_descricao`='Nacional, exceto as indicadas nos códigos 3 a 5	' WHERE `prod_origem_id`='1';
UPDATE `prod_origem` SET `prod_origem_descricao`='Estrangeira - Importação direta, exceto a indicada no código 6' WHERE `prod_origem_id`='2';
UPDATE `prod_origem` SET `prod_origem_descricao`='Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7' WHERE `prod_origem_id`='3';
INSERT INTO `prod_origem` (`prod_origem_id`, `prod_origem_descricao`) VALUES ('4', 'Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% (quarenta por cento)');
INSERT INTO `prod_origem` (`prod_origem_id`, `prod_origem_descricao`) VALUES ('5', 'Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam o Decreto-Lei');
INSERT INTO `prod_origem` (`prod_origem_id`, `prod_origem_descricao`) VALUES ('6', 'Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40% (quarenta por cento)');
INSERT INTO `prod_origem` (`prod_origem_id`, `prod_origem_descricao`) VALUES ('7', 'Estrangeira - Importação direta, sem similar nacional, constante em lista de Resolução CAMEX');
INSERT INTO `prod_origem` (`prod_origem_id`, `prod_origem_descricao`) VALUES ('8', 'Estrangeira - Adquirida no mercado interno, sem similar nacional, constante em lista de Resolução CAMEX');
UPDATE `prod_origem` SET `prod_origem_valor` = `prod_origem_id` - 1, `prod_origem_descricao` = UPPER(`prod_origem_descricao`);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('4', 'br.com.opensig.produto.client.controlador.comando.ComandoOrigem', '9', '0', '1');

# Adicionando o cascate a tabela de estoque e composicao de produtos
ALTER TABLE `prod_estoque` DROP FOREIGN KEY `FK_prod_estoque_2` ;
ALTER TABLE `prod_estoque` 
  ADD CONSTRAINT `FK_prod_estoque_2`
  FOREIGN KEY (`prod_produto_id` )
  REFERENCES `prod_produto` (`prod_produto_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `prod_composicao` DROP FOREIGN KEY `FK_prod_composicao_1` ;
ALTER TABLE `prod_composicao` 
  ADD CONSTRAINT `FK_prod_composicao_1`
  FOREIGN KEY (`prod_produto_principal` )
  REFERENCES `prod_produto` (`prod_produto_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

# Criando as tabelas que vao tratar da funcionalidade de grade dos produtos
CREATE  TABLE `prod_grade_tipo` (
  `prod_grade_tipo_id` INT NOT NULL AUTO_INCREMENT ,
  `prod_grade_tipo_nome` VARCHAR(50) NOT NULL ,
  `prod_grade_tipo_opcao` VARCHAR(1) NOT NULL ,
  PRIMARY KEY (`prod_grade_tipo_id`) );

CREATE  TABLE `prod_grade` (
  `prod_grade_id` INT NOT NULL AUTO_INCREMENT ,
  `prod_produto_id` INT NOT NULL ,
  `prod_grade_barra` VARCHAR(14) NOT NULL ,
  `prod_grade_tamanho` VARCHAR(50) NOT NULL ,
  `prod_grade_cor` VARCHAR(50) NOT NULL ,
  `prod_grade_opcao` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`prod_grade_id`) ,
  UNIQUE INDEX `prod_grade_barra_UNIQUE` (`prod_grade_barra` ASC) ,
  INDEX `FK_prod_grade_idx` USING BTREE (`prod_produto_id` ASC) ,
  CONSTRAINT `FK_prod_grade_1`
    FOREIGN KEY (`prod_produto_id` )
    REFERENCES `prod_produto` (`prod_produto_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE  TABLE `prod_estoque_grade` (
  `prod_estoque_grade_id` INT NOT NULL AUTO_INCREMENT ,
  `emp_empresa_id` INT NOT NULL ,
  `prod_grade_id` INT NOT NULL ,
  `prod_estoque_grade_quantidade` DECIMAL(10,4) NOT NULL ,
  PRIMARY KEY (`prod_estoque_grade_id`) ,
  INDEX `FK_ prod_estoque_grade_1_idx` USING BTREE (`emp_empresa_id` ASC) ,
  INDEX `FK_ prod_estoque_grade_2_idx` USING BTREE (`prod_grade_id` ASC) ,
  CONSTRAINT `FK_ prod_estoque_grade_1`
    FOREIGN KEY (`emp_empresa_id` )
    REFERENCES `emp_empresa` (`emp_empresa_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ prod_estoque_grade_2`
    FOREIGN KEY (`prod_grade_id` )
    REFERENCES `prod_grade` (`prod_grade_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE);

# Adicionando a funcao de tipo de grade ao menu
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('4', 'br.com.opensig.produto.client.controlador.comando.ComandoGrade', '4', '1', '1');
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('4', 'br.com.opensig.produto.client.controlador.comando.ComandoGradeTipo', '4', '2', '1');

# Adicionando novo campo para produto da venda, assim podendo identificar se caso tenha grade, qual o sub-produto
ALTER TABLE `com_venda_produto` ADD COLUMN `com_venda_produto_barra` VARCHAR(14) NULL DEFAULT NULL  AFTER `prod_embalagem_id` ;
UPDATE `com_venda_produto`, `prod_produto` SET `com_venda_produto_barra` = `prod_produto_barra`
WHERE `com_venda_produto`.`prod_produto_id` = `prod_produto`.`prod_produto_id`;

# Adicionando novo campo para produto da nota, assim podendo identificar se caso tenha grade, qual o sub-produto
ALTER TABLE `com_ecf_nota_produto` ADD COLUMN `com_ecf_nota_produto_barra` VARCHAR(14) NULL DEFAULT NULL  AFTER `prod_embalagem_id` ;
UPDATE `com_ecf_nota_produto`, `prod_produto` SET `com_ecf_nota_produto_barra` = `prod_produto_barra`
WHERE `com_ecf_nota_produto`.`prod_produto_id` = `prod_produto`.`prod_produto_id`;