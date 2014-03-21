# Atualizacoes da versao 1.1.6

# Modificando os tamanhos dos campos de razao e fantasia dos cadastros
ALTER TABLE `emp_entidade` CHANGE COLUMN `emp_entidade_nome1` `emp_entidade_nome1` VARCHAR(60) NOT NULL  , CHANGE COLUMN `emp_entidade_nome2` `emp_entidade_nome2` VARCHAR(60) NOT NULL  ;

# Adicionando o campo de venda na troca, para que a venda possa ter mais de uma troca
ALTER TABLE `com_troca` 
ADD COLUMN `com_ecf_venda_id` INT NULL DEFAULT NULL AFTER `emp_empresa_id`,
ADD INDEX `FK_com_troca_3_idx` (`com_ecf_venda_id` ASC),
ADD CONSTRAINT `FK_com_troca_3`
  FOREIGN KEY (`com_ecf_venda_id`)
  REFERENCES `com_ecf_venda` (`com_ecf_venda_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

# Atualizando as trocas das vendas
UPDATE `com_troca` AS t SET t.com_ecf_venda_id = (SELECT v.com_ecf_venda_id FROM com_ecf_venda AS v WHERE NOT v.com_troca_id IS NULL AND v.com_troca_id = t.com_troca_id);

# Removendo o campo de troca da venda
ALTER TABLE `com_ecf_venda` 
DROP FOREIGN KEY `FK_com_ecf_venda_8`,
DROP COLUMN `com_troca_id`,
DROP INDEX `FK_com_ecf_venda_8_idx`,
CHANGE COLUMN `com_ecf_z_id` `com_ecf_z_id` INT(11) NULL DEFAULT NULL ;

# Adicionando o campo de ecf_z nos documentos
ALTER TABLE `com_ecf_documento` ADD COLUMN `com_ecf_z_id` INT NOT NULL AFTER `com_ecf_id`;
# Fazer para cada ecfID separados pois tem um grande volume e pode dar timeout e verificar se tem algum registro na ecf_documento com data errada.
UPDATE com_ecf_documento AS d SET d.com_ecf_z_id = (SELECT z.com_ecf_z_id FROM com_ecf_z AS z WHERE z.com_ecf_id = 7 AND DATE(d.com_ecf_documento_data) = z.com_ecf_z_movimento) where d.com_ecf_id = 7;
ALTER TABLE `com_ecf_documento` ADD INDEX `FK_com_ecf_documento_2_idx` USING BTREE (`com_ecf_z_id` ASC);
ALTER TABLE `com_ecf_documento` ADD CONSTRAINT `FK_com_ecf_documento_2` FOREIGN KEY (`com_ecf_z_id`) REFERENCES `com_ecf_z` (`com_ecf_z_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

# Cheque sem vinculo de TEF
UPDATE `fin_forma` SET `fin_forma_tef`='0', `fin_forma_vinculado`='0', `fin_forma_rede`='LOJA' WHERE `fin_forma_descricao`='CHEQUE';
