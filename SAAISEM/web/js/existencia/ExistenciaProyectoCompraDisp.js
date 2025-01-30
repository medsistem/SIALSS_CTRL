$(document).ready(function () {
    ObtenerProyecto();

    $('#Descargar').click(function () {
        var Proyecto = $("#Nombre option:selected").val();
        var ProyectoL = $('#Proyecto').val();
        if (parseInt(Proyecto) > 0) {
            window.open("../ExcelExistenciaDisponible?Proyecto=" + Proyecto + "&ProyectoL=" + ProyectoL + "");
        } else {
            swal("Atención", "Favor de seleccionar un proyecto", "warning");
        }
    });

    $('#Mostrar').click(function () {
        var Proyecto = $('#Proyecto').val();
        $.ajax({
            url: "../ExistenciaProyecto",
            data: {accion: "MostrarTodosCompraDisp", ProyectoCL: Proyecto},
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
        MostrarRegistros();
    });

    $("input[name=Consulta]").change(function () {
        MostrarRegistros();
    });

});

function ObtenerProyecto() {
    var Proyecto = $('#Proyecto').val();
    $('#Nombre option:gt(0)').remove();
    $("#Nombre").append("<option value=0>Seleccione</option>").select2();
    $('#Nombre option:gt(0)').remove();
    $.ajax({
        url: "../ExistenciaProyecto",
        data: {accion: "obtenerProyectosCliente", ProyectoCL: Proyecto},
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

function MostrarRegistros() {
    var Proyecto = $("#Nombre").val();
    $.ajax({
        url: "../ExistenciaProyecto",
        data: {accion: "MostrarRegistrosCompraDisp", Proyecto: Proyecto},
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
        var Proyecto = json[i].Proyecto;
        var ClaPro = json[i].ClaPro;
        var Descripcion = json[i].Descripcion;
        var Lote = json[i].Lote;
        var Caducidad = json[i].Caducidad;
        var Cantidad = json[i].Cantidad;
        var Presentacion = json[i].Presentacion;
        Contar = json[i].Contar;
        CantidadT = json[i].CantidadT;
        ContarClave = json[i].ContarClave;


        aDataSet.push([Proyecto, ClaPro, Descripcion,Presentacion, Lote, Caducidad, Cantidad]);

    }

    if (Contar > 0) {
        $("#Registro").css("display", "block");
        $('#total').html(CantidadT);
        $('#totalClave').html(ContarClave);
    } else {
        $("#Registro").css("display", "none");
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
                {"sTitle": "Proyecto", "sClass": "center"},
                {"sTitle": "Clave", "sClass": "center"},
                {"sTitle": "Descripción", "sClass": "center"},
                {"sTitle": "Presentación", "sClass": "center"},
                {"sTitle": "Lote", "sClass": "center"},
                {"sTitle": "Caducidad", "sClass": "center"},
                {"sTitle": "Cantidad", "sClass": "center"}
            ]
        });


    });
}