## Atualizacao da versao 1.011 do OpenSIG

ALTER TABLE `com_ecf_venda` DROP FOREIGN KEY `FK_com_ecf_venda_1` , DROP FOREIGN KEY `FK_com_ecf_venda_6` ;
ALTER TABLE `com_ecf_venda` DROP INDEX `FK_com_ecf_venda_1_idx` , DROP INDEX `FK_com_ecf_venda_6_idx` ;

ALTER TABLE `com_ecf_venda` 
  ADD CONSTRAINT `FK_com_ecf_venda_1`
  FOREIGN KEY (`sis_usuario_id` )
  REFERENCES `sis_usuario` (`sis_usuario_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_com_ecf_venda_6`
  FOREIGN KEY (`sis_vendedor_id` )
  REFERENCES `sis_usuario` (`sis_usuario_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_com_ecf_venda_1_idx` (`sis_usuario_id` ASC) 
, ADD INDEX `FK_com_ecf_venda_6_idx` (`sis_vendedor_id` ASC) ;

ALTER TABLE `com_ecf_venda` ADD COLUMN `sis_gerente_id` INT NULL DEFAULT NULL  AFTER `sis_vendedor_id` , 
  ADD CONSTRAINT `FK_com_ecf_venda_7`
  FOREIGN KEY (`sis_gerente_id` )
  REFERENCES `sis_usuario` (`sis_usuario_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_com_ecf_venda_7_idx` (`sis_gerente_id` ASC) ;

DELETE FROM com_ecf_z_totais WHERE com_ecf_z_totais_valor = 0.00;