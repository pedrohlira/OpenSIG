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

# Inserindo os valores do sped contribuicao no sis_configuracao
INSERT INTO `sis_configuracao` (`emp_empresa_id`, `sis_configuracao_chave`, `sis_configuracao_valor`, `sis_configuracao_descricao`, `sis_configuracao_ativo`, `sis_configuracao_sistema`) VALUES ('1', 'SPED.CONTRIBUICAO.0000.COD_VER', '003', 'CODIGO DO LAYOUT DA VERSAO DO SPED CONTRIBUICAO ATUAL', '1', '0');
INSERT INTO `sis_configuracao` (`emp_empresa_id`, `sis_configuracao_chave`, `sis_configuracao_valor`, `sis_configuracao_descricao`, `sis_configuracao_ativo`, `sis_configuracao_sistema`) VALUES ('1', 'SPED.CONTRIBUICAO.0000.IND_ATIV', '2', 'Indicador de tipo de atividade preponderante: 0 – Industrial ou equiparado a industrial;1 – Prestador de serviços;2 - Atividade de comércio;3 – Atividade financeira;4 – Atividade imobiliária;9 – Outros.', '1', '0');
INSERT INTO `sis_configuracao` (`emp_empresa_id`, `sis_configuracao_chave`, `sis_configuracao_valor`, `sis_configuracao_descricao`, `sis_configuracao_ativo`, `sis_configuracao_sistema`) VALUES ('1', 'SPED.CONTRIBUICAO.0000.IND_REG_CUM', '2', 'Código indicador do critério de escrituração e apuração adotado:1 – Regime de Caixa – Escrituração consolidada (Registro F500);2 – Regime de Competência - Escrituração consolidada (Registro F550);9 – Regime de Competência - Escrituração detalhada.', '1', '0');
UPDATE `sis_configuracao` SET `sis_configuracao_chave`='SPED.0100.ID_FUNCIONARIO' WHERE `sis_configuracao_chave`='SPED.FISCAL.0100.ID_FUNCIONARIO';
UPDATE `sis_configuracao` SET `sis_configuracao_chave`='SPED.0000.SUFRAMA' WHERE `sis_configuracao_chave`='SPED.FISCAL.0000.SUFRAMA';

# Organizando os blocos e adicionando os da contribuicao
DELETE FROM `fis_sped_bloco` WHERE `fis_sped_bloco_classe` IS NULL;
ALTER TABLE `fis_sped_bloco` DROP COLUMN `fis_sped_bloco_descricao`, DROP COLUMN `fis_sped_bloco_obrigatorio` ;

UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='1' WHERE `fis_sped_bloco_id`='1';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='2' WHERE `fis_sped_bloco_id`='2';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='3' WHERE `fis_sped_bloco_id`='3';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='4' WHERE `fis_sped_bloco_id`='5';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='5' WHERE `fis_sped_bloco_id`='6';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='6' WHERE `fis_sped_bloco_id`='8';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='7' WHERE `fis_sped_bloco_id`='9';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='8' WHERE `fis_sped_bloco_id`='12';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='9' WHERE `fis_sped_bloco_id`='15';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='10' WHERE `fis_sped_bloco_id`='16';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='11' WHERE `fis_sped_bloco_id`='18';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='12' WHERE `fis_sped_bloco_id`='20';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='13' WHERE `fis_sped_bloco_id`='21';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='14' WHERE `fis_sped_bloco_id`='22';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='15' WHERE `fis_sped_bloco_id`='24';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='16' WHERE `fis_sped_bloco_id`='32';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='17' WHERE `fis_sped_bloco_id`='33';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='18' WHERE `fis_sped_bloco_id`='34';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='19' WHERE `fis_sped_bloco_id`='36';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='20' WHERE `fis_sped_bloco_id`='46';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='21' WHERE `fis_sped_bloco_id`='49';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='22' WHERE `fis_sped_bloco_id`='50';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='23' WHERE `fis_sped_bloco_id`='51';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='24' WHERE `fis_sped_bloco_id`='52';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='25' WHERE `fis_sped_bloco_id`='53';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='26' WHERE `fis_sped_bloco_id`='54';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='27' WHERE `fis_sped_bloco_id`='55';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='28' WHERE `fis_sped_bloco_id`='56';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='29' WHERE `fis_sped_bloco_id`='57';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='30' WHERE `fis_sped_bloco_id`='58';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='31' WHERE `fis_sped_bloco_id`='59';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='32' WHERE `fis_sped_bloco_id`='60';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='33' WHERE `fis_sped_bloco_id`='61';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='34' WHERE `fis_sped_bloco_id`='62';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='35' WHERE `fis_sped_bloco_id`='63';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='36' WHERE `fis_sped_bloco_id`='65';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='37' WHERE `fis_sped_bloco_id`='67';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='38' WHERE `fis_sped_bloco_id`='79';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='39' WHERE `fis_sped_bloco_id`='80';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='40' WHERE `fis_sped_bloco_id`='81';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='41' WHERE `fis_sped_bloco_id`='92';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='42' WHERE `fis_sped_bloco_id`='106';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='43' WHERE `fis_sped_bloco_id`='109';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='44' WHERE `fis_sped_bloco_id`='116';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='45' WHERE `fis_sped_bloco_id`='117';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='46' WHERE `fis_sped_bloco_id`='135';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='47' WHERE `fis_sped_bloco_id`='136';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='48' WHERE `fis_sped_bloco_id`='142';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='49' WHERE `fis_sped_bloco_id`='143';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='50' WHERE `fis_sped_bloco_id`='144';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='51' WHERE `fis_sped_bloco_id`='145';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='52' WHERE `fis_sped_bloco_id`='146';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='53' WHERE `fis_sped_bloco_id`='147';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='54' WHERE `fis_sped_bloco_id`='174';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='55' WHERE `fis_sped_bloco_id`='175';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='56' WHERE `fis_sped_bloco_id`='176';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='57' WHERE `fis_sped_bloco_id`='177';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='58' WHERE `fis_sped_bloco_id`='179';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_ordem`='59' WHERE `fis_sped_bloco_id`='180';

INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`,`fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '0', '0000', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco0.Registro0000', '1', '0');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`,`fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '0', '0001', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco0.Registro0001', '2', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`,`fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '0', '0100', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco0.Registro0100', '3', '2');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '0', '0110', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco0.Registro0110', '4', '2');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '0', '0120', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco0.Registro0120', '5', '2');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '0', '0140', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco0.Registro0140', '6', '2');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '0', '0990', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco0.Registro0990', '7', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'A', 'A001', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoA.RegistroA001', '8', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'A', 'A990', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoA.RegistroA990', '9', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'C', 'C001', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoC.RegistroC001', '10', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'C', 'C990', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoC.RegistroC990', '11', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'D', 'D001', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoD.RegistroD001', '12', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'D', 'D990', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoD.RegistroD990', '13', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'F', 'F001', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoF.RegistroF001', '14', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'F', 'F010', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoF.RegistroF010', '15', '2');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'F', 'F500', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoF.RegistroF500', '16', '3');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'F', 'F525', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoF.RegistroF525', '17', '3');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'F', 'F550', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoF.RegistroF550', '18', '3');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'F', 'F990', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoF.RegistroF990', '19', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'M', 'M001', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoM.RegistroM001', '20', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'M', 'M990', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoM.RegistroM990', '21', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'P', 'P001', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoP.RegistroP001', '22', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', 'P', 'P990', 'br.com.opensig.fiscal.server.sped.contribuicao.blocoP.RegistroP990', '23', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '1', '1001', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco1.Registro1001', '24', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '1', '1900', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco1.Registro1900', '25', '2');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '1', '1990', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco1.Registro1990', '26', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '9', '9001', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco9.Registro9001', '27', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '9', '9900', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco9.Registro9900', '28', '2');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '9', '9990', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco9.Registro9990', '29', '1');
INSERT INTO `fis_sped_bloco` (`fis_sped_bloco_tipo`, `fis_sped_bloco_letra`, `fis_sped_bloco_registro`, `fis_sped_bloco_classe`, `fis_sped_bloco_ordem`, `fis_sped_bloco_nivel`) VALUES ('CONTRIBUICOES', '9', '9999', 'br.com.opensig.fiscal.server.sped.contribuicao.bloco9.Registro9999', '30', '0');

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
ALTER TABLE `com_ecf_venda` 
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

# Adicionando o campo de vendedor na venda.
ALTER TABLE `com_venda` ADD COLUMN `sis_vendedor_id` INT NOT NULL  AFTER `sis_usuario_id` ;
UPDATE `com_venda` SET `sis_vendedor_id` = `sis_usuario_id`;
ALTER TABLE `com_venda` 
DROP INDEX `FK_com_venda_6` 
, ADD INDEX `FK_com_venda_7` USING BTREE (`com_natureza_id` ASC) ;
ALTER TABLE `com_venda` 
  ADD CONSTRAINT `FK_com_venda_6`
  FOREIGN KEY (`sis_vendedor_id` )
  REFERENCES `sis_usuario` (`sis_usuario_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_com_venda_6` USING BTREE (`sis_vendedor_id` ASC) ;
