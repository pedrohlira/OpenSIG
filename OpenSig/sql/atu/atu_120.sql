# Atualizacoes da versao 1.2.0

# Modificando o campo de numero da tabela de endereco para varchar
ALTER TABLE `emp_endereco` 
CHANGE COLUMN `emp_endereco_numero` `emp_endereco_numero` VARCHAR(10) NOT NULL ;
