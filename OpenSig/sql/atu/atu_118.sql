# Atualizacoes da versao 1.1.8

# Adicionando o Modulo Poker
INSERT INTO `sis_modulo` (`sis_modulo_classe`, `sis_modulo_ordem`, `sis_modulo_ativo`) VALUES ('br.com.opensig.poker.client.OpenSigPoker', 6, 1);

# Adicionando as Funcoes do Poker
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoTorneio', 1, 0, 1);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoParticipante', 2, 0, 1);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoCliente', 3, 0, 1);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoTorneioTipo', 4, 0, 1);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoCash', 5, 0, 1);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoJogador', 6, 0, 1);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoJackpot', 7, 0, 1);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoPagar', 8, 0, 1);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoReceber', 9, 0, 1);
INSERT INTO `sis_funcao` (`sis_modulo_id`, `sis_funcao_classe`, `sis_funcao_ordem`, `sis_funcao_subordem`, `sis_funcao_ativo`) VALUES (8, 'br.com.opensig.poker.client.controlador.comando.ComandoForma', 10, 0, 1);

# Adicionando as Acoes do Poker
INSERT INTO `sis_acao` (`sis_funcao_id`,`sis_acao_classe`,`sis_acao_ordem`,`sis_acao_subordem`,`sis_acao_ativo`,`sis_acao_visivel`) 
SELECT `sis_funcao_id`, 'br.com.opensig.poker.client.controlador.comando.ComandoTorneioFechar', 7, 0, 1, 1 FROM `sis_funcao` WHERE `sis_funcao_classe` = 'br.com.opensig.poker.client.controlador.comando.ComandoTorneio';
INSERT INTO `sis_acao` (`sis_funcao_id`,`sis_acao_classe`,`sis_acao_ordem`,`sis_acao_subordem`,`sis_acao_ativo`,`sis_acao_visivel`) 
SELECT `sis_funcao_id`, 'br.com.opensig.poker.client.controlador.comando.ComandoTorneioPlay', 8, 0, 1, 1 FROM `sis_funcao` WHERE `sis_funcao_classe` = 'br.com.opensig.poker.client.controlador.comando.ComandoTorneio';
INSERT INTO `sis_acao` (`sis_funcao_id`,`sis_acao_classe`,`sis_acao_ordem`,`sis_acao_subordem`,`sis_acao_ativo`,`sis_acao_visivel`) 
SELECT `sis_funcao_id`, 'br.com.opensig.poker.client.controlador.comando.ComandoReBuy', 7, 0, 1, 1 FROM `sis_funcao` WHERE `sis_funcao_classe` = 'br.com.opensig.poker.client.controlador.comando.ComandoParticipante';
INSERT INTO `sis_acao` (`sis_funcao_id`,`sis_acao_classe`,`sis_acao_ordem`,`sis_acao_subordem`,`sis_acao_ativo`,`sis_acao_visivel`) 
SELECT `sis_funcao_id`, 'br.com.opensig.poker.client.controlador.comando.ComandoAddOn', 8, 0, 1, 1 FROM `sis_funcao` WHERE `sis_funcao_classe` = 'br.com.opensig.poker.client.controlador.comando.ComandoParticipante';
INSERT INTO `sis_acao` (`sis_funcao_id`,`sis_acao_classe`,`sis_acao_ordem`,`sis_acao_subordem`,`sis_acao_ativo`,`sis_acao_visivel`) 
SELECT `sis_funcao_id`, 'br.com.opensig.poker.client.controlador.comando.ComandoCashFechar', 7, 0, 1, 1 FROM `sis_funcao` WHERE `sis_funcao_classe` = 'br.com.opensig.poker.client.controlador.comando.ComandoCash';
INSERT INTO `sis_acao` (`sis_funcao_id`,`sis_acao_classe`,`sis_acao_ordem`,`sis_acao_subordem`,`sis_acao_ativo`,`sis_acao_visivel`) 
SELECT `sis_funcao_id`, 'br.com.opensig.poker.client.controlador.comando.ComandoJackpotPlay', 7, 0, 1, 1 FROM `sis_funcao` WHERE `sis_funcao_classe` = 'br.com.opensig.poker.client.controlador.comando.ComandoJackpot';

