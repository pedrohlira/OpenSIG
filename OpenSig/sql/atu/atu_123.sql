# Atualizacoes da versao 1.2.3

# Alterando a tabela de entidade para aceitar NULL no campo de observacao.
ALTER TABLE `emp_entidade` CHANGE COLUMN `emp_entidade_observacao` `emp_entidade_observacao` VARCHAR(255) NULL DEFAULT NULL ;