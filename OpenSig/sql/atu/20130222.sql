# Atualizacoes da versao 1.1.1

# Arrumando o valor das vendas canceladas das ecfs
UPDATE com_ecf_venda SET com_ecf_venda_bruto = 
(SELECT SUM(com_ecf_venda_produto_bruto) FROM com_ecf_venda_produto WHERE com_ecf_venda.com_ecf_venda_id = com_ecf_venda_produto.com_ecf_venda_id)
WHERE com_ecf_venda_cancelada = 1 AND com_ecf_venda_fechada = 0 AND com_ecf_venda_bruto = 0.00;

# Adicionando o campo de observacao ao comercial ecf venda
ALTER TABLE `com_ecf_venda` ADD COLUMN `com_ecf_venda_observacao` VARCHAR(255) NULL  AFTER `com_ecf_venda_cancelada`;

# Adiconando a forma de pagamento troca
INSERT INTO `fin_forma` (`fin_forma_descricao`, `fin_forma_tef`, `fin_forma_vinculado`, `fin_forma_debito`, `fin_forma_rede`, `fin_forma_pagar`, `fin_forma_receber`) VALUES ('TROCA', '0', '0', '0', 'LOJA', '0', '1');

# Modificando o nome e campos da tabela do sped fiscal, alem dos itens relacionados
ALTER TABLE `fis_sped_bloco` DROP COLUMN `fis_sped_bloco_pis_cofins` , DROP COLUMN `fis_sped_bloco_icms_ipi`, ADD COLUMN `fis_sped_bloco_tipo` VARCHAR(20) NOT NULL  AFTER `fis_sped_bloco_id` ;
UPDATE fis_sped_bloco SET fis_sped_bloco_classe = REPLACE(fis_sped_bloco_classe, '.sped.', '.sped.fiscal.'), fis_sped_bloco_tipo = 'ICMS_IPI';
UPDATE sis_configuracao SET sis_configuracao_chave = REPLACE(sis_configuracao_chave, 'SPED.', 'SPED.FISCAL.');

