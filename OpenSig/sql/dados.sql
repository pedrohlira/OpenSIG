/* ENTIDADES*/;
INSERT INTO `emp_entidade`
(`emp_entidade_id`,
`emp_entidade_pessoa`,
`emp_entidade_nome1`,
`emp_entidade_nome2`,
`emp_entidade_documento1`,
`emp_entidade_documento2`,
`emp_entidade_ativo`,
`emp_entidade_observacao`)
VALUES
(
1,
'JURÍDICA',
'EMPRESA PADRAO',
'EMPRESA',
'00.000.000/0000-00',
'ISENTO',
1,
''
);

INSERT INTO `emp_entidade`
(`emp_entidade_id`,
`emp_entidade_pessoa`,
`emp_entidade_nome1`,
`emp_entidade_nome2`,
`emp_entidade_documento1`,
`emp_entidade_documento2`,
`emp_entidade_ativo`,
`emp_entidade_observacao`)
VALUES
(
2,
'JURÍDICA',
'FORNECEDOR PADRAO',
'FORNECEDOR',
'00.000.000/0000-00',
'ISENTO',
1,
''
);

INSERT INTO `emp_entidade`
(`emp_entidade_id`,
`emp_entidade_pessoa`,
`emp_entidade_nome1`,
`emp_entidade_nome2`,
`emp_entidade_documento1`,
`emp_entidade_documento2`,
`emp_entidade_ativo`,
`emp_entidade_observacao`)
VALUES
(
3,
'JURÍDICA',
'TRANSPORTADORA PADRAO',
'TRANSPORTADORA',
'00.000.000/0000-00',
'ISENTO',
1,
''
);

INSERT INTO `emp_entidade`
(`emp_entidade_id`,
`emp_entidade_pessoa`,
`emp_entidade_nome1`,
`emp_entidade_nome2`,
`emp_entidade_documento1`,
`emp_entidade_documento2`,
`emp_entidade_ativo`,
`emp_entidade_observacao`)
VALUES
(
4,
'FÍSICA',
'CLIENTE PADRAO',
'CLIENTE',
'000.000.000-00',
'00.000.000-0',
1,
''
);

INSERT INTO `emp_entidade`
(`emp_entidade_id`,
`emp_entidade_pessoa`,
`emp_entidade_nome1`,
`emp_entidade_nome2`,
`emp_entidade_documento1`,
`emp_entidade_documento2`,
`emp_entidade_ativo`,
`emp_entidade_observacao`)
VALUES
(
5,
'FÍSICA',
'FUNCIONARIO PADRAO',
'FUNCIONARIO',
'000.000.000-00',
'00.000.000-0',
1,
''
);

/* CONTATOS*/;
INSERT INTO `emp_contato_tipo`
(`emp_contato_tipo_id`,
`emp_contato_tipo_descricao`)
VALUES
(
1,
'TELEFONE'
);
INSERT INTO `emp_contato_tipo`
(`emp_contato_tipo_id`,
`emp_contato_tipo_descricao`)
VALUES
(
2,
'E-MAIL'
);

INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
1,
1,
1,
'(00) 0000-0000',
''
);

INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
2,
1,
2,
'SEU@EMAIL.COM',
'SUPORTE'
);

INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
3,
2,
1,
'(00) 0000-0000',
''
);

INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
4,
2,
2,
'SUPORTE@PHDSS.COM.BR',
'SUPORTE'
);

INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
5,
3,
1,
'(00) 0000-0000',
''
);

INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
6,
3,
2,
'SUPORTE@PHDSS.COM.BR',
'SUPORTE'
);


INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
7,
4,
1,
'(00) 0000-0000',
''
);

INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
8,
4,
2,
'SUPORTE@PHDSS.COM.BR',
'SUPORTE'
);

INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
9,
5,
1,
'(00) 0000-0000',
''
);

INSERT INTO `emp_contato`
(`emp_contato_id`,
`emp_entidade_id`,
`emp_contato_tipo_id`,
`emp_contato_descricao`,
`emp_contato_pessoa`)
VALUES
(
10,
5,
2,
'SUPORTE@PHDSS.COM.BR',
'SUPORTE'
);

