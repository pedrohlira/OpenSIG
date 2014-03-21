# Atualizacoes da versao 1.1.7

# Modificando os campos de protocolo para cancelamento
ALTER TABLE `fis_nota_entrada` 
CHANGE COLUMN `fis_nota_entrada_protocolo_cancelado` `fis_nota_entrada_protocolo_cancelado` VARCHAR(15) NULL ;

ALTER TABLE `fis_nota_saida` 
CHANGE COLUMN `fis_nota_saida_protocolo_cancelado` `fis_nota_saida_protocolo_cancelado` VARCHAR(15) NULL ;

# Modificando o campo de referencia para suporta um texto maior
ALTER TABLE `prod_produto` 
CHANGE COLUMN `prod_produto_referencia` `prod_produto_referencia` VARCHAR(60) NOT NULL ;
