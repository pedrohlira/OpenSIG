(function($) {
	$.fn.extend({
		// Função para validar numericamente um CPF
		validarCPF : function(cpf) {
			try {
				var numeros, digitos, soma, i, resultado, digitos_iguais;
				exp = /\.|\-|\//g;
				cpf = cpf.toString().replace(exp, "");
				digitos_iguais = 1;
				if (cpf.length != 11){
					return false;
				}else if(cpf == "00000000000"){
					return true;
				}
				for (i = 0; i < cpf.length - 1; i++) {
					if (cpf.charAt(i) != cpf.charAt(i + 1)) {
						digitos_iguais = 0;
						break;
					}
				}
				if (!digitos_iguais) {
					numeros = cpf.substring(0, 9);
					digitos = cpf.substring(9);
					soma = 0;
					for (i = 10; i > 1; i--) {
						soma += numeros.charAt(10 - i) * i;
					}
					resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
					if (resultado != digitos.charAt(0)) {
						return false;
					}
					numeros = cpf.substring(0, 10);
					soma = 0;
					for (i = 11; i > 1; i--) {
						soma += numeros.charAt(11 - i) * i;
					}
					resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
					if (resultado != digitos.charAt(1)) {
						return false;
					}
					return true;
				} else
					return false;
			} catch (ex) {
				return false;
			}
		},
		// Função para validar numericamente um CNPJ
		validarCNPJ : function(cnpj) {
			try {
				var valida = new Array(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2);
				var dig1 = new Number;
				var dig2 = new Number;

				exp = /\.|\-|\//g;
				cnpj = cnpj.toString().replace(exp, "");
				var dv = new Number(eval(cnpj.charAt(12) + cnpj.charAt(13)));

				for (i = 0; i < valida.length; i++) {
					dig1 += i > 0 ? cnpj.charAt(i - 1) * valida[i] : 0;
					dig2 += cnpj.charAt(i) * valida[i];
				}

				dig1 = dig1 % 11 < 2 ? 0 : 11 - (dig1 % 11);
				dig2 = dig2 % 11 < 2 ? 0 : 11 - (dig2 % 11);

				return dv == dig1 * 10 + dig2;
			} catch (ex) {
				return false;
			}
		}
	});
})(jQuery);