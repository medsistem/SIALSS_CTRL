

$(document).ready(function () {
    $('#datosfolios').dataTable();

    obtenerIdreg();

    $('#BtnConfirmar').click(function () {//modificar folio
        var Proyecto = $("#Proyecto").val();
        var FolioS = $("#FolioS").val();
        swal({
            title: "¿Seguro de Modificar la Factura?",
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
                    //setTimeout(function () {
                    $.ajax({
                        url: "FacturacionTran",
                        data: {accion: "ConformarFactTempFOLIO", Proyecto: Proyecto},
                        type: 'POST',
                        dataType: 'JSON',
                        async: true,
                        success: function (data) {
                            if (data.msj) {
                                swal({
                                    title: "Modificar Facturación realizada correctamente!",
                                    text: "",
                                    type: "success"
                                }, function () {
                                    //location.reload();
                                    window.location = "ModificarFolio?Accion=btnMostrar&Folio=" + FolioS + "";
                                });

                            } else {
                                swal("Datos no Registrados validar Datos", "", "error");
                                //location.reload();
                            }

                        }
                    });

                });

    });

   

});

function validaCantidad(e) {
    var cantidadSol = document.getElementById('Cantidad').value;
    document.getElementById('Cant' + e).value = cantidadSol;
    var cantidadAlm = document.getElementById('CantAlm_' + e).value;
    
//    if (parseInt(cantidadSol) > parseInt(cantidadAlm)) {
//        //alert('La cantidad a facturar no puede ser mayor a la cantidad de esa ubicación');
//        swal("Verificar", "La cantidad a facturar no puede ser mayor a la cantidad seleccionada", "error");
//        return false;
//    }
}

