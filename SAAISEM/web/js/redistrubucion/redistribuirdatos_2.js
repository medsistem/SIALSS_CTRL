$(function ()
{
    $("#btnSave1").click(function ()
    {
        var lote = $("#loteNuevo").val();
        var caducidad = $("#CaducidadNuevo").val();
        var cantidad = $("#CantidadNuevo").val();
        var cb = $("#CbNuevo").val();
        var marca = $("#marcaNuevo").val();
        var id = $("#idCompraTemporal").val();
        var usuario = $("#UserActual").val();
        if (lote === "")
        {
            alert("Ingresar un lote válido por favor.");
            return false;
        } else if (caducidad === "")
        {
            alert("ingresar una caducidad válida por favor.");
            return false;
        } else if (cantidad <= 0)
        {
            alert("ingresar una cantidad válida por favor.");
            return false;
        } else if (cb === "")
        {
            alert("ingresar una cb válido por favor.");
            return false;
        } else if (marca === "")
        {
            alert("ingresar una marca válida por favor.");
            return false;
        } else
        {
            $.ajax({
                url: "recepcionTransaccional",
                data: {accion: "EditarLotes", id: id, lote: lote, caducidad: caducidad, cantidad: cantidad, cb: cb, marca: marca, usuario: usuario},
                type: 'POST',
                dataType: 'JSON',
                async: true,
                success: function (data)
                {
                    if (data.msj)
                    {
                        alert("Modificación realizada con éxito");
                    } else
                    {
                        alert("Error en la modificación contactar al departamento de sistemas.");
                    }

                    $("#btnCancel").click();
                    location.reload();


                }, error: function (jqXHR, textStatus, errorThrown) {
                    alert("Error en sistema");
                }
            });
        }


    });

    /*Redistribucion HH*/
    $("#Redistribucion").click(function ()
    {
        /*
         var CantAnt = document.getElementById('CantAnt').value;
         var CantMov = document.getElementById('CantMov').value;
         var Ubicacion = document.getElementById('F_ClaUbi').value;
         var aClaUbi = document.getElementById('aClaUbi').value;
         var aCbUbica = document.getElementById('aCbUbica').value;
         */
        var CantAnt = $("#CantAnt").val();
        var CantMov = $("#CantMov").val();
        var Ubicacion = $.trim($("#F_ClaUbi").val());
        var aClaUbi = $("#aClaUbi").val();
        var aCbUbica = $("#aCbUbica").val();
        var IdLote = $("#F_IdLote").val();
        var aCbUbica = $("#aCbUbica").val();
        var aCbUbica = $("#aCbUbica").val();
        var ClaveUbica = $("#ClaveUbica").val();
        var Origen = $('#Origen').val();
        var usuario = $("#UserActual").val();

        Ubicacion = Ubicacion.toUpperCase();

        if ((CantMov !== "") && (Ubicacion !== "")) {

            if (parseInt(CantMov) > parseInt(CantAnt)) {
                swal({
                    title: "La Cantidad a mover no puede ser mayor a la cantidad en existencia.!",
                    text: "",
                    type: "error"
                }, function () {
                    document.getElementById('CantMov').focus();
                    return false;
                });
            } else {
                if (Ubicacion.trim() === aClaUbi.trim() || Ubicacion.trim() === aCbUbica.trim()) {
                    swal({
                        title: "La Ubicacino puede ser igual a la anterior.!",
                        text: "",
                        type: "error"
                    }, function () {
                        document.getElementById('F_ClaUbi').focus();
                        return false;
                    });

                } else if (Ubicacion === "NUEVATMP" || Ubicacion === "CONTROLADOTMP" || Ubicacion === "VACUNASTMP" || Ubicacion === "GNKVACUNAS" || Ubicacion === "GNKCONTROLADO" || Ubicacion === "MERE-CTRL" || Ubicacion === "NUEVAMERE-CTRL" || Ubicacion === "LOGISTICA_CTRL" ) {
                    if ((usuario !== "ICuellarB") && (usuario !== "RCuellarB")) {

                        swal({
                            title: "USUARIO NO PERMITIDO,PARA UBICACIONES 'Controlados y Vacunas'",
                            text: "",
                            type: "error"
                        }, function () {
                            document.getElementById('F_ClaUbi').focus();
                            return false;
                        });


                    } else if (Ubicacion === "NUEVATMP" || Ubicacion === "CONTROLADOTMP" || Ubicacion === "VACUNASTMP" || Ubicacion === "GNKVACUNAS" || Ubicacion === "GNKCONTROLADO" || Ubicacion === "MERE-CTRL" || Ubicacion === "NUEVAMERE-CTRL" || Ubicacion === "LOGISTICA_CTRL" ) {
                        if ((usuario === "ICuellarB") || (usuario === "RCuellarB")) {
                            swal({
                                title: "Seguro que desea realizar la redistribución a Vacunas?",
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
                                        $.ajax({
                                            url: "../RedistribucionTransaccional",
                                            data: {accion: "Redistribuir", Ubicacion: Ubicacion, IdLote: IdLote, CantMov: CantMov, Origen: Origen},
                                            type: 'POST',
                                            dataType: 'JSON',
                                            async: true,
                                            success: function (data) {
                                                if (data.msj) {
                                                    swal({
                                                        title: "Redistribución realizada correctamente!",
                                                        text: "",
                                                        type: "success"
                                                    }, function () {

                                                        window.location = "leerInsRedistClave.jsp?ClaPro=" + ClaveUbica + "";
                                                    });
                                                } else {
                                                    swal("Atención", "VERIFICAR DATOS", "error");
                                                }
                                            }
                                        });

                                    });

                        } else {
                            swal({
                                title: "Ubicacion NO Permitida Para 'Controlados y Vacunas'",
                                text: "",
                                type: "error"
                            }, function () {
                                document.getElementById('F_ClaUbi').focus();
                                return false;
                            });
                        }
                    }
                } else if (Ubicacion === "AFGC" || Ubicacion === "APEGC" || Ubicacion === "REDFRIAGC") {
                    if (Origen === "19") {

                        swal({
                            title: "Seguro que desea realizar la redistribución a GASTOS CATASTROFICOS?",
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
                                    $.ajax({
                                        url: "../RedistribucionTransaccional",
                                        data: {accion: "Redistribuir", Ubicacion: Ubicacion, IdLote: IdLote, CantMov: CantMov, Origen: Origen},
                                        type: 'POST',
                                        dataType: 'JSON',
                                        async: true,
                                        success: function (data) {
                                            if (data.msj) {
                                                swal({
                                                    title: "Redistribución realizada correctamente!",
                                                    text: "",
                                                    type: "success"
                                                }, function () {

                                                    window.location = "leerInsRedistClave.jsp?ClaPro=" + ClaveUbica + "";
                                                });
                                            } else {
                                                swal("Atención", "VERIFICAR DATOS", "error");
                                            }
                                        }
                                    });

                                });
                        ////
                    } else {

                        swal({
                            title: "ORIGEN NO PERMITIDO,PARA UBICACIONES 'GASTOS CATASTROFICOS'",
                            text: "",
                            type: "error"
                        }, function () {
                            document.getElementById('F_ClaUbi').focus();
                            return false;
                        });
                    }

                } else if (Ubicacion === "AF" || Ubicacion === "MODULA" || Ubicacion === "MODULA2" || Ubicacion === "A0S" || Ubicacion === "APE" || Ubicacion === "REDFRIA" || Ubicacion === "CONTROLADO" || Ubicacion === "URGENTES" || Ubicacion === "AF1N" || Ubicacion === "CONTROLADO1N" || Ubicacion === "APE1N" || Ubicacion === "REDFRIA1N" || Ubicacion === "PROXACADUCAR1N" || Ubicacion === "PROXACADUCAR" || Ubicacion === "PROXACADUCARRF1N" || Ubicacion === "PROXACADUCARAPE1N" || Ubicacion === "PROXACADUCARREDFRIA'" || Ubicacion === "PROXACADUCARAPE" || Ubicacion === "PROXACADUCARCONTROLADO" || Ubicacion.match(/CROSS.*/)) {
                    if (Origen === "19") {
                        swal({
                            title: "UBICACION NO PERMITIDA, PARA ORIGEN 'GASTOS CATASTROFICOS'",
                            text: "",
                            type: "error"
                        }, function () {
                            document.getElementById('F_ClaUbi').focus();
                            return false;
                        });
                    } else {
                        swal({
                            title: "Seguro que desea realizar la redistribución a " + Ubicacion + " ?",
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
                                    $.ajax({
                                        url: "../RedistribucionTransaccional",
                                        data: {accion: "Redistribuir", Ubicacion: Ubicacion, IdLote: IdLote, CantMov: CantMov, Origen: Origen},
                                        type: 'POST',
                                        dataType: 'JSON',
                                        async: true,
                                        success: function (data) {
                                            if (data.msj) {
                                                swal({
                                                    title: "Redistribución realizada correctamente!",
                                                    text: "",
                                                    type: "success"
                                                }, function () {
                                                    //alert(ClaveUbica);
                                                    //location.reload();
                                                    window.location = "leerInsRedistClave.jsp?ClaPro=" + ClaveUbica + "";
                                                });
                                            } else {
                                                swal("Atención", "Verifica Datos de ubicacion, cantidad", "error");
                                            }
                                        }
                                    });

                                });
                        /*si son cualquier clave*/
                    }
                } else if (aClaUbi !== "NUEVATMP" && aClaUbi !== "CONTROLADOTMP" && aClaUbi !== "VACUNASTMP" && aClaUbi !== "GNKVACUNAS" && aClaUbi !== "GNKCONTROLADO"  && aClaUbi !==  "MERE-CTRL" && aClaUbi !== "NUEVAMERE-CTRL" && aClaUbi !== "LOGISTICA_CTRL") {


                    swal({
                        title: "Seguro que desea realizar la redistribución a " + Ubicacion + " ?",
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
                                $.ajax({
                                    url: "../RedistribucionTransaccional",
                                    data: {accion: "Redistribuir", Ubicacion: Ubicacion, IdLote: IdLote, CantMov: CantMov, Origen: Origen},
                                    type: 'POST',
                                    dataType: 'JSON',
                                    async: true,
                                    success: function (data) {
                                        if (data.msj) {
                                            swal({
                                                title: "Redistribución realizada correctamente!",
                                                text: "",
                                                type: "success"
                                            }, function () {
                                                //alert(ClaveUbica);
                                                //location.reload();
                                                window.location = "leerInsRedistClave.jsp?ClaPro=" + ClaveUbica + "";
                                            });
                                        } else {
                                            swal("Atención", "Verifica Datos de ubicacion, cantidad", "error");
                                        }

                                        /*if (jQuery.isEmptyObject(data)) {
                                         swal("Atención", "Redistribución no aplicada", "error");
                                         } else {
                                         $.each(data, function (idx, elem) {
                                         if (elem.Validado == "Validado") {
                                         swal({
                                         title: "Redistribución Realizado correctamente!",
                                         text: "",
                                         type: "success"
                                         }, function () {
                                         window.location = "DevolucionesFacturas?Accion=btnEliminar&folio=" + Folio + "";
                                         });                                                    
                                         } else {
                                         swal("Atención", "Redistribución no aplicada", "error");
                                         }
                                         });
                                         }*/
                                    }
                                });

                            });
//                        }

                } else {
                    swal({
                        title: "Ubicacion NO Permitida Para 'Controlados y Vacunas'",
                        text: "",
                        type: "error"
                    }, function () {
                        document.getElementById('F_ClaUbi').focus();
                        return false;
                    });
                }



            }
        } else {
            swal({
                title: "Verificar campos vacíos.!",
                text: "",
                type: "error"
            }, function () {
                document.getElementById('CantMov').focus();
                return false;
            });
        }


        /*
         var ordenCompra=$("#vOrden").val();
         var remision=$("#vRemi").val();
         $.ajax({
         url: "recepcionTransaccional",
         data: {accion: "IngresarRemision", ordenCompra: ordenCompra, remision: remision},
         type: 'POST',
         dataType: 'JSON',
         async: true,
         success: function (data)
         {
         if (data.msj)
         {
         alert("Compra validada con éxito");
         } else
         {
         alert("Error en la modificación contactar al departamento de sistemas.");
         }
         
         
         location.reload();
         
         
         }, error: function (jqXHR, textStatus, errorThrown) {
         alert("Error en sistema");
         }
         });
         
         
         */
    });
});


$('#F_ClaUbi').keyup(function () {
    var descripcion = $('#F_ClaUbi').val();
    var Origen = $('#Origen').val();
    $('#F_ClaUbi').autocomplete({
        source: "../JQInvenCiclico?accion=buscaClaUbi&descrip=" + descripcion + "&Origen=" + Origen,
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

$('#F_ClaUbi2').keyup(function () {
    var descripcion = $('#F_ClaUbi2').val();
    var Origen = $('#Origen').val();
    $('#F_ClaUbi2').autocomplete({
        source: "../JQInvenCiclico?accion=buscaClaUbi2&descrip=" + descripcion + "&Origen=" + Origen,
        minLenght: 2,
        select: function (event, ui) {
            $('#F_ClaUbi2').val(ui.item.F_ClaUbi2);
            return false;
        }
    }).data('ui-autocomplete')._renderItem = function (ul, item) {
        return $('<li>')
                .data('ui-autocomplete-item', item)
                .append('<a>' + item.F_ClaUbi2 + '</a>')
                .appendTo(ul);
    };
});