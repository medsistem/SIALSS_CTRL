
$(document).ready(function () {
    $('#datosfolios').dataTable();

    obtenerIdreg();

    $('#BtnConfirmar').click(function () {
        var Proyecto = $("#Proyecto").val();
        var ProyectoFinal = $("#ProyectoFinal").val();
        var Obs = $("#obs").val();
        if (Obs != "") {
            if (ProyectoFinal != "0") {
                swal({
                    title: "¿Seguro de confirmar la Transferencia?",
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
                                data: {accion: "ConfirmarTranferenciaProyecto", Obs: Obs, Proyecto: Proyecto, ProyectoFinal: ProyectoFinal},
                                type: 'POST',
                                dataType: 'JSON',
                                async: true,
                                success: function (data) {
                                    if (data.msj) {
                                        swal({
                                            title: "Transferencia realizada correctamente!",
                                            text: "",
                                            type: "success"
                                        }, function () {
                                            //location.reload();
                                            window.location = "FacturacionTran?accion=TerminoCapturaTransfer";
                                        });

                                    } else {
                                        swal("Datos no Registrados", "", "error");
                                        //location.reload();
                                    }

                                }
                            });

                        });
            } else {
                swal("Atención", "Favor de seleccionar un Proyecto", "warning");
            }
        } else {
            swal("Atención", "Favor de llenar campo de observaciones", "warning");
        }
    });

    $('#BtngenerarRemision').click(function () {
        //alert("ffffaa");
    });

});

function validaCantidad(e) {
    var cantidadSol = document.getElementById('Cantidad').value;
    document.getElementById('Cant' + e).value = cantidadSol;
    var cantidadAlm = document.getElementById('CantAlm_' + e).value;
     var LoteTran =  document.getElementById('FolLot').value;
     
    swal(LoteTran);
    if (parseInt(cantidadSol) > parseInt(cantidadAlm) ) {
       
        swal("Verificar", "La cantidad a transferir no puede ser mayor a la cantidad seleccionada", "error");
        return false;
    }
    if (parseInt(LoteTran) === 2) {
       
        swal("Verificar", "El Lote ya esta registrado", "error");
        return false;
    }
     
}

//function validaLote(e) {
//   var LoteTran = document.getElementsById('IdLotval').value;
//     document.getElementById('IdLotval').value = LoteTran;
//     if (LoteTran = 2) {
//       
//        swal("Verificar", "El Lote ya esta registrado", "error");
//        return false;
//    }
//}

function obtenerIdreg() {
    var ClaPro = $('#ClaPro').val();
    var Cantidad = $('#Cantidad').val();

    $.ajax({
        type: "GET",
        url: "FacturacionTran?accion=obtenerIdReg&Clapro=" + ClaPro + "",
        dataType: "json",
        async: false,
        success: function (data) {
            $.each(data, function (idx, elem) {
                var isValidado;
                isValidado = elem.validado;

                //alert(elem.IdReg);
                $('#BtnAgregar_' + elem.IdReg).click(function () {

                    if (parseInt(Cantidad) > parseInt(elem.Existencia)) {
                        swal("Verificar", "La cantidad a transferir no puede ser mayor a la cantidad seleccionada", "error");
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
                                    window.location = "FacturacionTran?accion=RegresarCapturaTranferProductoProy";
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