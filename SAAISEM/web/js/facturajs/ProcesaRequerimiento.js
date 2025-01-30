function procesar(renglon) {
    var Folio = $("#fol_gnkl_" + renglon).val();
    var Unidad = $("#Unidad_" + renglon).val();
    var ClaCli = $("#ClaCli_" + renglon).val();

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
                    url: "../ProcesarRequerimiento",
                    data: {accion: "ProcesarRequerimiento", Unidad: Unidad, Folio: Folio, ClaCli: ClaCli},
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
                                location.reload();
                            });

                        } else {
                            swal("Datos no Registrados", "", "error");
                        }

                    }
                });

            });
}
;


function guardarEditado(id, confirm) {
    var cantidad = $("#input_" + id).val();
    cantidad = parseInt(cantidad);
    if (cantidad < 0 || cantidad > 56000 || Number.isNaN(cantidad)){
        swal({
            title: "La cantidad está fuera de los rangos permitidos",
            text: "",
            type: "error",
            showCancelButton: true,
            cancelButtonText: "Cancelar",
            closeOnConfirm: false,
            confirmButtonColor: "#DD6B55",
            showLoaderOnConfirm: false,
            showConfirmButton: false
        });
    } else {
        if (confirm) {
            swal({
                title: "¿Seguro de editar esta clave?",
                text: "",
                type: "warning",
                showCancelButton: true,
                cancelButtonText: "Cancelar",
                closeOnConfirm: false,
                confirmButtonColor: "#DD6B55",
                showLoaderOnConfirm: true,
                confirmButtonText: "Continuar!"
            },
                    function () {

                        $.ajax({
                            url: "../ProcesarRequerimiento",
                            data: {accion: "actualizaRequerimiento", id: id, cantidad: cantidad},
                            type: 'POST',
                            dataType: 'JSON',
                            async: true,
                            success: function (data) {
                                if (data.msj) {
                                    swal({
                                        title: "Requerimiento actualizado correctamente!",
                                        text: "",
                                        type: "success"
                                    }, function () {
//                                    window.location = "ProcesarRequeFarmacia.jsp?UnidadSe=" + Unidad + "";
                                        $("#editando_" + id).hide();
                                        $("#editar_" + id).show();
                                        $("#label_" + id).show();
                                        $("#label_" + id).text(cantidad);
                                        $("#input_" + id).val(cantidad);
                                        $("#input_" + id).hide();
                                    });

                                } else {
                                    swal("Datos no Registrados", "", "error");
                                }

                            }
                        });

                    });
        } else {
            $.ajax({
                url: "../ProcesarRequerimiento",
                data: {accion: "actualizaRequerimiento", id: id, cantidad: cantidad},
                type: 'POST',
                dataType: 'JSON',
                async: true,
                success: function (data) {
                    if (data.msj) {
//                                    window.location = "ProcesarRequeFarmacia.jsp?UnidadSe=" + Unidad + "";
                        $("#editando_" + id).hide();
                        $("#editar_" + id).show();
                        $("#label_" + id).show();
                        $("#label_" + id).text(cantidad);
                        $("#input_" + id).hide();
                    } else {
                        swal("Datos no Registrados", "", "error");
                    }

                }
            });
        }
    }
}

function guardarIds(ids) {
    return new Promise(function (resolve, reject) {
        for (var i = 0; i < ids.length; i++) {
            var id = ids[i];
            var cantidad = $("#input_" + id).val();
            $("#editando_" + id).hide();
            $("#editar_" + id).show();
            $("#label_" + id).show();
            $("#label_" + id).text(cantidad);
            $("#input_" + id).hide();
            $.ajax({
                url: "../ProcesarRequerimiento",
                data: {accion: "actualizaRequerimiento", id: id, cantidad: cantidad},
                type: 'POST',
                dataType: 'JSON',
                async: true,
                success: function (data) {
                    if (data.msj) {
//                                    window.location = "ProcesarRequeFarmacia.jsp?UnidadSe=" + Unidad + "";

                    } else {
                        reject();
                    }

                }
            });
        }
        resolve();
    });
}

function guardarTodo(unidad, folio) {
    var ids = [];
    var changed = [];
    $("#datosCompras").find("label").each(function () {
        ids.push(this.id);
    });
    for (var i = 0; i < ids.length; i++) {
        var id = ids[i].substr(6);
        var valorAnterior = $('#' + ids[i]).text();
        var nuevoValor = $('#input_' + id).val();
        nuevoValor = parseInt(nuevoValor);
        if(nuevoValor <0 || nuevoValor >56000|| Number.isNaN(nuevoValor)){
            swal({
                title: "La cantidad " +nuevoValor +" está fuera de los rangos permitidos",
                text: "",
                type: "error",
                showCancelButton: true,
                cancelButtonText: "Cancelar",
                closeOnConfirm: false,
                confirmButtonColor: "#DD6B55",
                showLoaderOnConfirm: false,
                showConfirmButton: false
            });
            return;
        }
        if (valorAnterior != nuevoValor) {
            changed.push(id);
            console.log(id);
        }
    }
    var fecha = $('#fecha').val();
    if (fecha || changed.length > 0) {
        swal({
            title: "¿Seguro que desea guardar los cambios?",
            text: "",
            type: "warning",
            showCancelButton: true,
            cancelButtonText: "Cancelar",
            closeOnConfirm: false,
            confirmButtonColor: "#DD6B55",
            showLoaderOnConfirm: true,
            confirmButtonText: "Continuar!"
        },
                function () {

                    guardarIds(changed).then(function () {
                        if (fecha) {
                            $.ajax({
                                url: "../ProcesarRequerimiento",
                                data: {accion: "guardarFecha", unidad: unidad, folio: folio, fecha: fecha},
                                type: 'POST',
                                dataType: 'JSON',
                                async: true,
                                success: function (data) {
                                    if (data.msj) {
                                        swal({
                                            title: "Requerimiento actualizado correctamente!",
                                            text: "",
                                            type: "success"
                                        }, function () {
//                                    window.location = "ProcesarRequeFarmacia.jsp?UnidadSe=" + Unidad + "";
//                                            window.history.back();
                                            window.location = document.referrer;
                                        });

                                    } else {
                                        swal("Datos no Registrados", "", "error");
                                    }

                                }
                            });
                        } else {
//                        swal.close();
                            swal({
                                title: "Requerimiento actualizado correctamente!",
                                text: "",
                                type: "success"
                            }, function () {
//                                    window.location = "ProcesarRequeFarmacia.jsp?UnidadSe=" + Unidad + "";
                                window.location = document.referrer;
                            });
//                        swal("Requerimiento actualizado correctamente!", "", "success");
//                        alert("Requerimiento actualizado correctamente!");
                        }
                    }
                    );

                });
    }
}
function validarFiltros(){
    var fechaE1 = $("#fecha1").val();
    var fechaE2 = $("#fecha2").val();
    var fechaC1 = $("#fechaCap1").val();
    var fechaC2 = $("#fechaCap2").val();
    var juris = $("#juris").val();
    var unidad = $("#UnidadSe").val();
    
    if((fechaE1 && fechaE2) && !fechaC1 && !fechaC2 && !juris && !unidad){
        swal({
            title: "No se puede filtrar solo por fechas de entrega, seleccione otro parámetro",
            text: "",
            type: "error",
            showCancelButton: true,
            cancelButtonText: "Continuar",
            closeOnConfirm: false,
            confirmButtonColor: "#DD6B55",
            showLoaderOnConfirm: true,
            showConfirmButton: false
        });
        return false;
    }
    return true;
}