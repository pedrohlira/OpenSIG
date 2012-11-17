SET FOREIGN_KEY_CHECKS = 0;

/*Limpando o Comercial*/;
TRUNCATE TABLE com_ecf_documento;

TRUNCATE TABLE com_ecf_nota_produto;

TRUNCATE TABLE com_ecf_nota;

TRUNCATE TABLE com_ecf_venda_produto;

TRUNCATE TABLE com_ecf_venda;

TRUNCATE TABLE com_ecf_z_totais;

TRUNCATE TABLE com_ecf_z;

TRUNCATE TABLE com_ecf;

TRUNCATE TABLE com_frete;

TRUNCATE TABLE com_valor_arredonda;

TRUNCATE TABLE com_valor_produto;

TRUNCATE TABLE com_venda_produto;

TRUNCATE TABLE com_venda;

TRUNCATE TABLE com_compra_produto;

TRUNCATE TABLE com_compra;

TRUNCATE TABLE com_natureza;

/* Limpando o Produto*/;
TRUNCATE TABLE prod_categoria;

TRUNCATE TABLE prod_estoque;

TRUNCATE TABLE prod_preco;

TRUNCATE TABLE prod_composicao;

TRUNCATE TABLE prod_produto;

TRUNCATE TABLE prod_embalagem;

/* Limpando o Financeiro*/;
TRUNCATE TABLE fin_remessa;

TRUNCATE TABLE fin_retorno;

TRUNCATE TABLE fin_pagamento;

TRUNCATE TABLE fin_pagar;

TRUNCATE TABLE fin_recebimento;

TRUNCATE TABLE fin_receber;

TRUNCATE TABLE fin_conta;

TRUNCATE TABLE fin_categoria;

TRUNCATE TABLE fin_forma;

/* Limpando o Fiscal*/;
TRUNCATE TABLE fis_nota_entrada;

TRUNCATE TABLE fis_nota_saida;

TRUNCATE TABLE fis_certificado;

TRUNCATE TABLE fis_incentivo_estado;

TRUNCATE TABLE fis_sped_fiscal;

/* Limpando a Permissao*/;
TRUNCATE TABLE sis_favorito_usuario;

TRUNCATE TABLE sis_favorito_grafico;

TRUNCATE TABLE sis_favorito_grupo;

TRUNCATE TABLE sis_favorito_portal;

TRUNCATE TABLE sis_favorito_campo;

TRUNCATE TABLE sis_favorito;

TRUNCATE TABLE sis_permissao;

TRUNCATE TABLE sis_grupo_usuario;

TRUNCATE TABLE sis_grupo;

TRUNCATE TABLE sis_usuario_empresa;

TRUNCATE TABLE sis_usuario;

/* Limpando a Empresa*/;
TRUNCATE TABLE emp_plano;

TRUNCATE TABLE emp_cliente;

TRUNCATE TABLE emp_transportadora;

TRUNCATE TABLE emp_fornecedor;

TRUNCATE TABLE emp_funcionario;

TRUNCATE TABLE emp_empresa;

TRUNCATE TABLE emp_contato;

TRUNCATE TABLE emp_contato_tipo;

TRUNCATE TABLE emp_endereco;

TRUNCATE TABLE emp_endereco_tipo;

TRUNCATE TABLE emp_entidade;

SET FOREIGN_KEY_CHECKS = 1;