/* ENDERECOS*/;
INSERT INTO `emp_endereco_tipo`
(`emp_endereco_tipo_id`,
`emp_endereco_tipo_descricao`)
VALUES
(
1,
'COMERCIAL'
);

INSERT INTO `emp_endereco_tipo`
(`emp_endereco_tipo_id`,
`emp_endereco_tipo_descricao`)
VALUES
(
2,
'RESIDENCIAL'
);

INSERT INTO `emp_endereco`
(`emp_endereco_id`,
`emp_entidade_id`,
`emp_endereco_tipo_id`,
`emp_municipio_id`,
`emp_endereco_logradouro`,
`emp_endereco_numero`,
`emp_endereco_complemento`,
`emp_endereco_bairro`,
`emp_endereco_cep`)
VALUES
(
1,
1,
1,
1695,
'RUA PADRAO',
0,
'',
'BAIRRO PADRAO',
'00000-000'
);

INSERT INTO `emp_endereco`
(`emp_endereco_id`,
`emp_entidade_id`,
`emp_endereco_tipo_id`,
`emp_municipio_id`,
`emp_endereco_logradouro`,
`emp_endereco_numero`,
`emp_endereco_complemento`,
`emp_endereco_bairro`,
`emp_endereco_cep`)
VALUES
(
2,
2,
1,
1695,
'RUA PADRAO',
0,
'',
'BAIRRO PADRAO',
'00000-000'
);

INSERT INTO `emp_endereco`
(`emp_endereco_id`,
`emp_entidade_id`,
`emp_endereco_tipo_id`,
`emp_municipio_id`,
`emp_endereco_logradouro`,
`emp_endereco_numero`,
`emp_endereco_complemento`,
`emp_endereco_bairro`,
`emp_endereco_cep`)
VALUES
(
3,
3,
1,
1695,
'RUA PADRAO',
0,
'',
'BAIRRO PADRAO',
'00000-000'
);

INSERT INTO `emp_endereco`
(`emp_endereco_id`,
`emp_entidade_id`,
`emp_endereco_tipo_id`,
`emp_municipio_id`,
`emp_endereco_logradouro`,
`emp_endereco_numero`,
`emp_endereco_complemento`,
`emp_endereco_bairro`,
`emp_endereco_cep`)
VALUES
(
4,
4,
2,
1695,
'RUA PADRAO',
0,
'',
'BAIRRO PADRAO',
'00000-000'
);

INSERT INTO `emp_endereco`
(`emp_endereco_id`,
`emp_entidade_id`,
`emp_endereco_tipo_id`,
`emp_municipio_id`,
`emp_endereco_logradouro`,
`emp_endereco_numero`,
`emp_endereco_complemento`,
`emp_endereco_bairro`,
`emp_endereco_cep`)
VALUES
(
5,
5,
2,
1695,
'RUA PADRAO',
0,
'',
'BAIRRO PADRAO',
'00000-000'
);

/* VINCULANDO*/;
INSERT INTO `emp_empresa`
(`emp_empresa_id`,
`emp_entidade_id`)
VALUES
(
1,
1
);

INSERT INTO `emp_plano`
(`emp_plano_id`,
`emp_empresa_id`,
`emp_plano_inicio`,
`emp_plano_fim`,
`emp_plano_limite`,
`emp_plano_excedente`)
VALUES
(
1,
1,
CURRENT_DATE(),
NULL,
0,
1
);


INSERT INTO `emp_fornecedor`
(`emp_fornecedor_id`,
`emp_entidade_id`)
VALUES
(
1,
2
);

INSERT INTO `emp_transportadora`
(`emp_transportadora_id`,
`emp_entidade_id`)
VALUES
(
1,
3
);

INSERT INTO `emp_cliente`
(`emp_cliente_id`,
`emp_entidade_id`)
VALUES
(
1,
4
);

INSERT INTO `emp_funcionario`
(`emp_funcionario_id`,
`emp_entidade_id`,
`emp_empresa_id`)
VALUES
(
1,
5,
1
);

