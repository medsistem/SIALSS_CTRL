$(function () {

    obtenerIdreg();

    $("#btnSave1").click(function () {
        var lote = $("#loteNuevo").val();
        var caducidad = $("#CaducidadNuevo").val();
        var cantidad = $("#CantidadNuevo").val();
        var cb = $("#CbNuevo").val();
        var marca = $("#marcaNuevo").val();
        var marca = $("#marcaNuevo").val();
        var tarimas = $("#tarimasNuevo").val();
        var cajas = $("#cajasNuevo").val();
        var pzacaja = $("#pzacajasNuevo").val();
        var cajasi = $("#cajasiNuevo").val();
        var resto = $("#restoNuevo").val();
        var id = $("#idCompraTemporal").val();
        var usuario = $("#UserActual").val();
        var costo = $("#costo").val();
        var factorEmpaque = $("#factorEmpaqueNuevo").val();
        
         var cantPedido = $("#cantPedido").val();
        var cantCompra = $("#cantCompra").val();
        var cantidadTemp = $("#cantidadTemp").val();
        
        console.log("Entra");
        var ingreso = ((parseInt(tarimas) * parseInt(cajas) * parseInt(pzacaja)) + (parseInt(cajasi) * parseInt(pzacaja) + parseInt(resto)) );
        console.log(ingreso);
        var piezas = parseInt(cantidadTemp) + parseInt(cantCompra) + ingreso ;
        console.log(piezas);
        if (piezas > parseInt(cantPedido)){
            alert("Excede la cantidad a recibir, Verificar cantidad");
            return false;
        } else if (ingreso <= 0){
            alert("la cantidad a recibir debe ser mayor a 0");
            return false;
        }
//        cajas = cajas * tarimas;
        if (lote === "") {
            alert("Ingresar un lote válido por favor.");
            return false;
        } else if (caducidad === "") {
            alert("ingresar una caducidad válida por favor.");
            return false;
        } else if (cantidad <= 0) {
            alert("ingresar una cantidad válida por favor.");
            return false;
        } else if (cb === "") {
            alert("ingresar una cb válido por favor.");
            return false;
        } else if (marca === "") {
            alert("ingresar una marca válida por favor.");
            return false;
        } else if (tarimas === "") {
            alert("ingresar Cantidad en Tarima válida por favor.");
            return false;
        } else if (cajas === "") {
            alert("ingresar Cantidad en Cajas válida por favor.");
            return false;
        } else if (pzacaja === "") {
            alert("ingresar Cantidad en Piezas x Cajas válida por favor.");
            return false;
        } else if (cajasi === "") {
            alert("ingresar Cantidad en Cajas x Tarimas Incompletas válida por favor.");
            return false;
        } else if (resto === "") {
            alert("ingresar Cantidad en Resto válida por favor.");
            return false;
//      
        }else if (factorEmpaque === "") {
            alert("ingresar Cantidad en Factor de Empaque válida por favor.");
            return false;
        }  else {
            $.ajax({
                url: "recepcionTransaccional",
//                data: {accion: "EditarLotes", id: id, lote: lote, caducidad: caducidad, cantidad: cantidad, cb: cb, marca: marca, tarimas: tarimas, cajas: cajas, pzacaja: pzacaja, cajasi: cajasi, resto: resto, usuario: usuario, costo: costo},
                data: {accion: "EditarLotes", id: id, lote: lote, caducidad: caducidad, cantidad: cantidad, cb: cb, marca: marca, tarimas: tarimas, cajas: cajas, pzacaja: pzacaja, cajasi: cajasi, resto: resto, usuario: usuario, costo: costo, factorEmpaque:factorEmpaque},
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
    
    $("#btnRes1").click(function () {
        var lote = $("#loteNuevo").val();
        var caducidad = $("#CaducidadNuevo").val();
        var cantidad = $("#CantidadNuevo").val();
        var cb = $("#CbNuevo").val();
        var marca = $("#marcaNuevo").val();
        var marca = $("#marcaNuevo").val();
        var tarimas = $("#tarimasNuevo").val();
        var cajas = $("#cajasNuevo").val();
        var pzacaja = $("#pzacajasNuevo").val();
    
        var id = $("#idCompraTemporal").val();
        var usuario = $("#UserActual").val();
        var costo = $("#costo").val();
      
        
         var cantPedido = $("#cantPedido").val();
        var cantCompra = $("#cantCompra").val();
        var cantidadTemp = $("#cantidadTemp").val();
        
        console.log("Entra");
        var ingreso = parseInt(cajas);
        console.log("ingreso "+ingreso);
        var piezas =  ingreso ;
        console.log(piezas);
       if (ingreso <= 0){
            alert("la cantidad a recibir debe ser mayor a 0");
            return false;
        }
//        cajas = cajas * tarimas;
        if (lote === "") {
            alert("Ingresar un lote válido por favor.");
            return false;
        } else if (caducidad === "") {
            alert("ingresar una caducidad válida por favor.");
            return false;
        } else if (cantidad <= 0) {
            alert("ingresar una cantidad válida por favor.");
            return false;
        } else if (cb === "") {
            alert("ingresar una cb válido por favor.");
            return false;
        } else if (marca === "") {
            alert("ingresar una marca válida por favor.");
            return false;
        } else if (tarimas === "") {
            alert("ingresar Cantidad en Tarima válida por favor.");
            return false;
        } else if (cajas === "") {
            alert("ingresar Cantidad en Cajas válida por favor.");
            return false;
        }  else {
            $.ajax({
                url: "recepcionTransaccional",
//                data: {accion: "EditarLotes", id: id, lote: lote, caducidad: caducidad, cantidad: cantidad, cb: cb, marca: marca, tarimas: tarimas, cajas: cajas, pzacaja: pzacaja, cajasi: cajasi, resto: resto, usuario: usuario, costo: costo},
                data: {accion: "EditarLotesRes", id: id, lote: lote, caducidad: caducidad, cantidad: cantidad, cb: cb, marca: marca, tarimas: tarimas, cajas: cajas, usuario: usuario, costo: costo},
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
                    alert("Error en sistema res");
                }
            });
        }


    });


    $("#validarRemision").click(function () {
        var ordenCompra = $("#vOrden").val();
        var remision = $("#vRemi").val();
        var UbicaN = $("#UbicaN option:selected").val();
        var origen = $("#Origen").val();

        if ($('#UbicaN').val().trim() === '0') {
            swal({
                title: "¿Seleccione Una Ubicacion?",
                text: "Seleccionar Una Ubicacion Valida:" + UbicaN,
                type: "warning",
                //  showCancelButton: true,
                closeOnConfirm: false,

            });
        } else {

            swal({
                title: "¿Seguro de Validar la Compra?",
                text: "No podrás deshacer este paso, La ubicacion es: " + UbicaN,
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
                            url: "recepcionTransaccional",
                            data: {accion: "IngresarRemision", ordenCompra: ordenCompra, remision: remision, UbicaN: UbicaN},
                            type: 'POST',
                            dataType: 'JSON',
                            async: true,
                            success: function (data) {
                                if (data.msj) {
                                    swal({
                                    title: "Compra validada correctamente!",
                                    text: "",
                                    type: "success"
                                }, function () {
                                    location.reload();
                                    //window.location = "verificarCompraAuto.jsp?vOrden=" + vOrden + "&vRemi=" + vRemi + "";
                                });
                                }
                                if (origen === "19") {
                                    swal({
                                    title: "Ingresar datos en WMS para registrar las cantidades de insumo asignado por unidad!",
                                    text: "http://gnklbajio.local/",
                                    type: "success"
                                     
                                }, function () {
                                    location.reload();
                                    //window.location = "verificarCompraAuto.jsp?vOrden=" + vOrden + "&vRemi=" + vRemi + "";
                                });
                                }
                                
                                else {
                                    alert("Error en la modificación contactar al departamento de sistemas.");
                                }
                                location.reload();
                            }, error: function (jqXHR, textStatus, errorThrown) {
                                alert("Error en sistema");
                            }
                        });
                    });
        }
    });

    $("#validarRemisionCross").click(function () {
        var ordenCompra = $("#vOrden").val();
        var remision = $("#vRemi").val();
        $.ajax({
            url: "recepcionTransaccional",
            data: {accion: "IngresarRemisionCross", ordenCompra: ordenCompra, remision: remision},
            type: 'POST',
            dataType: 'JSON',
            async: true,
            success: function (data) {
                if (data.msj) {
                    alert("Compra validada con éxito");
                } else {
                    alert("Error en la modificación contactar al departamento de sistemas.");
                }
                location.reload();
            }, error: function (jqXHR, textStatus, errorThrown) {
                alert("Error en sistema");
            }
        });
    });
});

function obtenerIdreg() {
    var vOrden = $('#vOrden').val();
    var vRemi = $('#vRemi').val();
    var tipoRV = $('#tipoRV').val();
    var vFac = "";
     var origen = $('#Origen').val();
    //alert(vOrden + " / " + vRemi);
    if (tipoRV == 2) {
        vFac = vRemi;
        vRemi = "";
    }
    $.ajax({
        type: "GET",
        url: "recepcionTransaccional?accion=obtenerIdReg&vOrden=" + vOrden + "&vRemi=" + vRemi + "&vFac=" + vFac + "",
        dataType: "json",
        async: false,
        success: function (data) {
            $.each(data, function (idx, elem) {
                var isValidado;
                isValidado = elem.validado;

                //alert(elem.IdReg);
                $('#Validar_' + elem.IdReg).click(function () {
                    var UbicaN = $('#UbicaN option:selected').val();

                    if ($('#UbicaN').val().trim() === '0') {
                        swal({
                            title: "¿Seleccione Una Ubicacion?",
                            text: "Seleccionar Una Ubicacion Valida:" + UbicaN,
                            type: "warning",
                            //  showCancelButton: true,
                            closeOnConfirm: false,

                        });
                    } else {

                        swal({
                            title: "¿Seguro de Validar Parcial?",
                            text: "No podrás deshacer este paso, La ubicacion es: " + UbicaN,
                            type: "warning",
                            showCancelButton: true,
                            //cancelButtonText: "Mejor no",
                            closeOnConfirm: false,
                            confirmButtonColor: "#DD6B55",
                            showLoaderOnConfirm: true,
                            confirmButtonText: "Continuar!"
                        },
                                function () {
                                    $('#Validar_' + elem.IdReg).prop('disabled', true);
                                    $.ajax({
                                        url: "recepcionTransaccional",
                                        data: {accion: "RegistrarDatosParcial", IdReg: elem.IdReg, vOrden: vOrden, vRemi: vRemi, UbicaN: UbicaN},
                                        type: 'POST',
                                        dataType: 'JSON',
                                        async: true,
                                        success: function (data) {
                                            if (data.msj) {
                                                swal({
                                                    title: "Validación parcial realizada correctamente!",
                                                    text: "",
                                                    type: "success"
                                                }, function () {
                                                    location.reload();
                                                    //window.location = "verificarCompraAuto.jsp?vOrden=" + vOrden + "&vRemi=" + vRemi + "";
                                                });
                                                if (origen === "19") {
                                    swal({
                                    title: "Ingresar datos en WMS para registrar las cantidades de insumo asignado por unidad!",
                                    text: "http://medalfa.local/#/login",
                                    type: "success"
                                     
                                }, function () {
                                    location.reload();
                                    //window.location = "verificarCompraAuto.jsp?vOrden=" + vOrden + "&vRemi=" + vRemi + "";
                                });
                                }
                                            } else {
                                                swal("Atención", "Datos no aplicados", "error");
                                                $('#Validar_' + elem.IdReg).prop('disabled', false);
                                                //location.reload();
                                            }
                                        }
                                    });
                                });
                    }
                });
            });
        }
    });

}



