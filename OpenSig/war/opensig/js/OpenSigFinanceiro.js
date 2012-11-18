(function($) {
	$.fn.extend( {
		// Função para validar numericamente o codigo de cheque
		validarCMC7 : function(cheque) {
			try {
				exp = /\D/g;
				cheque = cheque.toString().replace(exp, "");

				var dv1;
				var dv2;
				var dv3;
				var validado;

				// parte 1
				dv1 = this.modulo10(cheque.substring(8, 18));
				// parte 2
				dv2 = this.modulo10("000" + cheque.substring(0, 7));
				// parte 3
				dv3 = this.modulo10(cheque.substring(19, 29));

				validado = cheque.substring(0, 7) + dv1
						+ cheque.substring(8, 18) + dv2
						+ cheque.substring(19, 29) + dv3;

				return cheque == validado;
			} catch (ex) {
				return false;
			}
		},

		modulo10 : function(numero) {
			var soma = 0;
			var pos = true;
			var aux;

			for (i = 0; i < numero.length; i++) {
				aux = numero.substring(i, i + 1);
				if (pos) {
					soma = new Number(soma) + new Number(aux);
				} else {
					if (aux > 4) {
						soma = soma + (aux * 2) - 9;
					} else {
						soma = soma + (aux * 2);
					}
				}
				pos = !pos;
			}

			return soma % 10 == 0 ? 0 : 10 - (soma % 10);
		}
	});
})(jQuery);