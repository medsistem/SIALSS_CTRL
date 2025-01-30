$(document).ready(function () {
    ObtenerJurisdiccionAud();
    ObtenerTipoUnidadAud();
    ObtenerUnidadesAud();

    $("#SelectJuris").change(function () {
        var Jurisdiccion = $("#SelectJuris option:selected").val();

        $('#SelectUnidad option:gt(0)').remove();
        $("#SelectUnidad").append("<option value=0>Seleccione</option>").select2();
        $('#SelectUnidad option:gt(0)').remove();

        $('#SelectMuni option:gt(0)').remove();
        $("#SelectMuni").append("<option value=0>Seleccione</option>").select2();
        $('#SelectMuni option:gt(0)').remove();
        $.ajax({
            url: "../ReporteadorConsulta",
            data: {accion: "obtenerMunicipioAud", Jurisdiccion: Jurisdiccion},
            type: 'POST',
            dataType: 'JSON',
            async: true,
            success: function (data) {
                $.each(data, function (i, valueProyecto) {
                    $("#SelectMuni").append("<option value=" + valueProyecto.Id + ">" + valueProyecto.Nombre + "</option>").select2();
                });
            }
        });
    });

    $("#SelectMuni").change(function () {
        var Jurisdiccion = $("#SelectJuris option:selected").val();
        var Municipio = $("#SelectMuni option:selected").val();

        $('#SelectUnidad option:gt(0)').remove();
        $("#SelectUnidad").append("<option value=0>Seleccione</option>").select2();
        $('#SelectUnidad option:gt(0)').remove();
        $.ajax({
            url: "../ReporteadorConsulta",
            data: {accion: "obtenerUnidadAud", Jurisdiccion: Jurisdiccion, Municipio: Municipio},
            type: 'POST',
            dataType: 'JSON',
            async: true,
            success: function (data) {
                $.each(data, function (i, valueProyecto) {
                    $("#SelectUnidad").append("<option value=" + valueProyecto.Id + ">" + valueProyecto.Nombre + "</option>").select2();
                });
            }
        });
    });
});

function ObtenerJurisdiccionAud() {
    $('#SelectJuris option:gt(0)').remove();
    $("#SelectJuris").append("<option value=0>Seleccione</option>").select2();
    $('#SelectJuris option:gt(0)').remove();
    $.ajax({
        url: "../ReporteadorConsulta",
        data: {accion: "obtenerJurisdiccionesAud"},
        type: 'GET',
        dataType: 'JSON',
        async: true,
        success: function (data) {
            $.each(data, function (i, valueProyecto) {
                $("#SelectJuris").append("<option value=" + valueProyecto.Id + ">" + valueProyecto.Nombre + "</option>").select2();
            });
        }
    });
}

function ObtenerTipoUnidadAud() {
    $('#SelectTipoUnidad option:gt(0)').remove();
    $("#SelectTipoUnidad").append("<option value=0>Seleccione</option>").select2();
    $('#SelectTipoUnidad option:gt(0)').remove();
    $.ajax({
        url: "../ReporteadorConsulta",
        data: {accion: "obtenerTipoUnidadAud"},
        type: 'GET',
        dataType: 'JSON',
        async: true,
        success: function (data) {
            $.each(data, function (i, valueProyecto) {
                $("#SelectTipoUnidad").append("<option value=" + valueProyecto.Id + ">" + valueProyecto.Nombre + "</option>").select2();
            });
        }
    });
}

function ObtenerUnidadesAud() {
    var Jurisdiccion = $("#SelectJuris option:selected").val();
    if (Jurisdiccion == "0") {

        $('#SelectUnidad option:gt(0)').remove();
        $("#SelectUnidad").append("<option value=0>Seleccione</option>").select2();
        $('#SelectUnidad option:gt(0)').remove();
        $.ajax({
            url: "../ReporteadorConsulta",
            data: {accion: "obtenerUnidadesAud"},
            type: 'GET',
            dataType: 'JSON',
            async: true,
            success: function (data) {
                $.each(data, function (i, valueProyecto) {
                    $("#SelectUnidad").append("<option value=" + valueProyecto.Id + ">" + valueProyecto.Nombre + "</option>").select2();
                });
            }
        });
    }
}
