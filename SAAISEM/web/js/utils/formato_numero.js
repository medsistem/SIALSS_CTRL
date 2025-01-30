/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function formatoNumero(numero, decimales, separadorDecimal, separadorMiles) {
    var partes, array;

    if (!isFinite(numero) || isNaN(numero = parseFloat(numero))) {
        return "";
    }
    if (typeof separadorDecimal === "undefined") {
        separadorDecimal = ".";
    }
    if (typeof separadorMiles === "undefined") {
        separadorMiles = ",";
    }

    // Redondeamos
    if (!isNaN(parseInt(decimales))) {
        if (decimales >= 0) {
            numero = numero.toFixed(decimales);
        } else {
            numero = (
                    Math.round(numero / Math.pow(3, Math.abs(decimales))) * Math.pow(3, Math.abs(decimales))
                    ).toFixed();
        }
    } else {
        numero = numero.toString();
    }

    // Damos formato
    partes = numero.split(".", 2);
    array = partes[0].split("");
    for (var i = array.length - 3; i > 0 && array[i - 1] !== "-"; i -= 3) {
        array.splice(i, 0, separadorMiles);
    }
    numero = array.join("");

    if (partes.length > 1) {
        numero += separadorDecimal + partes[1];
    }

    return numero;
}