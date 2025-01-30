$(function ()
{
     ObtenerProyecto();
     
     $("#Nombre").change(function()
     {
         var idProyecto = $("#Nombre").val();
         
         
         obtenerExistencias(idProyecto);
     });
     
     $("#Descargar").click(function(){
         
         var idProyecto = $("#Nombre").val(); 
         var nombreProyecto = $("#Nombre option:selected").text();
         
         if(idProyecto === "Seleccione" )
         {
             swal("Atención", "Favor de verificar cricterios de búsqueda seleccionar un proyecto por favor", "warning");
             return false;
         }
         else
         {
             window.open(context + '/ComprasController?proyecto='+idProyecto+'&descripcionProyecto='+nombreProyecto+'');
         }
         
     });
    
     
     
});


function obtenerExistencias(proyecto)
{    
    $.ajax({
        url: context + "/ComprasController",
        data: {accion: buscar_por_proyecto, proyecto:proyecto},
        type: 'POST',
        async: true,
        dataType: 'json',
        beforeSend: function ()
        {
            $("#myModal").modal({show: true});
        },
        success: function (data) {
           MostrarExistencias(data);
           $("#myModal").modal('hide');
           
        }, error: function (jqXHR, textStatus, errorThrown) {

            alert("Error Contactar al departamento de sistemas");

        }
    });
}

function MostrarExistencias(data) {

    $("#example").remove();
    var nombreProyecto = $("#Nombre option:selected").text();

    var json = data;
    var aDataSet = [];
    for (var i = 0; i < json.length; i++)
    {
        var clave = json[i].clave;
        var descripcion = json[i].descripcion;
        var ubicacionesTemporales = json[i].letra;
        var existenciaDisponible = json[i].existenciaDisponible;
        var existenciaTemporal = json[i].existenciaTemporal;
        var existenciaTotal = json[i].existenciaTotal;

        aDataSet.push([nombreProyecto,clave, descripcion, ubicacionesTemporales, existenciaDisponible,
            existenciaTemporal, existenciaTotal]);
    }
    $(document).ready(function () {
        $('#dynamic').html('<table class="table table-borded table-condensed table-striped " width="100%" id="example"></table>');
        $('#example').dataTable({
            "aaData": aDataSet,
            "bAutoWidth": true,
            "aoColumns": [
                {"sTitle": "Proyecto", "sClass": "text-center"},
                {"sTitle": "Clave", "sClass": "text-center"},
                {"sTitle": "Descripción", "sClass": "text-center"},
                {"sTitle": "Ubicaciones Temporales","sClass": "text-center"},
                {"sTitle": "Existencia Disponible","sClass": "text-center"},
                {"sTitle": "Existencia Temporal", "sClass": "text-center"},
                {"sTitle": "Existencia Total", "sClass": "text-center"}

            ]
        });


    });


}

function ObtenerProyecto() {
    $("#Nombre").append("<option>Seleccione</option>").select2();
    $.ajax({
        url: context + "/ExistenciaProyecto",
        data: {accion: "obtenerProyectosCompras"},
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
