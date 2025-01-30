
$(document).ready(function () {
    $('#datosfolios').dataTable();

    obtenerIdreg();
    obtenerProyecto();

    $('#btnDevolucion').click(function () {
        var Folio = $("#foliod").val();
        var Obs = $("#Obs").val();
        if (Obs != "") {
            swal({
                title: "¿Seguro que deseas aplicar la Devolución?",
                text: "No podrás deshacer este paso...",
                type: "warning",
                showCancelButton: true,
                //cancelButtonText: "Mejor no",
                closeOnConfirm: false,
                confirmButtonColor: "#DD6B55",
                showLoaderOnConfirm: true,
                confirmButtonText: "Adelante!"
            },
                    function () {
                        //setTimeout(function () {
                        $.ajax({
                            type: "GET",
                            url: "ConsultaModificaFolio?accion=validaDevolucion&foliosel=+" + Folio + "&Obs=" + Obs + "",
                            dataType: "json",
                            success: function (data) {
                                if (jQuery.isEmptyObject(data)) {
                                    swal("Atención", "Devolución no aplicada", "error");
                                } else {
                                    $.each(data, function (idx, elem) {
                                        if (elem.Validado == "Validado") {
                                            swal({
                                                title: "Devolución aplicado correctamente!",
                                                text: "",
                                                type: "success"
                                            }, function () {
                                                //location.reload();
                                                window.location = "ModificarFolio?Accion=btnEliminar&folio=" + Folio + "";
                                            });
                                            //document.getElementById('Loader').style.display = 'none';                                                    
                                        } else {
                                            swal("Atención", "Devolución no aplicada", "error");
                                        }
                                    });
                                }
                            }
                        });

                    });
        } else {
            swal("Atención", "Favor de llenar campo de observaciones", "warning");
        }
    });

});

function obtenerIdreg() {
    var Folio = $('#fol_gnkl').val();
    
    $.ajax({
        type: "GET",
        url: "ConsultaModificaFolio?accion=obtenerIdReg&foliosel=" + Folio + "",
        dataType: "json",
        async: false,
        success: function (data) {
            $.each(data, function (idx, elem) {
                var isValidado;
                isValidado = elem.validado;
                $('#btnEliminar' + elem.IdReg + '').click(function () {
                    $.ajax({
                        type: "GET",
                        url: "ConsultaModificaFolio?accion=EliminarReg&idreg=" + elem.IdReg + "&foliosel=+" + Folio + "",
                        dataType: "json",
                        success: function (data) {
                            if (jQuery.isEmptyObject(data)) {
                                swal("Atención", "Registro no eliminado", "warning");
                            } else {
                                $.each(data, function (idx, elem) {
                                    if (elem.Procesado == "Terminado") {
                                        //location.reload();
                                        window.location = "ModificarFolio?Accion=btnEliminar&folio=" + Folio + "";
                                    } else {
                                        swal("Atención", "Registro no eliminado", "warning");
                                    }
                                });
                            }
                        }
                    });
                });
            });
        }
    });
}

function obtenerProyecto() {
    $('#Nombre option:gt(0)').remove();
    $("#Nombre").append("<option value=0>Seleccione</option>").select2();
    $('#Nombre option:gt(0)').remove();
    $.ajax({
        url: "ExistenciaProyecto",
        data: {accion: "obtenerProyectos"},
        type: 'GET',
        dataType: 'JSON',
        async: true,
        success: function (data) {
            $.each(data, function (i, valueProyecto) {
                $("#Nombre").append("<option value=" + valueProyecto.Id + ">" + valueProyecto.Nombre + "</option>").select2();
            });
        }
    });
}