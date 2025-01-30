$(function () {

    $("#selectOrdenCompra").select2();
    $("#selectProveedor").select2();
    reporteOrdenesCerradas();
    $("#searchByNoOrdenCOmpra").click(function ()
    {
        var noOrdenCompra = $("#selectOrdenCompra").val();

        $.ajax({
            url: context + "/OrdenesCompraController",
            data: {accion: porNoOrden, noOrdenCompra: noOrdenCompra},
            type: 'POST',
            async: true,
            dataType: 'json',
            beforeSend: function ()
            {
                $("#myModal").modal({show: true});
            },
            success: function (data)
            {

                MostrarOrdenCompra(data);
                $("#myModal").modal('hide');


            }, error: function (jqXHR, textStatus, errorThrown) {

                alert("Error Contactar al departamento de sistemas");

            }
        });


    });

    $("#searchByProvider").click(function ()
    {
        var proveedor = $("#selectProveedor").val();

        $.ajax({
            url: context + "/OrdenesCompraController",
            data: {accion: porProveedor, proveedor: proveedor},
            type: 'POST',
            async: true,
            dataType: 'json',
            beforeSend: function ()
            {
                $("#myModal").modal({show: true});
            },
            success: function (data)
            {

                MostrarOrdenCompra(data);
                $("#myModal").modal('hide');


            }, error: function (jqXHR, textStatus, errorThrown) {

                alert("Error Contactar al departamento de sistemas");

            }
        });


    });


});


function MostrarOrdenCompra(data) {

    $("#example").remove();

    var json = data;
    var aDataSet = [];
    for (var i = 0; i < json.length; i++)
    {
        var noCompra = json[i].noCompra;
        var fecha = json[i].fecha;
        var proveedor = json[i].proveedor;

        var btnCerrarOrden = '<button class="btn btn-sm btn-info btn-sm" onclick="cerrarOrden(\'' + noCompra + '\')" type="button"><span class="glyphicon glyphicon-remove"></span></button>';

        aDataSet.push([noCompra, fecha, proveedor, btnCerrarOrden]);
    }
    $(document).ready(function () {
        $('#dynamic').html('<table class="table table-borded table-condensed table-striped " width="100%" id="example"></table>');
        $('#example').dataTable({
            "aaData": aDataSet,
            "bAutoWidth": true,
            "aoColumns": [
                {"sTitle": "No. Ori", "sClass": "text-center"},
                {"sTitle": "Fecha", "sClass": "center"},
                {"sTitle": "Proveedor", "sClass": "center"},
                {"sTitle": "Cerrar", "sClass": "text-center"}
            ]
        });

    });
}
function cerrarOrden(noOrden)
{
    swal({
        title: "Cerrar orden de compra",
        text: "¿Estas seguro de cerrar esta orden de compra?  Esta acción no se puede revertir",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Aceptar",
        closeOnConfirm: false
    }, function ()
    {
        $.ajax({
            url: context + "/OrdenesCompraController",
            data: {accion: closeOrder, noOrdenCompra: noOrden},
            type: 'POST',
            async: true,
            dataType: 'JSON',
            success: function (data)
            {

                if (data.msj === "false")
                {
                    swal({
                        title: "Error al cerrar la orden",
                        text: "Error al cerrar la orden de comra contactar con el departamento de sistemas.",
                        type: "warning",
                        showCancelButton: false,
                        confirmButtonClass: "btn-info",
                        confirmButtonText: "Ingresar",
                        closeOnConfirm: false
                    });
                } else
                {
                    swal({
                        title: "Proceso exitoso",
                        text: "Se cerró la orden correctamente",
                        type: "success",
                        showCancelButton: false,
                        confirmButtonClass: "btn-primary",
                        confirmButtonText: "Aceptar"
                    }, function ()
                    {
                        location.reload();
                    });


                }
            }, error: function (jqXHR, textStatus, errorThrown) {

                alert("Error Contactar al departamento de sistemas");

            }
        });

    });
}
function reporteOrdenesCerradas()
{
    $.ajax({
        url: context + "/OrdenesCompraController",
        data: {accion: reporteOrdenes},
        type: 'POST',
        async: true,
        dataType: 'JSON',
        success: function (data)
        {
            MostrarOrdenCompraCerrada(data);
            
        }, error: function (jqXHR, textStatus, errorThrown) {

            alert("Error Contactar al departamento de sistemas");

        }
    });

}

function MostrarOrdenCompraCerrada(data) {

    $("#exampleCerradas").remove();

    var json = data;
    var aDataSet = [];
    for (var i = 0; i < json.length; i++)
    {
        var noCompra = json[i].noCompra;
        var fecha = json[i].fecha;
        var usuario = json[i].usuario;
        aDataSet.push([noCompra, fecha, usuario]);
    }
    $(document).ready(function () {
        $('#dynamiCerradas').html('<table class="table table-borded table-condensed table-striped " width="100%" id="exampleCerradas"></table>');
        $('#exampleCerradas').dataTable({
            "aaData": aDataSet,
            "bAutoWidth": true,
            "aoColumns": [
                {"sTitle": "No. Ori", "sClass": "text-center"},
                {"sTitle": "Fecha", "sClass": "center"},
                {"sTitle": "Usuario", "sClass": "center"}
            ]
        });

    });
}