function obtenerIdreg() {
    var ClaPro = $('#ClaPro').val();
    var Cantidad = $('#Cantidad').val();
    var GCUnidad = $("#ClaCli").val();
    var IdLot = $("#IdLot").val();
    $.ajax({
        type: "GET",
        url: "FacturacionTran?accion=obtenerIdReg&Clapro=" + ClaPro + "&GCUnidad=" + GCUnidad,
        dataType: "json",
        async: false,
        
        success: function (data) {
            $.each(data, function (idx, elem) {
                var isValidado;
                isValidado = elem.validado;

                //alert(elem.IdReg);
                $('#BtnAgregar_' + elem.IdReg).click(function () {

                  //  if (parseInt(Cantidad) > parseInt(elem.Existencia)) {
                    //
                    //        swal("Verificar", "La cantidad a facturar no puede ser mayor a la cantidad seleccionada", "error");
                   // } else {
                      if (parseInt(elem.Existencia) === 0) {
                        swal("Verificar", "No hay existencia", "error");
                        
//                //    } else if (parseInt(elem.Existencia) > 0 && parseInt(elem.Existencia) < parseInt(Cantidad)) {
//                      swal({
//                          title:"Verificar", 
//                          text: "La cantidad solicitada es mayor se tomara la disponible: " + parseInt(elem.Existencia),
//                          type: "warning",
//                          confirmButtonClass: "btn-danger",
//                          closeOnConfirm: false
//                      });    
//                        
//                        $('#BtnAgregar_' + elem.IdReg).prop('disabled', true);
//                        $.ajax({
//                            url: "FacturacionTran",
//                            data: {accion: "RegistrarDatos", IdLote: elem.IdReg, CantMov: Cantidad},
//                            type: 'POST',
//                            dataType: 'JSON',
//                            async: true,
//                            success: function (data) {
//                                if (data.msj) {
//                                    window.location = "FacturacionTran?accion=RegresarCapturaFOLIO";
//                                } else {
//                                    swal("Atención", "No se puede agregar el mismo lote", "error");
//                                    location.reload();
//                                }
//
//                            }
//                        });
//                    }//fin del if
//                    else{
//                    $('#BtnAgregar_' + elem.IdReg).prop('disabled', true);
//                    $.ajax({
//                            url: "FacturacionTran",
//                            data: {accion: "RegistrarDatos", IdLote: elem.IdReg, CantMov: Cantidad},
//                            type: 'POST',
//                            dataType: 'JSON',
//                            async: true,
//                            success: function (data) {
//                                if (data.msj) {
//                                    window.location = "FacturacionTran?accion=RegresarCapturaFOLIO";
//                                } else {
//                                    
//                                    swal("Verificar", "No se puede agregar el mismo lote","success").then(function() {
//                                        location.reload();
//                                    });
//                                  
//                                     //window.location = "facturacionManualSelectLote.jsp";
//                                }
//                              
//                            }
//                        });
//                }
//
//                });
//            });
//        }
//    });
//     swal("Verificar", "No hay existencia", "error");
                    } else if (parseInt(elem.Origen) === 19 && parseInt(elem.Existencia2) < parseInt(elem.Existencia) && parseInt(Cantidad) > parseInt(elem.Existencia2)) {
                        swal({
                            title: "Verificar",
//                            text: "ya mamo",
                            text: "La cantidad solicitada no esta disponible en ubicación para remisionar, se tomará la existencia en ubicación: " + parseInt(elem.Existencia2),
                            type: "warning",
                            showCancelButton: true,
                            closeOnConfirm: false,
                            confirmButtonColor: "#DD6B55",
                            showLoaderOnConfirm: true,
                            confirmButtonText: "Continuar!"
                        });
                        $('#BtnAgregar_' + elem.IdReg).prop('disabled', true);
                        $.ajax({
                            url: "FacturacionTran",
                            data: {accion: "RegistrarDatos", IdLote: elem.IdReg, CantMov: parseInt(elem.Existencia2)},
                            type: 'POST',
                            dataType: 'JSON',
                            async: true,
                            success: function (data) {
                                if (data.msj) {
                                    window.location = "FacturacionTran?accion=RegresarCapturaFOLIO";
                                } else {

                                    swal("Verificar", "No se puede agregar el mismo lote", "success").then(function () {
                                        location.reload();
                                    });

                                    //window.location = "facturacionManualSelectLote.jsp";
                                }

                            }
                        });
                        
                    } else if (parseInt(elem.Existencia) > 0 && parseInt(elem.Existencia) < parseInt(Cantidad)) { ///cuando la cantidad es mayor
                        swal({
                            title: "Verificar",
                            text: "La cantidad solicitada es mayor se tomara la disponible: " + parseInt(elem.Existencia),
                            type: "warning",
                            showCancelButton: true,
                            closeOnConfirm: false,
                            confirmButtonColor: "#DD6B55",
                            showLoaderOnConfirm: true,
                            confirmButtonText: "Continuar!"
                        });

                        $('#BtnAgregar_' + elem.IdReg).prop('disabled', true);
                      

                        $.ajax({
                            url: "FacturacionTran",
                            data: {accion: "RegistrarDatos", IdLote: elem.IdReg, CantMov: Cantidad},
                            type: 'POST',
                            dataType: 'JSON',
                            async: true,
                            success: function (data) {
                                if (data.msj) {
                                    window.location = "FacturacionTran?accion=RegresarCapturaFOLIO";
                                } else {

                                    swal("Verificar", "No se puede agregar el mismo lote", "success").then(function () {
                                        location.reload();
                                    });

                                    //window.location = "facturacionManualSelectLote.jsp";
                                }

                            }
                        });

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
                                    window.location = "FacturacionTran?accion=RegresarCapturaFOLIO";
                                } else {

                                    swal("Verificar", "No se puede agregar el mismo lote", "success").then(function () {
                                        location.reload();
                                    });

                                    //window.location = "facturacionManualSelectLote.jsp";
                                }

                            }
                        });
                    }

                });
            });
        }
    });

}