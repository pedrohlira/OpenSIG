# Atualizacoes da versao 1.2.1

# Criando a tabela para cadastro dos cartoes presentes
CREATE TABLE `fin_cartao_presente` (
  `fin_cartao_presente_id` INT NOT NULL AUTO_INCREMENT,
  `fin_cartao_presente_numero` VARCHAR(14) NOT NULL,
  `fin_cartao_presente_valor` DECIMAL(10,2) NOT NULL,
  `fin_cartao_presente_ativo` TINYINT(1) NOT NULL,
  PRIMARY KEY (`fin_cartao_presente_id`));

# Criando a tabela de auditoria dos cartoes presentes
CREATE TABLE `fin_cartao_auditoria` (
  `fin_cartao_auditoria_id` INT NOT NULL AUTO_INCREMENT,
  `fin_cartao_presente_id` INT NOT NULL,
  `sis_usuario_id` INT NOT NULL,
  `fin_cartao_auditoria_data` DATETIME NOT NULL,
  `fin_cartao_auditoria_acao` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`fin_cartao_auditoria_id`),
  INDEX `FK_cartao_auditoria_1_idx` USING BTREE (`fin_cartao_presente_id` ASC),
  INDEX `FK_cartao_auditoria_2_idx` USING BTREE (`sis_usuario_id` ASC),
  CONSTRAINT `FK_cartao_auditoria_1`
    FOREIGN KEY (`fin_cartao_presente_id`)
    REFERENCES `fin_cartao_presente` (`fin_cartao_presente_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_cartao_auditoria_2`
    FOREIGN KEY (`sis_usuario_id`)
    REFERENCES `sis_usuario` (`sis_usuario_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

# Adicionando as novas funcoes no sistema de permissao
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('7', 'br.com.opensig.financeiro.client.controlador.comando.ComandoCartaoPresente', '3', '1', '1');
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES ('7', 'br.com.opensig.financeiro.client.controlador.comando.ComandoCartaoAuditoria', '3', '2', '1');
# dar permissoes ao grupo de administrativo e gerentes pelo sistema

# Adicionando a forma de pagamento Cartao Presente ao financeiro
INSERT INTO `fin_forma` (`fin_forma_descricao`, `fin_forma_tef`, `fin_forma_vinculado`, `fin_forma_debito`, `fin_forma_rede`, `fin_forma_pagar`, `fin_forma_receber`) VALUES ('CARTAO PRESENTE', '0', '0', '0', 'LOJA', '0', '1');
