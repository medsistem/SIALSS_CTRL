$(document).ready(function () {


    $('#BtnSugerencia').click(function () {
        var ObsGral = $('#ObsGral').val();
        var Solicitante = $('#Solicitante').val();
        if (ObsGral != "") {
            if (Solicitante != "") {
                swal({
                    title: "¿Seguro de Registrar la Sugerencia?",
                    text: "No podrás deshacer este paso...",
                    type: "warning",
                    showCancelButton: true,
                    closeOnConfirm: false,
                    confirmButtonColor: "#DD6B55",
                    showLoaderOnConfirm: true,
                    confirmButtonText: "Continuar!"
                },
                        function () {
                            $.ajax({
                                url: "FacturacionTran",
                                data: {accion: "ConfirmarSugerencia", ObsGral: ObsGral, Solicitante: Solicitante},
                                type: 'GET',
                                dataType: 'JSON',
                                async: true,
                                success: function (data) {
                                    if (data.msj) {
                                        swal({
                                            title: "Sugerencia realizada correctamente!",
                                            text: "",
                                            type: "success"
                                        }, function () {
                                            window.location = "RequerimientoTran?accion=TerminoSugerencia";
                                        });
                                    } else {
                                        swal("Datos no Registrados", "", "error");
                                    }

                                }
                            });
                        });
            } else {
                swal("Atención", "Favor de llenar campo Nombre del Solicitante", "warning");
            }
        } else {
            swal("Atención", "Favor de llenar campo de Sugerencia", "warning");
        }
    });

$('#BtnSugerenciaCompra').click(function () {
        var ObsGral = $('#ObsGral').val();
        var Solicitante = $('#Solicitante').val();
        if (ObsGral != "") {
            if (Solicitante != "") {
                swal({
                    title: "¿Seguro de Registrar la Sugerencia?",
                    text: "No podrás deshacer este paso...",
                    type: "warning",
                    showCancelButton: true,
                    closeOnConfirm: false,
                    confirmButtonColor: "#DD6B55",
                    showLoaderOnConfirm: true,
                    confirmButtonText: "Continuar!"
                },
                        function () {
                            $.ajax({
                                url: "FacturacionTran",
                                data: {accion: "ConfirmarSugerenciaCompra", ObsGral: ObsGral, Solicitante: Solicitante},
                                type: 'GET',
                                dataType: 'JSON',
                                async: true,
                                success: function (data) {
                                    if (data.msj) {
                                        swal({
                                            title: "Sugerencia realizada correctamente!",
                                            text: "",
                                            type: "success"
                                        }, function () {
                                            window.location = "FacturacionTran?accion=TerminoSugerenciaCompra";
                                        });
                                    } else {
                                        swal("Datos no Registrados", "", "error");
                                    }

                                }
                            });
                        });
            } else {
                swal("Atención", "Favor de llenar campo Nombre del Solicitante", "warning");
            }
        } else {
            swal("Atención", "Favor de llenar campo de Sugerencia", "warning");
        }
    });
    
});