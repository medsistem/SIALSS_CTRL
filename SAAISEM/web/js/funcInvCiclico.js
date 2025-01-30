/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$('#formBusca').submit(function() {
    return false;
});

$('#formIngresa').submit(function() {
    return false;
});
$('#btnGuardar').click(function() {
    var F_ClaPro = $('#F_ClaPro').val();
    var F_ClaLot = $('#F_ClaLot').val();
    var F_FecCad = $('#F_FecCad').val();
    var F_ClaUbi = $('#F_ClaUbi').val();
    var F_Presentacion = $('#F_Presentacion').val();
    var F_Total = $('#F_Total').val();


    if (F_ClaPro === "" || F_ClaLot === "" || F_FecCad === "" || F_ClaUbi === "" || F_Presentacion === "" || F_Total === "") {
        alert('Favor de llenar todos los datos');
        return false;
    } else {
        var dir = '../JQInvenCiclico?accion=GuardarInsumo';
        var form = $('#formIngresa');
        $.ajax({
            type: form.attr('method'),
            url: dir,
            data: form.serialize(),
            success: function(data) {
                LimpiaDatos();
                recargaTabla(F_ClaUbi);
            },
            error: function(data) {
                alert('Error al leer del CB de la Ubicación');
            }
        });

        function LimpiaDatos() {
            $('#F_ClaPro').attr('value', '');
            $('#F_ClaLot').attr('value', '');
            $('#F_FecCad').attr('value', '');
            $('#F_Presentacion').val('');
            $('#F_Total').val('');
        }
        function recargaTabla(Ubica) {
            $('#tbInsumo').load('capturaClave.jsp?F_Ubica=' + Ubica + ' #tbInsumo');
        }
    }
});
$('#btnBuscar').click(function() {
    var CBUbi = $('#buscarUbi').val();
    var CBMed = $('#buscarMed').val();
    var Descrip = $('#buscarDescrip').val();

    if (CBUbi !== "") {
        $('#buscarMed').val('');
        var dir = '../JQInvenCiclico?accion=BuscarUbi';
        var form = $('#formBusca');
        $.ajax({
            type: form.attr('method'),
            url: dir,
            data: form.serialize(),
            success: function(data) {
                poneDatos(data);
            },
            error: function(data) {
                alert('Error al leer del CB de la Ubicación');
            }
        });
        function recargaTabla(Ubica) {
            $('#tbInsumo').load('capturaClave.jsp?F_Ubica=' + Ubica + ' #tbInsumo');
        }
        function poneDatos(data) {
            var json = JSON.parse(data);
            $('#selectClave').empty();
            $('#selectClave').append(
                    $('<option>', {
                        value: "",
                        text: "--Clave--"
                    })
                    );
            $('#selectLote').empty();
            $('#selectLote').append(
                    $('<option>', {
                        value: "",
                        text: "--Lote--"
                    })
                    );
            $('#selectCadu').empty();
            $('#selectCadu').append(
                    $('<option>', {
                        value: "",
                        text: "--Caducidad--"
                    })
                    );
            $('#F_ClaUbi').attr('value', json[0].F_ClaUbi);
            for (var i = 0; i < json.length; i++) {
                //alert(json[i].F_ClaPro);
                if (typeof (json[i].F_ClaPro) !== "undefined") {
                    $('#selectClave').append(
                            $('<option>', {
                                value: json[i].F_ClaPro,
                                text: json[i].F_ClaPro
                            })
                            );
                    $('#selectLote').append(
                            $('<option>', {
                                value: json[i].F_ClaLot,
                                text: json[i].F_ClaLot
                            })
                            );
                    $('#selectCadu').append(
                            $('<option>', {
                                value: json[i].F_FecCad,
                                text: json[i].F_FecCad
                            })
                            );
                }
            }
            recargaTabla(json[0].F_ClaUbi);
        }
    } else if (CBMed !== "") {
        /*var dir = '../JQInvenCiclico?accion=BuscarCBMed';
        var form = $('#formBusca');
        $.ajax({
            type: form.attr('method'),
            url: dir,
            data: form.serialize(),
            success: function(data) {
                poneDatos(data);
            },
            error: function(data) {
                alert('Error al leer del CB de la Ubicación');
            }
        });
        function recargaTabla(Ubica) {
            $('#tbInsumo').load('capturaClave.jsp?F_CBMed=' + Ubica + ' #tbInsumo');
        }
        function poneDatos(data) {
            var json = JSON.parse(data);
            $('#selectClave').empty();
            $('#selectLote').empty();
            $('#selectCadu').empty();
            $('#selectUbica').empty();
            $('#selectUbica').append(
                    $('<option>', {
                        value: "",
                        text: "--Ubicación--"
                    })
                    );
            $('#selectUbica').attr('value', json[0].F_ClaUbi);
            for (var i = 0; i < json.length; i++) {
                //alert(json[i].F_ClaPro);
                if (typeof (json[i].F_ClaUbi) !== "undefined") {
                    $('#selectUbica').append(
                            $('<option>', {
                                value: json[i].F_ClaUbi,
                                text: json[i].F_ClaUbi
                            })
                            );
                }
                if (typeof (json[i].F_ClaPro) !== "undefined") {

                    $('#selectClave').append(
                            $('<option>', {
                                value: json[i].F_ClaPro,
                                text: json[i].F_ClaLot
                            })
                            );
                    $('#selectLote').append(
                            $('<option>', {
                                value: json[i].F_ClaLot,
                                text: json[i].F_ClaLot
                            })
                            );
                    $('#selectCadu').append(
                            $('<option>', {
                                value: json[i].F_FecCad,
                                text: json[i].F_FecCad
                            })
                            );
                }
            }
            recargaTabla(json[0].F_ClaUbi);
        }*/
    } else if (Descrip !== "") {

    }
});


$('#buscarDescrip').keyup(function() {
    var descripcion = $('#buscarDescrip').val();
    $('#buscarDescrip').autocomplete({
        source: "../JQInvenCiclico?accion=buscaDescrip&descrip=" + descripcion,
        minLenght: 2,
        select: function(event, ui) {
            $('#buscarDescrip').val(ui.item.DesPro);
            return false;
        }
    }).data('ui-autocomplete')._renderItem = function(ul, item) {
        return $('<li>')
                .data('ui-autocomplete-item', item)
                .append('<a>' + item.DesPro + '</a>')
                .appendTo(ul);
    };
});



$('#F_ClaUbi').keyup(function() {
    var descripcion = $('#F_ClaUbi').val();
    $('#F_ClaUbi').autocomplete({
        source: "../JQInvenCiclico?accion=buscaClaUbi&descrip=" + descripcion,
        minLenght: 2,
        select: function(event, ui) {
            $('#F_ClaUbi').val(ui.item.F_ClaUbi);
            return false;
        }
    }).data('ui-autocomplete')._renderItem = function(ul, item) {
        return $('<li>')
                .data('ui-autocomplete-item', item)
                .append('<a>' + item.F_ClaUbi + '</a>')
                .appendTo(ul);
    };
});

$('#selectResto').change(function() {
    $('#F_Presentacion').val($('#selectResto').val());
});


function cambiaLoteCadu(elemento) {
    var indice = elemento.selectedIndex;
    document.getElementById('selectLote').selectedIndex = indice;
    document.getElementById('selectCadu').selectedIndex = indice;

    $('#F_ClaPro').attr('value', $('#selectClave').val());
    $('#F_ClaLot').attr('value', $('#selectLote').val());
    $('#F_FecCad').attr('value', $('#selectCadu').val());
}
