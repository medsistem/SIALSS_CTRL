$(document).ready(function () {
    ObtenerProyecto();

    $('#Descargar').click(function () {
     
        var ConsultaT = $('#Consulta').val();
        var Proyecto = $("#Nombre option:selected").val();
        if(Proyecto !== "0" && ConsultaT !== null ){
            window.open("../ExcelExistenciaProyecto?Proyecto=" + Proyecto + "&Tipo=Usuario&Consulta=" + ConsultaT + "");
        }else{

      alert("Los campos no deben ir vacios, seleccione una opcion");
        }
    });

    $('#Mostrar').click(function () {
        var Consulta = $('#Consulta').val();
        $.ajax({
            url: "../ExistenciaProyecto",
            data: {accion: "MostrarTodos", Tipo: Consulta},
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
        ObtenerProyecto();
    });

    $("#Nombre").change(function () {
        //var Nombre = $("#Nombre option:selected").val();
        MostrarRegistros();
    });

    $("#Consulta").change(function () {

        //var Nombre = $("#Nombre option:selected").val();
        MostrarRegistros();

    });

});

function ObtenerProyecto() {
    $('#Nombre option:gt(1)').remove();
    $("#Nombre").append("<option value=0 selected disabled>Seleccione</option>").select2();
    $('#Nombre option:gt(0)').remove();
    $.ajax({
        url: "../ExistenciaProyecto",
        data: {accion: "obtenerProyectos"},
        type: 'GET',
        dataType: 'JSON',
        async: true,
        success: function (data) {
            $.each(data, function (i, valueProyecto) {
                $("#Nombre").append("<option value=" + valueProyecto.Id + ">" + valueProyecto.Nombre + " </option>").select2();
            });
        }
    });
}

function MostrarRegistros() {
    var Proyecto = $("#Nombre").val();
    var Consulta = $('#Consulta').val();
    if (Consulta && Proyecto !== "0") {
        $.ajax({
            url: "../ExistenciaProyecto",
            data: {accion: "MostrarRegistros", Proyecto: Proyecto, Tipo: Consulta},
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
}

function limpiarTabla() {
    $("#example").remove();
}

function MostrarTabla(data) {
    var json = JSON.parse(data);
    var aDataSet = [];
    var Contar, CantidadT, ContarClave;
    for (var i = 0; i < json.length; i++) {
        var json = JSON.parse(data);
    var aDataSet = [];
    var Contar, CantidadT, ContarClave;
    for (var i = 0; i < json.length; i++) {
        var Proyecto = json[i].Proyecto;
        var ClaPro = json[i].ClaPro;
        var Descripcion = json[i].Descripcion;
        var Lote = json[i].Lote;
        var Caducidad = json[i].Caducidad;
        var Cantidad = json[i].Cantidad;
        var Origen = json[i].Origen;
        var Presentacion = json[i].Presentacion;
        var Costo = json[i].Costo;
        var Monto = json[i].Monto;
        var IdProyecto = json[i].IdProyecto;
        var Tipo = json[i].Tipo;
        var Ubicacion = json[i].Ubicacion;
        var OrdenSuministro = json[i].OrdenSuministro;
        var Ubicacion = json[i].Ubicacion;
        var Proveedor = json[i].Proveedor;
        var FechaMov = json[i].FechaMov;
        var FechaIng = json[i].FechaIng;
        var Documento = json[i].Documento;
        var Oc = json[i].Oc;
        var Remision = json[i].Remision;
        var Lugar = json[i].Lugar;
        var OrdSuministro = json[i].OrdSuministro;
        Contar = json[i].Contar;
        CantidadT = json[i].CantidadT;
        ContarClave = json[i].ContarClave;
        //alert(IdProyecto);
        if ((IdProyecto == 9) || (IdProyecto == 14) || (IdProyecto == 15) || (IdProyecto == 3)) {
            switch (Tipo) {
               case "1":
                    aDataSet.push([Proyecto, ClaPro, Descripcion, Presentacion, Cantidad]);
                    break;
                case "2":
                    aDataSet.push([Proyecto, ClaPro, Descripcion, Presentacion, Lote, Caducidad, Cantidad, Costo, Monto, Origen,Proveedor]);
                    break;
                case "3":
                    aDataSet.push([Proyecto, ClaPro, Descripcion, Lote, Caducidad,FechaMov,FechaIng, Ubicacion, Cantidad, Origen,Documento,Oc,Remision,OrdSuministro,Lugar]);
                    break;
                case "4":
                    aDataSet.push([Proyecto, ClaPro,Lote,Caducidad, Descripcion, Presentacion, Cantidad]);
                    break;
                default:
                    aDataSet.push([Proyecto, ClaPro, Descripcion, Lote, Caducidad, Cantidad, Origen,Proveedor,Lugar]);
                    break;
                    
            }

        } else {
            switch (Tipo) {
               case "1":
                    aDataSet.push([Proyecto, ClaPro, Descripcion, Cantidad]);
                    break;
                case "2":
                    aDataSet.push([Proyecto, ClaPro, Descripcion, Lote, Caducidad, Cantidad, Origen,Proveedor]);
                    break;
                case "3":
                    aDataSet.push([Proyecto, ClaPro, Descripcion, Lote, Caducidad, FechaMov, FechaIng, Ubicacion, Cantidad, Origen, Documento, Oc, Remision, OrdSuministro, Lugar]);
                    break;
                case "4":
                    aDataSet.push([Proyecto, ClaPro,Lote,Caducidad, Descripcion, Presentacion, Cantidad]);
                    break;
                default:
                    aDataSet.push([Proyecto, ClaPro, Descripcion, Lote, Caducidad, Cantidad, Origen, OrdenSuministro,Proveedor,Lugar]);
                    break;
            }
        }
    }

    if (Contar > 0) {
        $("#Registro").css("display", "block");
        $('#total').html(CantidadT);
        $('#totalClave').html(ContarClave);
    } else {
        $("#Registro").css("display", "none");
    }

    $(document).ready(function () {
        if ((IdProyecto == 9) || (IdProyecto == 14) || (IdProyecto == 15) || (IdProyecto == 3)) {

            switch (Tipo) {
                case "1":
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
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Descripción", "sClass": "center"},
                            {"sTitle": "Presentación", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"}
                        ]
                    });
                    break;
                case "2":
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
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Descripción", "sClass": "center"},
                            {"sTitle": "Presentación", "sClass": "center"},
                            {"sTitle": "Lote", "sClass": "center"},
                            {"sTitle": "Caducidad", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"},
                            {"sTitle": "Costo", "sClass": "center"},
                            {"sTitle": "Monto", "sClass": "center"},
                            {"sTitle": "Origen", "sClass": "center"},
                            {"sTitle": "Proveedor", "sClass": "center"}
                        ]
                    });
                    break;
                case "3":
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
                            
                            
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Descripción", "sClass": "center"},
                            {"sTitle": "Lote", "sClass": "center"},
                            {"sTitle": "Caducidad", "sClass": "center"},
                            {"sTitle": "Fecha Movimiento", "sClass": "center"},
                            {"sTitle": "Fecha Ingreso", "sClass": "center"},
                            {"sTitle": "Ubicación", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"},
                            {"sTitle": "Origen", "sClass": "center"},
                            {"sTitle": "No. documento", "sClass": "center"},
                            {"sTitle": "Orden de compra", "sClass": "center"},
                            {"sTitle": "Remision", "sClass": "center"},
                            {"sTitle": "Orden de Suministro", "sClass": "center"},                            
                            {"sTitle": "Bodega", "sClass": "center"}
                         
                        ]
                    });
                    break;
                    
                     case "4":
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
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Lote", "sClass": "center"},
                            {"sTitle": "Caducidad", "sClass": "center"},
                            {"sTitle": "Descripcion", "sClass": "center"},
                            {"sTitle": "Presentacion", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"}
                        ]
                    });
                    break;
                default:
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
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Descripción", "sClass": "center"},
                            {"sTitle": "Presentación", "sClass": "center"},
                            {"sTitle": "Lote", "sClass": "center"},
                            {"sTitle": "Caducidad", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"}
                        ]
                    });
                    break;
            }


        } else {
            switch (Tipo) {
                case "1":
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
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Descripción", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"}
                        ],
                        "createdRow": function (row, data, dataIndex) {
                            if (data[2].includes('APARTADO')) {
                                $(row).css('background-color', '#f5c6cb');
                            }
                        }
                    });
                    break;
                case "2":
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
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Descripción", "sClass": "center"},
                            {"sTitle": "Lote", "sClass": "center"},
                            {"sTitle": "Caducidad", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"},
                            {"sTitle": "Origen", "sClass": "center"},
                            {"sTitle": "Proveedor", "sClass": "center"}
                          
                        ],
                        "createdRow": function (row, data, dataIndex) {
                            if (data[2].includes('APARTADO')) {
                                $(row).css('background-color', '#f5c6cb');
                            }
                        }
                    });
                    break;
                case "3":
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
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Descripción", "sClass": "center"},
                            {"sTitle": "Lote", "sClass": "center"},
                            {"sTitle": "Caducidad", "sClass": "center"},
                            {"sTitle": "Fecha Movimiento", "sClass": "center"},
                            {"sTitle": "Fecha Ingreso", "sClass": "center"},
                            {"sTitle": "Ubicación", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"},
                            {"sTitle": "Origen", "sClass": "center"},
                            {"sTitle": "No. documento", "sClass": "center"},
                            {"sTitle": "Orden de compra", "sClass": "center"},
                            {"sTitle": "Remision", "sClass": "center"},
                            {"sTitle": "Orden de Suministro", "sClass": "center"},                            
                            {"sTitle": "Bodega", "sClass": "center"}
                        ],
                        "createdRow": function (row, data, dataIndex) {
                            if (data[2].includes('APARTADO')) {
                                $(row).css('background-color', '#f5c6cb');
                            }
                        }
                    });
                    break;
                    case "4":
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
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Lote", "sClass": "center"},
                            {"sTitle": "Caducidad", "sClass": "center"},
                            {"sTitle": "Descripcion", "sClass": "center"},
                            {"sTitle": "Presentacion", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"}
                        ]
                    });
                    break;
                default:
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
                            {"sTitle": "Proyecto", "sClass": "center"},
                            {"sTitle": "Clave", "sClass": "center"},
                            {"sTitle": "Descripción", "sClass": "center"},
                            {"sTitle": "Lote", "sClass": "center"},
                            {"sTitle": "Caducidad", "sClass": "center"},
                            {"sTitle": "Cantidad", "sClass": "center"},
                            {"sTitle": "Origen", "sClass": "center"},
                            {"sTitle": "Orden Suministro", "sClass": "center"}
                        ]
                    });
                    break;
            }

        }
    });

}}