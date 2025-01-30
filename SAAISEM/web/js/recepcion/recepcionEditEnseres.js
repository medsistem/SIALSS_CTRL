$(function () {
    $("#validarRemision").click(function () {
        var ordenCompra = $("#vOrden").val();
        var Proveedor = $("#vRemi").val();
        $.ajax({
            type: "GET",
            url: "FacturacionEnseres?accion=obtenerIdReg&OrdenCompra=" + ordenCompra + "&Proveedor=" + Proveedor + "&Unidad=1001A&Folio=0&Cantidad=0",
            dataType: "json",
            async: false,
            success: function (data) {
                $.each(data, function (idx, elem) {
                    var isValidado;
                    isValidado = elem.validado;

                    var Cantidad = $('#Cantidad_' + elem.Id).val();
                    $.ajax({
                        type: "GET",
                        url: "FacturacionEnseres?accion=ActualizarReq&OrdenCompra=" + ordenCompra + "&Proveedor=" + Proveedor + "&Cantidad=" + Cantidad + "&IdRegistro=" + elem.Id + "&Unidad=1001A&Folio=0",
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            $.each(data, function (idx, elem) {
                                var isValidado;
                                isValidado = elem.validado;
                            });
                        }
                    });
                });
            }
        });
        swal({
            title: "¿Seguro de Validar la Compra ENSERES?",
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
                        url: "FacturacionEnseres",
                        data: {accion: "IngresarRemision", ordenCompra: ordenCompra, Proveedor: Proveedor, Unidad: "1001A", Folio: "0", Cantidad: "0"},
                        type: 'POST',
                        dataType: 'JSON',
                        async: true,
                        success: function (data) {
                            if (data.msj) {
                                swal({
                                    title: "Compra validada correctamente!",
                                    text: "",
                                    type: "success"
                                }, function () {
                                    window.location = "main_menu.jsp";
                                });
                            } else {
                                alert("Error en la Validación contactar al departamento de sistemas.");
                            }
                            location.reload();
                        }, error: function (jqXHR, textStatus, errorThrown) {
                            alert("Error en sistema");
                        }
                    });
                });
    });
});