
$(document).ready(function () {
    $('#datosfolios').dataTable();
    obtenerIdreg();
    $('#BtngenerarRemision').click(function () {

        var Catalogo = $('#Cata').val();
        var checkboxValues = "";
        $('input[name="chkUniFact[]"]:checked').each(function () {
            checkboxValues += $(this).val() + "','";
        });
        checkboxValues = checkboxValues.substring(0, checkboxValues.length - 3);

        $.ajax({
            type: "GET",
            url: "FacturacionTran?accion=obtenerIdRegFact&ClaUni=" + checkboxValues + "&Catalogo=" + Catalogo + "",
            dataType: "json",
            async: false,
            success: function (data) {
                $.each(data, function (idx, elem) {
                    var isValidado;
                    isValidado = elem.validado;
                    //alert(elem.IdReg);
                    var Cantidad = $("#Cantidad" + elem.ClaUni + elem.IdReg).val();
                    alert("Unidad=" + elem.ClaUni + " Clave=" + elem.IdReg + " Cant=" + Cantidad);
                    //console.log("Datos=" + Datos);
/*
                    $.ajax({
                        url: "FacturacionTran",
                        data: {accion: "ActualizaReq", ClaUni: elem.ClaUni, ClaPro: elem.ClaPro, Cantidad: Cantidad},
                        type: 'GET',
                        dataType: 'JSON',
                        async: true,
                        success: function (data) {
                            if (data.msj) {
                                /*swal({
                                    title: "Redistribución realizada correctamente!",
                                    text: "",
                                    type: "success"
                                }, function () {
                                    //location.reload();
                                    window.location = "FacturacionTran?accion=TerminoCaptura";
                                });*/
                                //swal("Registrados", "", "error");
                         /*   } else {
                                swal("Datos no Registrados", "", "error");
                                //location.reload();
                            }

                        }
                    });
*/

                });
            }
        });

    });

});

function obtenerIdreg() {
    var Catalogo = $('#Cata').val();
    var checkboxValues = "";
    $('input[name="chkUniFact[]"]:checked').each(function () {
        checkboxValues += $(this).val() + "','";
    });
    checkboxValues = checkboxValues.substring(0, checkboxValues.length - 3);

    $.ajax({
        type: "GET",
        url: "FacturacionTran?accion=obtenerIdRegFact&ClaUni=" + checkboxValues + "&Catalogo=" + Catalogo + "",
        dataType: "json",
        async: false,
        success: function (data) {
            $.each(data, function (idx, elem) {
                var isValidado;
                isValidado = elem.validado;
                //alert(elem.IdReg);
                $('#BtnAgregar_' + elem.IdReg).click(function () {

                    if (parseInt(Cantidad) > parseInt(elem.Existencia)) {
                        swal("Verificar", "La cantidad a facturar no puede ser mayor a la cantidad seleccionada", "error");
                    } else {
                        $('#BtnAgregar_' + elem.IdReg).prop('disabled', true);
                        $.ajax({
                            url: "FacturacionTran",
                            data: {accion: "RegistrarDatos", IdLote: elem.IdReg, CantMov: Cantidad},
                            type: 'POST',
                            dataType: 'JSON',
                            async: true,
                            success: function (data) {
                                if (data.msj) {
                                    window.location = "FacturacionTran?accion=RegresarCaptura";
                                } else {
                                    swal("Atención", "Datos no aplicados", "error");
                                    location.reload();
                                }

                            }
                        });
                    }

                });
            });
        }
    });
}