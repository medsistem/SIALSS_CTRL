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
        var Ubicacion = $("#F_ClaUbi").val();
        var aClaUbi = $("#aClaUbi").val();
        var aCbUbica = $("#aCbUbica").val();
        var IdLote = $("#F_IdLote").val();
        var aCbUbica = $("#aCbUbica").val();
        var aCbUbica = $("#aCbUbica").val();


        Ubicacion = Ubicacion.toUpperCase();

        if ((CantMov != "") && (Ubicacion != "")) {

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
                if (Ubicacion === aClaUbi || Ubicacion === aCbUbica) {
                    swal({
                        title: "La Ubicación no puede ser igual a la anterior.!",
                        text: "",
                        type: "error"
                    }, function () {
                        document.getElementById('F_ClaUbi').focus();
                        return false;
                    });
                } else {
                    if(!Ubicacion.startsWith("AF")){
                        swal({
                            title: "No se puede mover a esta ubicación!",
                            text: "",
                            type: "error"
                        }, function () {
                            document.getElementById('F_ClaUbi').focus();
                            return false;
                        });
                    }else{
                    swal({
                        title: "Seguro que desea hacer la redistribución.?",
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
                                    url: "RedistribucionTransaccional",
                                    data: {accion: "Redistribuir", Ubicacion: Ubicacion, IdLote: IdLote, CantMov: CantMov},
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.msj) {
                                            swal({
                                                title: "Redistribución realizada correctamente!",
                                                text: "",
                                                type: "success"
                                            }, function () {
                                                //location.reload();
                                                window.location = "DevolucionesFacturas?Accion=btnEliminar&folio=" + Folio + "";
                                            });
                                        } else {
                                            swal("Atención", "Redistribución no aplicada", "error");
                                        }

                                        /*if (jQuery.isEmptyObject(data)) {
                                            swal("Atención", "Redistribución no aplicada", "error");
                                        } else {
                                            $.each(data, function (idx, elem) {
                                                if (elem.Validado == "Validado") {
                                                    swal({
                                                        title: "Redistribución realizada correctamente!",
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
                        }
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



