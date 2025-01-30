$(document).ready(function () {
    ObtenerJurisdiccion();
    ObtenerTipoUnidad();
    ObtenerUnidades();

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
            data: {accion: "obtenerMunicipio", Jurisdiccion: Jurisdiccion},
            type: 'GET',
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
            data: {accion: "obtenerUnidad", Jurisdiccion: Jurisdiccion, Municipio: Municipio},
            type: 'GET',
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

function ObtenerJurisdiccion() {
    $('#SelectJuris option:gt(0)').remove();
    $("#SelectJuris").append("<option value=0>Seleccione</option>").select2();
    $('#SelectJuris option:gt(0)').remove();
    $.ajax({
        url: "../ReporteadorConsulta",
        data: {accion: "obtenerJurisdicciones"},
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

function ObtenerTipoUnidad() {
    $('#SelectTipoUnidad option:gt(0)').remove();
    $("#SelectTipoUnidad").append("<option value=0>Seleccione</option>").select2();
    $('#SelectTipoUnidad option:gt(0)').remove();
    $.ajax({
        url: "../ReporteadorConsulta",
        data: {accion: "obtenerTipoUnidad"},
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

function ObtenerUnidades() {
    var Jurisdiccion = $("#SelectJuris option:selected").val();
    if (Jurisdiccion == "0") {

        $('#SelectUnidad option:gt(0)').remove();
        $("#SelectUnidad").append("<option value=0>Seleccione</option>").select2();
        $('#SelectUnidad option:gt(0)').remove();
        $.ajax({
            url: "../ReporteadorConsulta",
            data: {accion: "obtenerUnidades"},
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