/* PERMISSAO*/;
INSERT INTO `sis_usuario`
(`sis_usuario_id`,
`sis_usuario_login`,
`sis_usuario_senha`,
`sis_usuario_desconto`,
`sis_usuario_ativo`,
`sis_usuario_sistema`,
`sis_usuario_email`)
VALUES
(
1,
'ADMIN',
SHA1('opensig'),
100,
1,
1,
'SEU@EMAIL.COM'
);

INSERT INTO `sis_grupo`
(`sis_grupo_id`,
`emp_empresa_id`,
`sis_grupo_nome`,
`sis_grupo_descricao`,
`sis_grupo_desconto`,
`sis_grupo_ativo`,
`sis_grupo_sistema`)
VALUES
(
1,
1,
'TODOS',
'TODOS OS USUARIOS DA EMPRESA',
0,
1,
1
);

INSERT INTO `sis_grupo_usuario`
(`sis_grupo_id`,
`sis_usuario_id`)
VALUES
(
1,
1
);

INSERT INTO `sis_usuario_empresa`
(`sis_usuario_id`,
`emp_empresa_id`)
VALUES
(
1,
1
);

INSERT INTO `sis_permissao`
(`sis_permissao_id`,
`sis_usuario_id`,
`sis_grupo_id`,
`sis_modulo_id`,
`sis_funcao_id`,
`sis_acao_id`,
`sis_executar`)
VALUES
(
1,
1,
NULL,
-1,
-1,
-1,
1
);

/* FINANCEIRO*/;
INSERT INTO `fin_categoria`
(`fin_categoria_id`,
`fin_categoria_descricao`)
VALUES
(
1,
'PADRAO'
);

INSERT INTO `fin_categoria`
(`fin_categoria_id`,
`fin_categoria_descricao`)
VALUES
(
2,
'VENDAS'
);

INSERT INTO `fin_categoria`
(`fin_categoria_id`,
`fin_categoria_descricao`)
VALUES
(
3,
'COMPRAS'
);

INSERT INTO `fin_categoria`
(`fin_categoria_id`,
`fin_categoria_descricao`)
VALUES
(
4,
'FRETE'
);

INSERT INTO `fin_categoria`
(`fin_categoria_id`,
`fin_categoria_descricao`)
VALUES
(
5,
'ECF'
);

INSERT INTO `fin_conta`
(`fin_conta_id`,
`fin_banco_id`,
`emp_empresa_id`,
`fin_conta_nome`,
`fin_conta_numero`,
`fin_conta_agencia`,
`fin_conta_carteira`,
`fin_conta_convenio`,
`fin_conta_saldo`)
VALUES
(
1,
1,
1,
'BB',
'00000-0',
'0000-0',
'',
'',
0.00
);

INSERT INTO `fin_forma`
(`fin_forma_id`,
`fin_forma_codigo`,
`fin_forma_debito`,
`fin_forma_descricao`,
`fin_forma_pagar`,
`fin_forma_receber`,
`fin_forma_rede`,
`fin_forma_tef`,
`fin_forma_vinculado`)
VALUES
(
1,
'01',
0,
'DINHEIRO',
1,
1,
'LOJA',
0,
0
);

/* Comercial */
INSERT INTO `com_natureza` VALUES (1,1,'VENDA','VENDA DE MERCADORIAS',5102,5403,0,0,0,0);
INSERT INTO `com_natureza` VALUES (2,1,'COMPRA','COMPRA DE MERCADORIAS',1102,1403,0,0,0,0);
INSERT INTO `com_natureza` VALUES (3,1,'PERDA,ROUBO,QUEBRA','LANÇAMENTO EFETUADO A TÍTULO DE BAIXA DE ESTOQUE DECORRENTE ',5927,5927,0,0,0,0);
INSERT INTO `com_valor_produto` VALUES (1,1,NULL,NULL,10,50,'BRUTO * 1.ICMS * 1.IPI * 1.DESPESA * 1.MARKUP');
INSERT INTO `prod_embalagem` VALUES(1,'UND', 'MENOR UNIDADE', 1);
