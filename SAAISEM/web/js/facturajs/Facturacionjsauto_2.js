
$(document).ready(function () {
    $('#datosfolios').dataTable();
    //obtenerIdreg();
    $('#BtngenerarRemision').click(function () {

        var ban = 0;
        var CantidadAcomulada = 0;
        var Catalogo = $('#Cata').val();
        var Tipo = $('#F_Tipo').val();
        var FecEnt = $('#FecEnt').val();
        var checkboxValues = "";
        var Obs = "";
        alert(FecEnt);
        if (FecEnt != "") {
            swal({
                title: "¿Seguro de confirmar la Factura?",
                text: "No podrás deshacer este paso...",
                type: "warning",
                showCancelButton: true,
                //cancelButtonText: "Mejor no",
                closeOnConfirm: false,
                confirmButtonColor: "#DD6B55",
                showLoaderOnConfirm: true,
                confirmButtonText: "Continuar!"
            },
                    function () {
                        $("input:checkbox:checked").each(function () {
                            checkboxValues += $(this).val() + "','";
                        });
                        checkboxValues = checkboxValues.substring(0, checkboxValues.length - 2);
                        checkboxValues = "'" + checkboxValues;

                        $.ajax({
                            type: "GET",
                            url: "FacturacionTran?accion=obtenerIdRegFactAuto&ClaUni=" + checkboxValues + "&Catalogo=" + Catalogo + "&tablaUnireq=tb_unireq",
                            dataType: "json",
                            async: false,
                            success: function (data) {
                                $.each(data, function (idx, elem) {
                                    var isValidado;
                                    isValidado = elem.validado;
                                    var Cantidad = $("#Cantidad_" + elem.Datos).val();

                                    CantidadAcomulada = parseInt(CantidadAcomulada) + parseInt(Cantidad);

                                    $.ajax({
                                        url: "FacturacionTran",
                                        data: {accion: "ActualizaReqId", ClaUni: elem.ClaUni, ClaPro: elem.ClaPro, IdRegistro: elem.IdReg, Catalogo: Catalogo, Cantidad: Cantidad},
                                        type: 'GET',
                                        dataType: 'JSON',
                                        async: true,
                                        success: function (data) {
                                            if (data.msj) {

                                            } else {
                                                swal("Datos no Registrados", "", "error");
                                                //location.reload();
                                            }

                                        }
                                    });


                                });
                            }
                        });

                        $.ajax({
                            url: "FacturacionTran",
                            data: {accion: "GeneraFolio", ClaUni: checkboxValues, Catalogo: Catalogo, Tipo: Tipo, FecEnt: FecEnt},
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
                                } else {
                                    swal("Datos no Registrados", "", "error");
                                    //location.reload();
                                }

                            }
                        });
                    });
        } else {
            swal("Atención", "Favor de llenar campo de observaciones", "warning");
        }
    });

});