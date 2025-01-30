/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$('#F_ClaUbi').keyup(function () {
    var descripcion = $('#F_ClaUbi').val();
 
    $('#F_ClaUbi').autocomplete({
        source: "../JQInvenCiclico?accion=buscaClaUbi&descrip=" + descripcion,
        minLenght: 2,
        select: function (event, ui) {
            $('#F_ClaUbi').val(ui.item.F_ClaUbi);
            return false;
        }
    }).data('ui-autocomplete')._renderItem = function (ul, item) {
        return $('<li>')
                .data('ui-autocomplete-item', item)
                .append('<a>' + item.F_ClaUbi + '</a>')
                .appendTo(ul);
    };
});

function validaRedist() {
    var CantAnt = document.getElementById('CantAnt').value;
    var CantMov = document.getElementById('CantMov').value;
    var Ubicacion = document.getElementById('F_ClaUbi').value;
    Ubicacion = Ubicacion.toUpperCase();
    if (parseInt(CantMov) > parseInt(CantAnt)) {
        alert('La Cantidad a mover no puede ser mayor a la cantidad en existencia.');
        document.getElementById('CantMov').focus();
        return false;
    }

    if (CantMov === "") {
        alert('La Cantidad a mover no puede ser vacio');
        document.getElementById('CantMov').focus();
        return false;
    }

    if (Ubicacion === "") {
        alert('La Ubicación no puede ser vacio');
        document.getElementById('F_ClaUbi').focus();
        return false;
    }

    var aClaUbi = document.getElementById('aClaUbi').value;
    var aCbUbica = document.getElementById('aCbUbica').value;

    if (Ubicacion === aClaUbi || Ubicacion === aCbUbica) {
        alert('La Ubicación no puede ser igual a la anterior');
        document.getElementById('F_ClaUbi').focus();
        return false;
    }

    return confirm("Seguro que desea hacer la redistribución?");
}