# Adicionando os Grupos do Poker
INSERT INTO `sis_grupo` (`sis_grupo_nome`,`emp_empresa_id`,`sis_grupo_descricao`,`sis_grupo_desconto`,`sis_grupo_ativo`,`sis_grupo_sistema`) VALUES ('CROUPIER', 1,'PARA USAR O SISTEMA DO TABLET.', 0.00, 1, 1);
INSERT INTO `sis_grupo` (`sis_grupo_nome`,`emp_empresa_id`,`sis_grupo_descricao`,`sis_grupo_desconto`,`sis_grupo_ativo`,`sis_grupo_sistema`) VALUES ('CAIXA', 1,'PARA USAR O SISTEMA DO TABLET.', 0.00, 1, 1);

# Criando as tabelas do modulo de poker
CREATE TABLE `poker_cliente` (
  `poker_cliente_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_cliente_nome` varchar(100) NOT NULL,
  `poker_cliente_codigo` int(11) NOT NULL,
  `poker_cliente_auxiliar` int(11) NOT NULL,
  `poker_cliente_documento` varchar(20) DEFAULT NULL,
  `poker_cliente_email` varchar(100) DEFAULT NULL,
  `poker_cliente_contato` varchar(50) DEFAULT NULL,
  `poker_cliente_data` date NOT NULL,
  `poker_cliente_associado` bit(1) NOT NULL,
  `poker_cliente_ativo` bit(1) NOT NULL,
  PRIMARY KEY (`poker_cliente_id`),
  UNIQUE KEY `poker_cliente_codigo_UNIQUE` (`poker_cliente_codigo`),
  UNIQUE KEY `poker_cliente_nome_UNIQUE` (`poker_cliente_nome`)
) ENGINE=InnoDB;

CREATE TABLE `poker_torneio_tipo` (
  `poker_torneio_tipo_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_torneio_tipo_nome` varchar(50) NOT NULL,
  PRIMARY KEY (`poker_torneio_tipo_id`),
  UNIQUE KEY `poker_torneio_tipo_nome_UNIQUE` (`poker_torneio_tipo_nome`)
) ENGINE=InnoDB;

