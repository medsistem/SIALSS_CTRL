
$(document).ready(function () {
    $('#datosfolios').dataTable();

    obtenerIdreg();

    $('#BtnConfirmar').click(function () {
        var Obs = $("#obs").val();
        var Tipo = $("#Tipo").val();
        var Proyecto = $("#Proyecto").val();
        var OC = $("#OC").val();
        if (Obs != "") {
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
                        //setTimeout(function () {
                        $.ajax({
                            url: "FacturacionTran",
                            data: {accion: "ConformarFactTempSemiCause", Obs: Obs, Tipo: Tipo, Proyecto: Proyecto, OC: OC},
                            type: 'POST',
                            dataType: 'JSON',
                            async: true,
                            success: function (data) {
                                if (data.msj) {
                                    swal({
                                        title: "Facturación realizada correctamente!",
                                        text: "",
                                        type: "success"
                                    }, function () {
                                        //location.reload();
                                        window.location = "FacturacionTran?accion=TerminoCapturaSemiCause";
                                    });

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

    $('#BtngenerarRemision').click(function () {
        //alert("ffffaa");
    });

});

function validaCantidad(e) {
    var cantidadSol = document.getElementById('Cantidad').value;
    document.getElementById('Cant' + e).value = cantidadSol;
    var cantidadAlm = document.getElementById('CantAlm_' + e).value;
    if (parseInt(cantidadSol) > parseInt(cantidadAlm)) {
        //alert('La cantidad a facturar no puede ser mayor a la cantidad de esa ubicación');
        swal("Verificar", "La cantidad a facturar no puede ser mayor a la cantidad seleccionada", "error");
        return false;
    }
}

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
                                    window.location = "FacturacionTran?accion=RegresarCapturaSemiCause";
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