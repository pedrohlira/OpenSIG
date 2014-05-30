# Atualizacoes da versao 1.1.9

# Modificando a table de cash para guardar as horas de abertura e fechamento e a mesa
ALTER TABLE `poker_cash` 
CHANGE COLUMN `poker_cash_codigo` `poker_cash_mesa` VARCHAR(14) NOT NULL ,
CHANGE COLUMN `poker_cash_data` `poker_cash_inicio` DATETIME NOT NULL ,
ADD COLUMN `poker_cash_fim` DATETIME NULL AFTER `poker_cash_inicio`;
UPDATE `poker_cash` SET `poker_cash_fim` = `poker_cash_inicio`;

# Modificando a tabela de jogadores para identificar a data/hora de entrada e saida
ALTER TABLE `poker_jogador` 
ADD COLUMN `poker_jogador_entrada` DATETIME NOT NULL AFTER `poker_cash_id`,
ADD COLUMN `poker_jogador_saida` DATETIME NULL AFTER `poker_jogador_entrada`;
UPDATE `poker_jogador`, `poker_cash` SET `poker_jogador_entrada` = `poker_cash_inicio`, `poker_jogador_saida` = `poker_cash_inicio`
WHERE `poker_jogador`.`poker_cash_id` = `poker_cash`.`poker_cash_id`;

# Modificando a tabela de pagar, para informar a hora do evento
ALTER TABLE `poker_pagar` 
CHANGE COLUMN `poker_pagar_cadastrado` `poker_pagar_cadastrado` DATETIME NOT NULL ,
CHANGE COLUMN `poker_pagar_realizado` `poker_pagar_realizado` DATETIME NULL DEFAULT NULL ;

# Modificando a tabela de receber, para informar a hora do evento
ALTER TABLE `poker_receber` 
CHANGE COLUMN `poker_receber_cadastrado` `poker_receber_cadastrado` DATETIME NOT NULL ,
CHANGE COLUMN `poker_receber_realizado` `poker_receber_realizado` DATETIME NULL DEFAULT NULL ;
