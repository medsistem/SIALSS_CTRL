/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$('#selectClave').keyup(function() {
    var descripcion = $('#selectClave').val();
    $('#selectClave').autocomplete({
        source: "/SIALSS_CTRL/JQIngresos?accion=buscaClave&clave=" + descripcion,
        minLenght: 2,
        select: function(event, ui) {
            $('#selectClave').val(ui.item.F_Clave);
            return false;
        }
    }).data('ui-autocomplete')._renderItem = function(ul, item) {
        return $('<li>')
                .data('ui-autocomplete-item', item)
                .append('<a>' + item.F_Clave + '</a>')
                .appendTo(ul);
    };
});


$('#ClaPro').keyup(function() {
    var descripcion = $('#ClaPro').val();
    $('#ClaPro').autocomplete({
        source: "/SIALSS_CTRL/JQIngresos?accion=buscaClaveTodas&clave=" + descripcion,
        minLenght: 2,
        select: function(event, ui) {
            $('#ClaPro').val(ui.item.F_ClaPro);
            return false;
        }
    }).data('ui-autocomplete')._renderItem = function(ul, item) {
        return $('<li>')
                .data('ui-autocomplete-item', item)
                .append('<a>' + item.F_ClaPro + '</a>')
                .appendTo(ul);
    };
});

                             