CREATE TABLE `poker_torneio` (
  `poker_torneio_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_torneio_tipo_id` int(11) NOT NULL,
  `poker_torneio_nome` varchar(20) NOT NULL,
  `poker_torneio_codigo` varchar(14) NOT NULL,
  `poker_torneio_entrada` decimal(10,2) NOT NULL,
  `poker_torneio_entrada_ficha` int(11) NOT NULL,
  `poker_torneio_reentrada` decimal(10,2) NOT NULL,
  `poker_torneio_reentrada_ficha` int(11) NOT NULL,
  `poker_torneio_adicional` decimal(10,2) NOT NULL,
  `poker_torneio_adicional_ficha` int(11) NOT NULL,
  `poker_torneio_dealer` decimal(10,2) NOT NULL,
  `poker_torneio_dealer_ficha` int(11) NOT NULL,
  `poker_torneio_ponto` int(11) NOT NULL,
  `poker_torneio_arrecadado` decimal(10,2) NOT NULL,
  `poker_torneio_premio` decimal(10,2) NOT NULL,
  `poker_torneio_taxa` decimal(10,2) NOT NULL,
  `poker_torneio_comissao` decimal(10,2) NOT NULL,
  `poker_torneio_data` date NOT NULL,
  `poker_torneio_fechado` bit(1) NOT NULL,
  `poker_torneio_ativo` bit(1) NOT NULL,
  PRIMARY KEY (`poker_torneio_id`),
  KEY `FK_poker_torneio_1_idx` (`poker_torneio_tipo_id`) USING BTREE,
  CONSTRAINT `FK_poker_torneio_1` FOREIGN KEY (`poker_torneio_tipo_id`) REFERENCES `poker_torneio_tipo` (`poker_torneio_tipo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `poker_nivel` (
  `poker_nivel_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_torneio_id` int(11) NOT NULL,
  `poker_nivel_numero` int(11) NOT NULL,
  `poker_nivel_pequeno` int(11) NOT NULL,
  `poker_nivel_grande` int(11) NOT NULL,
  `poker_nivel_ante` int(11) NOT NULL,
  `poker_nivel_tempo` int(11) NOT NULL,
  `poker_nivel_espera` int(11) NOT NULL,
  `poker_nivel_ativo` bit(1) NOT NULL,
  PRIMARY KEY (`poker_nivel_id`),
  UNIQUE KEY `FK_poker_nivel_2_UNIQUE` (`poker_torneio_id`,`poker_nivel_numero`) USING BTREE,
  KEY `FK_poker_nivel_1_idx` (`poker_torneio_id`) USING BTREE,
  CONSTRAINT `FK_poker_nivel_1` FOREIGN KEY (`poker_torneio_id`) REFERENCES `poker_torneio` (`poker_torneio_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `poker_mesa` (
  `poker_mesa_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_torneio_id` int(11) NOT NULL,
  `poker_mesa_numero` int(11) NOT NULL,
  `poker_mesa_lugares` int(11) NOT NULL,
  `poker_mesa_ativo` bit(1) NOT NULL,
  PRIMARY KEY (`poker_mesa_id`),
  UNIQUE KEY `FK_poker_mesa_2_UNIQUE` (`poker_torneio_id`,`poker_mesa_numero`) USING BTREE,
  KEY `FK_poker_mesa_1_idx` (`poker_torneio_id`) USING BTREE,
  CONSTRAINT `FK_poker_mesa_1` FOREIGN KEY (`poker_torneio_id`) REFERENCES `poker_torneio` (`poker_torneio_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `poker_premiacao` (
  `poker_premiacao_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_torneio_id` int(11) NOT NULL,
  `poker_premiacao_posicao` int(11) NOT NULL,
  `poker_premiacao_valor` decimal(10,2) NOT NULL,
  `poker_premiacao_ponto` int(11) NOT NULL,
  PRIMARY KEY (`poker_premiacao_id`),
  UNIQUE KEY `FK_poker_premiacao_2_UNIQUE` (`poker_torneio_id`,`poker_premiacao_posicao`) USING BTREE,
  KEY `FK_ poker_premiacao_1_idx` (`poker_torneio_id`) USING BTREE,
  CONSTRAINT `FK_ poker_premiacao_1` FOREIGN KEY (`poker_torneio_id`) REFERENCES `poker_torneio` (`poker_torneio_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `poker_participante` (
  `poker_participante_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_cliente_id` int(11) NOT NULL,
  `poker_torneio_id` int(11) NOT NULL,
  `poker_mesa_id` int(11) DEFAULT NULL,
  `poker_participante_bonus` int(11) NOT NULL,
  `poker_participante_reentrada` int(11) NOT NULL,
  `poker_participante_adicional` int(11) NOT NULL,
  `poker_participante_dealer` int(11) NOT NULL,
  `poker_participante_posicao` int(11) NOT NULL,
  `poker_participante_premio` decimal(10,2) NOT NULL,
  `poker_participante_ponto` int(11) NOT NULL,
  `poker_participante_ativo` bit(1) NOT NULL,
  PRIMARY KEY (`poker_participante_id`),
  UNIQUE KEY `FK_poker_participante_4_UNIQUE` (`poker_torneio_id`,`poker_cliente_id`) USING BTREE,
  KEY `FK_poker_participante_1_idx` (`poker_torneio_id`) USING BTREE,
  KEY `FK_poker_participante_2_idx` (`poker_cliente_id`) USING BTREE,
  KEY `FK_poker_participante_3_idx` (`poker_mesa_id`) USING BTREE,
  CONSTRAINT `FK_poker_participante_1` FOREIGN KEY (`poker_torneio_id`) REFERENCES `poker_torneio` (`poker_torneio_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_poker_participante_2` FOREIGN KEY (`poker_cliente_id`) REFERENCES `poker_cliente` (`poker_cliente_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_poker_participante_3` FOREIGN KEY (`poker_mesa_id`) REFERENCES `poker_mesa` (`poker_mesa_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `poker_forma` (
  `poker_forma_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_forma_nome` varchar(50) NOT NULL,
  `poker_forma_realizado` bit(1) NOT NULL,
  `poker_forma_jackpot` bit(1) NOT NULL,
  PRIMARY KEY (`poker_forma_id`)
) ENGINE=InnoDB;

CREATE TABLE `poker_cash` (
  `poker_cash_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_cash_codigo` varchar(14) NOT NULL,
  `poker_cash_pago` decimal(10,2) NOT NULL,
  `poker_cash_recebido` decimal(10,2) NOT NULL,
  `poker_cash_saldo` decimal(10,2) NOT NULL,
  `poker_cash_data` date NOT NULL,
  `poker_cash_fechado` bit(1) NOT NULL,
  PRIMARY KEY (`poker_cash_id`)
) ENGINE=InnoDB;

CREATE TABLE `poker_jogador` (
  `poker_jogador_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_cliente_id` int(11) NOT NULL,
  `poker_cash_id` int(11) NOT NULL,
  `poker_jogador_ativo` bit(1) NOT NULL,
  PRIMARY KEY (`poker_jogador_id`),
  KEY `FK_poker_jogador_1_idx` (`poker_cliente_id`),
  KEY `FK_poker_jogador_3_idx` (`poker_cash_id`),
  CONSTRAINT `FK_poker_jogador_1` FOREIGN KEY (`poker_cliente_id`) REFERENCES `poker_cliente` (`poker_cliente_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_poker_jogador_3` FOREIGN KEY (`poker_cash_id`) REFERENCES `poker_cash` (`poker_cash_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `poker_pagar` (
  `poker_pagar_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_forma_id` int(11) NOT NULL,
  `poker_cash_id` int(11) NOT NULL,
  `poker_pagar_descricao` varchar(200) NOT NULL,
  `poker_pagar_valor` decimal(10,2) NOT NULL,
  `poker_pagar_cadastrado` date NOT NULL,
  `poker_pagar_realizado` date DEFAULT NULL,
  `poker_pagar_ativo` bit(1) NOT NULL,
  PRIMARY KEY (`poker_pagar_id`),
  KEY `FK_poker_pagar_1_idx` (`poker_forma_id`),
  KEY `FK_poker_pagar_2_idx` (`poker_cash_id`),
  CONSTRAINT `FK_poker_pagar_1` FOREIGN KEY (`poker_forma_id`) REFERENCES `poker_forma` (`poker_forma_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_poker_pagar_2` FOREIGN KEY (`poker_cash_id`) REFERENCES `poker_cash` (`poker_cash_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `poker_receber` (
  `poker_receber_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_forma_id` int(11) NOT NULL,
  `poker_cash_id` int(11) NOT NULL,
  `poker_receber_descricao` varchar(200) NOT NULL,
  `poker_receber_valor` decimal(10,2) NOT NULL,
  `poker_receber_cadastrado` date NOT NULL,
  `poker_receber_realizado` date DEFAULT NULL,
  `poker_receber_ativo` bit(1) NOT NULL,
  PRIMARY KEY (`poker_receber_id`),
  KEY `FK_poker_receber_1_idx` (`poker_forma_id`),
  KEY `FK_poker_receber_2_idx` (`poker_cash_id`),
  CONSTRAINT `FK_poker_receber_1` FOREIGN KEY (`poker_forma_id`) REFERENCES `poker_forma` (`poker_forma_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_poker_receber_2` FOREIGN KEY (`poker_cash_id`) REFERENCES `poker_cash` (`poker_cash_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `poker_jackpot` (
  `poker_jackpot_id` int(11) NOT NULL AUTO_INCREMENT,
  `poker_forma_id` int(11) NOT NULL,
  `poker_receber_id` int(11) DEFAULT NULL,
  `poker_pagar_id` int(11) DEFAULT NULL,
  `poker_jackpot_total` decimal(10,2) NOT NULL,
  PRIMARY KEY (`poker_jackpot_id`),
  KEY `FK_ poker_jackpot_1_idx` (`poker_forma_id`),
  KEY `FK_ poker_jackpot_2_idx` (`poker_pagar_id`),
  KEY `FK_ poker_jackpot_3_idx` (`poker_receber_id`),
  CONSTRAINT `FK_ poker_jackpot_1` FOREIGN KEY (`poker_forma_id`) REFERENCES `poker_forma` (`poker_forma_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ poker_jackpot_2` FOREIGN KEY (`poker_pagar_id`) REFERENCES `poker_pagar` (`poker_pagar_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ poker_jackpot_3` FOREIGN KEY (`poker_receber_id`) REFERENCES `poker_receber` (`poker_receber_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
