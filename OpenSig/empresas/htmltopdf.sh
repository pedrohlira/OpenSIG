#!/bin/sh

# Path do comando e arquivos
PATH="/Volumes/Macintosh HD 2/Users/pedrohenriquelira/Documents/Projetos/Git/OpenSIG/OpenSig/empresas";

# Comando para Mac
CMD="$PATH/wkhtmltopdf.app/Contents/MacOS/wkhtmltopdf";

# Comando para Linux
#CMD="$PATH"/wkhtmltopdf;

# Comando para Windows
#CMD="$PATH/wkthmltopdf.exe";

# Aplicatico com parametros
"$CMD" --footer-left "Sistema: OpenSIG" --footer-center "Pagina [page] de [toPage]" --footer-right "[date] [time]" --footer-font-name "serif" --footer-font-size 8 --quiet --orientation $1 "$2" "$3";
