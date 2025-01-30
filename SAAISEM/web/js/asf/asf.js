
/* 
 * Ing. JUAN MANUEL CISNEROS SANTILLAN
 * DESARROLLO DE APLICACIONES 2018
 * TODOS LOS DERECHOS RESERVADOS.
 */

$(function () {
    
    showTable("tablaASF","-1", "-1");
    
    $("#consultarMeses").click(function(){
        
        
        
        var meses =$("#meses").val();
        var prioridad = $("#prioridad").val();
        
        if(meses === "" )
        {
            alert("Ingresar un número de meses válido por favor");
            return false;
        }
        else if(prioridad === "")
        {
            alert("Ingresar una prioridad válida por favor");
            return false;
        }
        
        showTable("tablaASF",meses,prioridad);
        
    });
    
    $("#downloadAsf").click(function(){
        
        var meses = $("#meses").val();
        var prioridad = $("#prioridad").val();
        
        meses = (meses === "") ? "-1" : meses; 
        prioridad = (prioridad === "") ? "-1" : prioridad; 
        window.open(currentContext + '/ReporteASFReloaded?proyecto='+proyecto+'&meses='+meses+'&prioridad='+prioridad+'');
    });
    
    
});

function showTable(accion, meses, prioridad)
{
    var table;
    if (table === undefined) {
        options = {};
        options.destroy=true;
        options.columns = [
            {title: "No."},
            {title: "Prioridad"},
            {title: "Clave"},
            {title: "Descripción"},
            {title: "Origen"},
            {title: "Proveedor"},
            {title: "Tipo"},
            {title: "Inventario"},
            {title: "CPM MORELIA"},
            {title: "ASF CPM"},
            {title: "Requerido"},
            {title: "Surtido"},
            {title: "CPD Requerido"},
            {title: "CPD Surtido"},
            {title: "Sobre Inventario"},
            {title: "NIM"},
            {title: "C.U."},
            {title: "Importe"},
            {title: "Última recepción"},
            {title: "Fecha ORI"},
            {title: "Cantidad Ori"},
            {title: "Cantidad Max."},
            {title: "Cantidad Reg."},
            {title: "% Abasto"},
            {title: "Grupo terapeútico"},
            {title: "Comentarios"}

        ];
        options.columnDefs = [
            {targets: "_all", className: "center"}
        ];
        options.order = [0, "asc"];
        options.scrollY = "500px";
        options.scrollX = true;
        options.fixedColumns = true;
        table = $('#datosProv').DataTable(options);

    }


   toogleLoader('#datosProv');
    $.ajax({
        url: currentContext + "/ReporteASFReloaded",
        data: {accion: accion, proyecto: proyecto, meses:meses, prioridad:prioridad},
        type: 'POST',
        async: true,
        dataType: 'JSON',
        success: function (data)
        {
            table.clear();
            table.rows.add(data);
            $.ajax({
                url: currentContext + "/ReporteASFReloaded",
                data: {accion: "totales", proyecto: proyecto},
                type: 'POST',
                async: true,
                success: function (data)
                {
                    json = JSON.parse(data);
                    $("#clavesAutorizadas").text(json.clavesAutorizadas);
                    $("#medicamento").text(json.medicamento);
                    $("#materialCuracion").text(json.materialCuracion);
                    $("#soluciones").text(json.soluciones);
                    $("#clavesEnCero").text(json.clavesEnCero);
                    $("#existenciasTotales").text(json.existenciasTotales);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("Clave invalida");
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Clave invalida");
        },
        complete: function (data) {
            toogleLoader('#datosProv');
            table.columns.adjust().draw();
        }
    });
}


