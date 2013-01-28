# Atualizacoes da versao 1.0.13

# Arrumando a classe que executa o bloco
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoD.RegistroD500' WHERE `fis_sped_bloco_id`='106';
UPDATE `fis_sped_bloco` SET `fis_sped_bloco_classe`='br.com.opensig.fiscal.server.sped.blocoD.RegistroD590' WHERE `fis_sped_bloco_id`='109';

# Arrumando a funcao que contem a exportacao/importacao
UPDATE `sis_exp_imp` SET `sis_exp_imp_funcao`='br.com.opensig.fiscal.client.controlador.comando.ComandoSped' WHERE `sis_exp_imp_funcao`='br.com.opensig.fiscal.client.controlador.comando.ComandoSpedFiscal';

# Arrumando os valores dos itens das vendas do ecf que tiveram descontos
UPDATE com_ecf_venda_produto SET com_ecf_venda_produto_liquido = com_ecf_venda_produto_bruto - (com_ecf_venda_produto_bruto * com_ecf_venda_produto_desconto / 100) WHERE com_ecf_venda_produto_desconto > 0.00;
UPDATE com_ecf_venda_produto SET com_ecf_venda_produto_liquido = 0.01 WHERE com_ecf_venda_produto_liquido = 0.00;
UPDATE com_ecf_venda_produto SET com_ecf_venda_produto_total = com_ecf_venda_produto_liquido * com_ecf_venda_produto_quantidade WHERE com_ecf_venda_produto_desconto > 0.00;

# Arrumando os dados de cadastros dos clientes oriundos do OpenPDV
UPDATE emp_entidade SET emp_entidade_nome1 = 'CONSUMIDOR' WHERE emp_entidade_nome1 = '' AND emp_entidade_observacao LIKE 'IMPORTADO DO OPENPDV%';
UPDATE emp_entidade SET emp_entidade_nome2 = 'CONSUMIDOR' WHERE emp_entidade_observacao LIKE 'IMPORTADO DO OPENPDV%';

INSERT INTO emp_endereco
(`emp_entidade_id`,`emp_endereco_tipo_id`,`emp_municipio_id`,`emp_endereco_logradouro`,`emp_endereco_numero`,`emp_endereco_bairro`,`emp_endereco_complemento`,`emp_endereco_cep`)
SELECT emp_entidade_id, 2, 1695, 'NAO INFORMADO', 0, 'NAO INFORMADO', '', '00000000' FROM emp_entidade WHERE emp_entidade_observacao LIKE 'IMPORTADO DO OPENPDV%';

INSERT INTO emp_contato
(`emp_entidade_id`,`emp_contato_tipo_id`,`emp_contato_descricao`,`emp_contato_pessoa`)
SELECT emp_entidade_id, 1, '(00) 0000-0000', emp_entidade_nome1 FROM emp_entidade WHERE emp_entidade_observacao LIKE 'IMPORTADO DO OPENPDV%';

# Alterando a tabele z_totais para ter o campo codigo unico
ALTER TABLE `com_ecf_z_totais` 
ADD UNIQUE INDEX `UK_com_ecf_z_totais_1` (`com_ecf_z_id` ASC, `com_ecf_z_totais_codigo` ASC) ;
 