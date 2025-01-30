$(document).ready(function () {
    //obtenerClave();
    $("#Descrip").keyup(function () {
        var Nombre = $("#Descrip").val();
        var ProyectoCL = $("#ProyectoCL").val();
        $("#Descrip").autocomplete({
            source: "AutoCompleteDescripcion?accion=BuscarDescripcionCliente&ProyectoCL=" + ProyectoCL + "&descrip=" + Nombre,
            select: function (event, ui) {
                $("#Descrip").val(ui.item.DesPro);
                return false;
            }
        }).data("ui-autocomplete")._renderItem = function (ul, item) {
            return $("<li>")
                    .data("ui-autocomplete-item", item)
                    .append("<a>" + item.DesPro + "</a>")
                    .appendTo(ul);
        };
    });
});

/*function obtenerClave() {
 $('#Clave option:gt(0)').remove();
 $("#Clave").append("<option value=0>Seleccione</option>").select2();
 $('#Clave option:gt(0)').remove();
 $.ajax({
 url: "ConsultaKardex",
 data: {accion: "obtenerClave"},
 type: 'GET',
 dataType: 'JSON',
 async: true,
 success: function (data) {
 $.each(data, function (i, valueProyecto) {
 $("#Clave").append("<option value=" + valueProyecto.Clave + ">" + valueProyecto.Descripcion + "</option>").select2();
 });
 }
 });
 }*/