# Atualizacao do cascate das tabelas
ALTER TABLE `com_ecf_documento` DROP FOREIGN KEY `FK_com_ecf_documento_1` ;
ALTER TABLE `com_ecf_documento` 
  ADD CONSTRAINT `FK_com_ecf_documento_1`
  FOREIGN KEY (`com_ecf_id` )
  REFERENCES `com_ecf` (`com_ecf_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `com_ecf_nota_produto` DROP FOREIGN KEY `FK_com_ecf_nota_produto_1` ;
ALTER TABLE `com_ecf_nota_produto` 
  ADD CONSTRAINT `FK_com_ecf_nota_produto_1`
  FOREIGN KEY (`com_ecf_nota_id` )
  REFERENCES `com_ecf_nota` (`com_ecf_nota_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `com_ecf_z` DROP FOREIGN KEY `FK_com_ecf_z_1` ;
ALTER TABLE `com_ecf_z` 
  ADD CONSTRAINT `FK_com_ecf_z_1`
  FOREIGN KEY (`com_ecf_id` )
  REFERENCES `com_ecf` (`com_ecf_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `com_ecf_z_totais` DROP FOREIGN KEY `FK_com_ecf_z_totais_1` ;
ALTER TABLE `com_ecf_z_totais` 
  ADD CONSTRAINT `FK_com_ecf_z_totais_1`
  FOREIGN KEY (`com_ecf_z_id` )
  REFERENCES `com_ecf_z` (`com_ecf_z_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `com_ecf_venda` DROP FOREIGN KEY `FK_com_ecf_venda_3` ;
ALTER TABLE `opensig`.`com_ecf_venda` 
  ADD CONSTRAINT `FK_com_ecf_venda_3`
  FOREIGN KEY (`com_ecf_z_id` )
  REFERENCES `com_ecf_z` (`com_ecf_z_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `com_ecf_venda_produto` DROP FOREIGN KEY `FK_com_ecf_venda_produto_1` ;
ALTER TABLE `com_ecf_venda_produto` 
  ADD CONSTRAINT `FK_com_ecf_venda_produto_1`
  FOREIGN KEY (`com_ecf_venda_id` )
  REFERENCES `com_ecf_venda` (`com_ecf_venda_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `com_valor_arredonda` DROP FOREIGN KEY `FK_com_valor_arredonda_1` ;
ALTER TABLE `com_valor_arredonda` 
  ADD CONSTRAINT `FK_com_valor_arredonda_1`
  FOREIGN KEY (`com_valor_produto_id` )
  REFERENCES `com_valor_produto` (`com_valor_produto_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `emp_contato` DROP FOREIGN KEY `FK_emp_contato_1` ;
ALTER TABLE `emp_contato` 
  ADD CONSTRAINT `FK_emp_contato_1`
  FOREIGN KEY (`emp_entidade_id` )
  REFERENCES `emp_entidade` (`emp_entidade_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `sis_favorito_grafico` DROP FOREIGN KEY `FK_sis_favorito_grafico_1` ;
ALTER TABLE `sis_favorito_grafico` 
  ADD CONSTRAINT `FK_sis_favorito_grafico_1`
  FOREIGN KEY (`sis_favorito_id` )
  REFERENCES `sis_favorito` (`sis_favorito_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `sis_favorito_grupo` DROP FOREIGN KEY `FK_sis_favorito_grupo_1` ;
ALTER TABLE `sis_favorito_grupo` 
  ADD CONSTRAINT `FK_sis_favorito_grupo_1`
  FOREIGN KEY (`sis_favorito_id` )
  REFERENCES `sis_favorito` (`sis_favorito_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `sis_favorito_portal` DROP FOREIGN KEY `FK_sis_favorito_portal_1` ;
ALTER TABLE `sis_favorito_portal` 
  ADD CONSTRAINT `FK_sis_favorito_portal_1`
  FOREIGN KEY (`sis_favorito_id` )
  REFERENCES `sis_favorito` (`sis_favorito_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `sis_favorito_usuario` DROP FOREIGN KEY `FK_sis_favorito_usuario_1` ;
ALTER TABLE `sis_favorito_usuario` 
  ADD CONSTRAINT `FK_sis_favorito_usuario_1`
  FOREIGN KEY (`sis_favorito_id` )
  REFERENCES `sis_favorito` (`sis_favorito_id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;

# Modificando a conta
ALTER TABLE `fin_conta` DROP FOREIGN KEY `FK_fin_conta_2` ;
ALTER TABLE `fin_conta` DROP COLUMN `emp_empresa_id`
, DROP INDEX `FK_fin_conta_2` ;

# Adicionando a conta para os pagamentos e recebimentos
ALTER TABLE `fin_pagamento` ADD COLUMN `fin_conta_id` INT NULL DEFAULT NULL  AFTER `fin_forma_id` , 
  ADD CONSTRAINT `FK_fin_pagamento_3`
  FOREIGN KEY (`fin_conta_id` )
  REFERENCES `fin_conta` (`fin_conta_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_fin_pagamento_3_idx` USING BTREE (`fin_conta_id` ASC) ;
UPDATE fin_pagamento, fin_pagar SET fin_pagamento.fin_conta_id = fin_pagar.fin_conta_id
WHERE fin_pagamento.fin_pagar_id = fin_pagar.fin_pagar_id;

ALTER TABLE `fin_recebimento` ADD COLUMN `fin_conta_id` INT NULL DEFAULT NULL  AFTER `fin_forma_id` , 
  ADD CONSTRAINT `FK_fin_recebimento_3`
  FOREIGN KEY (`fin_conta_id` )
  REFERENCES `fin_conta` (`fin_conta_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_fin_recebimento_3_idx` USING BTREE (`fin_conta_id` ASC) ;
UPDATE fin_recebimento, fin_receber SET fin_recebimento.fin_conta_id = fin_receber.fin_conta_id
WHERE fin_recebimento.fin_receber_id = fin_receber.fin_receber_id;

# Removendo a conta do Pagar e Receber
ALTER TABLE `fin_pagar` DROP FOREIGN KEY `FK_fin_pagar_3` ;
ALTER TABLE `fin_pagar` DROP COLUMN `fin_conta_id` 
, DROP INDEX `FK_fin_pagar_3` ;

ALTER TABLE `fin_receber` DROP FOREIGN KEY `FK_fin_receber_3` ;
ALTER TABLE `fin_receber` DROP COLUMN `fin_conta_id` 
, DROP INDEX `FK_fin_receber_3` ;

# deletando os retornos e remessas
DROP TABLE `fin_retorno`;
DROP TABLE `fin_remessa`;
DELETE FROM `sis_funcao` WHERE `sis_modulo_id` = 7 AND `sis_funcao_ordem` IN (3,4,5);
DELETE FROM `sis_configuracao` WHERE `sis_configuracao_chave` = 'conta.padrao';
