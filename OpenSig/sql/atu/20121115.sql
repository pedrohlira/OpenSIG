## Atualizacao da versao 1.08 do OpenSIG

# Para deixar compativel com o OpenPDV 1.01, onde sera salvo o vendedor vinculado a venda
ALTER TABLE `com_ecf_venda` ADD COLUMN `sis_vendedor_id` INT NULL DEFAULT NULL  AFTER `com_ecf_z_id` , 
  ADD CONSTRAINT `FK_com_ecf_venda_6`
  FOREIGN KEY (`sis_usuario_id` )
  REFERENCES `sis_usuario` (`sis_usuario_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_com_ecf_venda_6_idx` (`sis_usuario_id` ASC) ;