
$(document).ready(function () {
    $('#datosfolios').dataTable();
    //obtenerIdreg();
    $('#BtngenerarRemision').click(function () {

        var ban = 0;
        var CantidadAcomulada = 0;
        var FecEnt = $('#FecEnt').val();
        var Proyecto = $('#Proyecto').val();
        var checkboxValues = "";
        var Obs = "";

        if (FecEnt != "") {
            swal({
                title: "¿Seguro de generar las Remisiones Enseres?",
                text: "No podrás deshacer este paso.",
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
                            url: "FacturacionTran?accion=obtenerIdRegEnseres&ClaUni=" + checkboxValues + "",
                            dataType: "json",
                            async: false,
                            success: function (data) {
                                $.each(data, function (idx, elem) {
                                    var isValidado;
                                    isValidado = elem.validado;
                                    var Cantidad = $("#Cantidad_" + elem.Datos).val();
                                    var CantidadReq = $("#CantidadReq_" + elem.Datos).val();
                                    var Obs = $("#obs" + elem.ClaUni).val();

                                    CantidadAcomulada = parseInt(CantidadAcomulada) + parseInt(Cantidad);

                                    $.ajax({
                                        url: "FacturacionTran",
                                        data: {accion: "ActualizaReqIdEnseres", ClaUni: elem.ClaUni, ClaPro: elem.ClaPro, IdRegistro: elem.IdReg, Cantidad: Cantidad, CantidadReq: CantidadReq, Obs: Obs},
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
                            data: {accion: "GeneraFolioEnseres", ClaUni: checkboxValues, FecEnt: FecEnt, Proyecto: Proyecto},
                            type: 'GET',
                            dataType: 'JSON',
                            async: true,
                            success: function (data) {
                                if (data.msj) {
                                    swal({
                                        title: "Remisiones generado correctamente!",
                                        text: "",
                                        type: "success"
                                    }, function () {
                                        //location.reload();
                                        window.location = "facturaAtomatica.jsp";
                                    });
                                    //swal("Registrados", "", "error");
                                } else {
                                    swal("Datos no Registrados", "", "error");
                                    //location.reload();
                                }

                            }
                        });

                    });
        } else {
            swal("Atención", "Favor de llenar campo de Fecha Entrega", "warning");
        }
    });

});