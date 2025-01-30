<%-- 
    Document   : cambioFechas
    Created on : 14/04/2015, 12:58:35 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="javax.print.PrintService"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%DecimalFormat format = new DecimalFormat("####,###");%>
<%

    HttpSession sesion = request.getSession();
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionInvUni con = new ConectionInvUni();

    
   
    
    
    String fecha_ini="",fecha_fin="";
    try {
        fecha_ini = request.getParameter("fecha_ini");        
        fecha_fin = request.getParameter("fecha_fin");
    } catch (Exception e) {

    }
    if(fecha_ini==null){
        fecha_ini="";
    }
    if(fecha_fin==null){
        fecha_fin="";
    }
%>
<html>
    <style>
/* Prevent the text contents of draggable elements from being selectable. */
[draggable] {
  -moz-user-select: none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  user-select: none;
  /* Required to make elements draggable in old WebKit */
  -khtml-user-drag: element;
  -webkit-user-drag: element;
}
.column {
  height: 150px;
  width: 150px;
  float: left;
  border: 2px solid #666666;
  background-color: #ccc;
  margin-right: 5px;
  -webkit-border-radius: 10px;
  -ms-border-radius: 10px;
  -moz-border-radius: 10px;
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 3px #000;
  -ms-box-shadow: inset 0 0 3px #000;
  box-shadow: inset 0 0 3px #000;
  text-align: center;
  cursor: move;
}
.column header {
  color: #fff;
  text-shadow: #000 0 1px;
  box-shadow: 5px;
  padding: 5px;
  background: -moz-linear-gradient(left center, rgb(0,0,0), rgb(79,79,79), rgb(21,21,21));
  background: -webkit-gradient(linear, left top, right top,
                               color-stop(0, rgb(0,0,0)),
                               color-stop(0.50, rgb(79,79,79)),
                               color-stop(1, rgb(21,21,21)));
  background: -webkit-linear-gradient(left center, rgb(0,0,0), rgb(79,79,79), rgb(21,21,21));
  background: -ms-linear-gradient(left center, rgb(0,0,0), rgb(79,79,79), rgb(21,21,21));
  border-bottom: 1px solid #ddd;
  -webkit-border-top-left-radius: 10px;
  -moz-border-radius-topleft: 10px;
  -ms-border-radius-topleft: 10px;
  border-top-left-radius: 10px;
  -webkit-border-top-right-radius: 10px;
  -ms-border-top-right-radius: 10px;
  -moz-border-radius-topright: 10px;
  border-top-right-radius: 10px;
}

.column.over {
  border: 2px dashed #000;
}
</style>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>
            
            <div class="panel-heading">
                <h3 class="panel-title">Reporteador</h3>
            </div>
            <form action="InvUnidades.jsp" method="post">
            <div class="panel-footer">                
                <div class="row">
                    <div id="columns">
                        <div class="column" draggable="true"><header>A</header></div>
                        <div class="column" draggable="true"><header>B</header></div>
                        <div class="column" draggable="true"><header>C</header></div>
                  </div>
                </div>
            </div>
            <div class="panel-footer">                
                <div class="row">
                    <table class="table-bordered col-sm-4">
                        <tr>
                            <td>
                                <div id="columns">
                                    <div class="column" draggable="false"><header></header></div>
                                </div>
                            </td>     
                            <td>
                                <div id="columns">
                                    <div class="column" draggable="false"><header></header></div>
                                </div>
                            </td>
                            <td>
                                <div id="columns">
                                    <div class="column" draggable="false"><header></header></div>
                                </div>
                            </td>
                            <td>
                                <div id="columns">
                                    <div class="column" draggable="false"><header></header></div>
                                </div>
                            </td>
                        </tr>
                        
                        
                    </table>
                </div>
            </div>    
                <div class="panel-body">
                    <div class="row">
                            <button class="btn btn-block btn-primary" id="btn_capturar" onclick="return confirma();">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                    </div>
                </div>  
            </form>
           
            
                
                
                    
                    
                
            
        </div>

        
        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/bootstrap-datepicker.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
            $(document).ready(function() {
                $('#datosCompras').dataTable();
                $("#fecha").datepicker();
                $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                //$('#btnRecalendarizar').attr('disabled', true);
                //$('#btnImpMult').attr('disabled', true);
            });            
        </script>
        <script>
var dragSrcEl = null;

function handleDragStart(e) {
  // Target (this) element is the source node.
  this.style.opacity = '0.4';

  dragSrcEl = this;

  e.dataTransfer.effectAllowed = 'move';
  e.dataTransfer.setData('text/html', this.innerHTML);
}

function handleDragOver(e) {
  if (e.preventDefault) {
    e.preventDefault(); // Necessary. Allows us to drop.
  }

  e.dataTransfer.dropEffect = 'move';  // See the section on the DataTransfer object.

  return false;
}

function handleDragEnter(e) {
  // this / e.target is the current hover target.
  this.classList.add('over');
}

function handleDragLeave(e) {
  this.classList.remove('over');  // this / e.target is previous target element.
}

function handleDrop(e) {
  // this/e.target is current target element.

  if (e.stopPropagation) {
    e.stopPropagation(); // Stops some browsers from redirecting.
  }

  // Don't do anything if dropping the same column we're dragging.
  if (dragSrcEl != this) {
    // Set the source column's HTML to the HTML of the columnwe dropped on.
    dragSrcEl.innerHTML = this.innerHTML;
    this.innerHTML = e.dataTransfer.getData('text/html');
  }

  return false;
}

function handleDragEnd(e) {
  // this/e.target is the source node.

  [].forEach.call(cols, function (col) {
    col.classList.remove('over');
  });
}

var cols = document.querySelectorAll('#columns .column');
[].forEach.call(cols, function(col) {
  col.addEventListener('dragstart', handleDragStart, false);
  col.addEventListener('dragenter', handleDragEnter, false)
  col.addEventListener('dragover', handleDragOver, false);
  col.addEventListener('dragleave', handleDragLeave, false);
  col.addEventListener('drop', handleDrop, false);
  col.addEventListener('dragend', handleDragEnd, false);
});
        </script>
        <script>
 
            
        </script>
        
    </body>
</html>

