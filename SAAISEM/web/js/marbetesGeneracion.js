$(function ()
{
    $("#folNumber").focus();
    $("#searchButton").click(function () {
        var folio = $("#folNumber").val();
        var Nombre = $("#Nombre").val();
        if ((folio === "") || (Nombre === "0")) {
            alert("Ingresar un número de folio o Proyecto");
        } else {
            $.ajax({
                url: "../marbetesGenerar",
                data: {ban: 0, folio: folio, Proyecto: Nombre},
                type: 'POST',
                async: false,
                success: function (data)
                {
                    if (data.unidad === "") {
                        alert("Folio inexistente");
                        $("#uniName").val("");
                        $("#RF").val("");
                        $("#Cont").val("");

                    } else {
                        $("#uniName").val(data.unidad);
                        $("#RF").val(data.RF);
                        $("#Proyecto").val(Nombre);
                        $("#Ct").val(data.Ct);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("Error Contactar al departamento de sistemas");
                }
            });
        }
    });
    
   $("#generarMarbete").click(function ()
    {
        var folio = $("#folNumber").val();
        var unidad = $("#uniName").val();
        var marbetes = $("#marbetNumber").val();
        var Proyecto = $("#Proyecto").val();
        var RF = $("#RF").val();
        var Ct = $("#Ct").val();
        var ruta = $("#ruta").val();


        if (folio === "") {
            alert("Ingresar número de folio.");
            return false;
        } else if (unidad === "") {
            alert("Ingresar Unidad de atención.");
            return false;
        } else if (marbetes === "") {
            alert("Ingresar cantidad de marbetes");
            return false;
        } else if (ruta === "") {
            alert("Ingresar cantidad de ruta");
            return false;
        }else {
            $.ajax({
                url: "../marbetesGenerar",
                data: {ban: 1, folio: folio, unidad: unidad, marbetes: marbetes, Proyecto: Proyecto,ruta: ruta},
                type: 'POST',
                async: false,
                success: function (data)
                {
                    if (data.msj === "realizado") {
                        $("#folNumber").val("");
                        $("#uniName").val("");
                        $("#marbetNumber").val("");
                        window.open("../Marbetes/MarbeteN.jsp?folio=" + folio + "&RF=" + RF + "&Proyecto=" + Proyecto+  "&Ct=" + Ct);
                    } else {
                        alert("Error contactar al departamento de sistemas.");
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("Error Contactar al departamento de sistemas");
                }
            });
        }
    });
    
    $("#generarMarbete0").click(function () 
    {
        var folio = $("#folNumber0").val();
        var folio2 = $("#folNumber1").val();
        var unidad = $("#uniName").val();
        var marbetes = $("#marbetNumber").val();
        var Proyecto = $("#Nombre").val();
        var ruta = $("#ruta").val();
        var RF = 0;
        var Ct = 0;
        
        if (folio === "") {
            alert("Ingresar número de folio.");
            return false;
        } else if (unidad === "") {
            alert("Ingresar Unidad de atención.");
            return false;
        } else if (marbetes === "") {
            alert("Ingresar cantidad de marbetes");
            return false;
        } else {
            $.ajax({
                url: "../marbetesGenerar",
                data: {ban: 2, folio: folio, unidad: unidad, marbetes: marbetes, Proyecto: Proyecto, ruta:ruta},
                type: 'POST',
                async: false,
                success: function (data)
                {
                    if (data.msj === "realizado") {
                        $("#folNumber").val("");
                        $("#uniName").val("");
                        $("#marbetNumber").val("");
                        window.open("../Marbetes/MarbeteN.jsp?folio=" + folio + "&RF=" + RF + "&Proyecto=" + Proyecto + "&Ct=" + Ct);
                    } else {
                        alert("Error contactar al departamento de sistemas.");
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("Error Contactar al departamento de sistemas");
                }
            });
        }
   
        
    });

});


