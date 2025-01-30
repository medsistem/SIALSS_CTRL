
$(document).ready(function () {
    $('#datosfolios').dataTable();
    BuscarEnseres();
    //obtenerIdreg();


    $('#GuardarTemp').click(function () {
        var Unidad = $("#ClaCli").val();
        var ClaPro = $("#ClaPro").val();
        var FechaEnt = $("#FechaEnt").val();
        var Cantidad = $("#Cantidad").val();
        var folio = $("#folio").val();
        if ((Unidad != "") && (ClaPro != "") && (FechaEnt != "") && (parseInt(Cantidad) > 0) && (parseInt(folio) > 0)) {
            $.ajax({
                url: "FacturacionEnseres",
                data: {accion: "InsertaEnseres", Unidad: Unidad, ClaPro: ClaPro, FechaEnt: FechaEnt, Cantidad: Cantidad, Folio: folio},
                type: 'POST',
                dataType: 'JSON',
                async: true,
                success: function (data) {
                    if (data.msj) {
                        swal({
                            title: "Captura realizada correctamente!",
                            text: "",
                            type: "success"
                        }, function () {
                            MostrarRegistro();
                            BuscarEnseres();
                            $("#Cantidad").val("");
                        });

                    } else {
                        swal("Datos no Registrados", "", "error");
                        //location.reload();
                    }

                }
            });
            $("#Cantidad").val("");
        } else {
            swal("Llenar todos campos", "", "error");
        }
    });

    $('#CancelarFactura').click(function () {
        var Unidad = $("#ClaCli").val();
        var folio = $("#folio").val();
        swal({
            title: "¿Seguro de Cancelar la Factura?",
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
                        url: "FacturacionEnseres",
                        data: {accion: "EliminarCaptura", Unidad: Unidad, Folio: folio, Cantidad: 0},
                        type: 'POST',
                        dataType: 'JSON',
                        async: true,
                        success: function (data) {
                            if (data.msj) {
                                swal({
                                    title: "Cancelación realizada correctamente!",
                                    text: "",
                                    type: "success"
                                }, function () {
                                    window.location = "FacturacionEnseres?accion=TerminoCaptura&Unidad=" + Unidad + "&Folio=" + folio + "";
                                });

                            } else {
                                swal("Cancelación no Aplicada", "", "error");
                                //location.reload();
                            }

                        }
                    });

                });

    });
    $('#BtnConfirmar').click(function () {
        var Obs = $("#obs").val();
        var Unidad = $("#ClaCli").val();
        var folio = $("#folio").val();
        var Proyecto = $("#Proyecto").val();
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
                            url: "FacturacionEnseres",
                            data: {accion: "ConfirmarFactTempEnseres", Obs: Obs, Unidad: Unidad, Folio: folio, Proyecto: Proyecto, Cantidad: 0},
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
                                        window.location = "FacturacionEnseres?accion=TerminoCaptura&Unidad=" + Unidad + "&Folio=" + folio + "";
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

function BuscarEnseres() {
    var Unidad = $("#ClaCli").val();
    var folio = $("#folio").val();
    $('#ClaPro option:gt(0)').remove();
    $("#ClaPro").append("<option value=0>Seleccione</option>").select2();
    $('#ClaPro option:gt(0)').remove();
    $.ajax({
        url: "FacturacionEnseres",
        data: {accion: "MostrarEnseres", Unidad: Unidad, Folio: folio},
        type: 'GET',
        dataType: 'JSON',
        async: true,
        success: function (data) {
            $.each(data, function (i, valueOC) {
                $("#ClaPro").append("<option value=" + valueOC.Id + ">" + valueOC.Descripcion + "</option>").select2();
            });
        }
    });
}

function MostrarRegistro() {
    var Unidad = $("#ClaCli").val();
    var folio = $("#folio").val();

    $.ajax({
        url: "FacturacionEnseres",
        data: {accion: "MostrarRegistros", Unidad: Unidad, Folio: folio},
        type: 'GET',
        async: false,
        success: function (data)
        {
            limpiarTabla();
            MostrarTabla(data);
        }, error: function (jqXHR, textStatus, errorThrown) {
            alert("Error grave contactar con el departamento de sistemas");
        }
    });
}

function limpiarTabla() {
    $("#example").remove();
}

function MostrarTabla(data) {
    var json = JSON.parse(data);
    var aDataSet = [];
    var Contar, CantidadT, ContarClave;
    for (var i = 0; i < json.length; i++) {

        var Descripcion = json[i].Descripcion;
        var UM = json[i].UM;
        var Cantidad = json[i].Cantidad;
        var Id = json[i].Id;
        var BtnEliminar = '<button class="btn btn-block btn-info" onclick="Eliminar(' + Id + ')" type="button"  ><span class="glyphicon glyphicon-trash"></span></button>';

        aDataSet.push([Descripcion, UM, Cantidad, BtnEliminar]);
    }

    $(document).ready(function () {
        $('#dynamic').html('<table class="table table-striped table-bordered table-condensed"  id="example"></table>');
        $('#example').dataTable({
            "aaData": aDataSet, "button": 'aceptar',
            "bScrollInfinite": true,
            "bScrollCollapse": true,
            //"sScrollY": "600px",
            "bFooter": true,
            "bProcessing": true,
            "sPaginationType": "full_numbers",
            "bAutoWidth": false,
            "order": [[0, "desc"]],
            "aoColumns": [
                {"sTitle": "Descripción", "sClass": "center"},
                {"sTitle": "UM", "sClass": "center"},
                {"sTitle": "Cantidad", "sClass": "center"},
                {"sTitle": "Eliminar", "sClass": "center"}
            ]
        });
    });
}


function Eliminar(IdReg) {
    var Unidad = $("#ClaCli").val();
    var folio = $("#folio").val();
    swal({
        title: "Eliminar",
        text: "¿Está seguro de Eliminar el Registro?",
        type: "warning",
        showCancelButton: true,
        confirmButtonClass: "btn-info",
        confirmButtonText: "Aceptar",
        cancelButtonText: "Cancelar",
        closeOnConfirm: false,
        showLoaderOnConfirm: true
    },
            function (isConfirm) {
                if (isConfirm) {
                    $.ajax({
                        type: "GET",
                        url: "FacturacionEnseres?accion=EliminarRegistro&idreg=" + IdReg + "&Unidad=" + Unidad + "&Folio=" + folio + "",
                        dataType: "json",
                        success: function (data) {
                            if (data.msj) {
                                swal({
                                    title: "Eliminación realizada correctamente!",
                                    text: "",
                                    type: "success"
                                }, function () {
                                    MostrarRegistro();
                                });

                            } else {
                                swal("Cancelación no Aplicada", "", "error");
                                //location.reload();
                            }

                        }
                    });

                }
            });

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
                                    window.location = "FacturacionTran?accion=RegresarCaptura";
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