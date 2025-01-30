
$(document).ready(function () {
    $('#Editar Requerimiento').click(function () {
        var Folio = $("#fol_gnkl").val();
        var Unidad = $("#Unidad").val();
        var ClaCli = $("#ClaCli").val();

        swal({
            title: "¿Seguro de procesar el Requerimiento " + Folio + "?",
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
                        url: "../EditarRequerimiento",
                        data: {accion: "EditarRequerimiento", Unidad: Unidad, Folio: Folio, ClaCli: ClaCli},
                        type: 'POST',
                        dataType: 'JSON',
                        async: true,
                        success: function (data) {
                            if (data.msj) {
                                swal({
                                    title: "Requerimiento Procesado correctamente!",
                                    text: "",
                                    type: "success"
                                }, function () {
                                    window.location = "ProcesarRequeFarmacia.jsp?UnidadSe=" + Unidad + "";
                                });

                            } else {
                                swal("Datos no Registrados", "", "error");
                            }

                        }
                    });

                });
    });
});