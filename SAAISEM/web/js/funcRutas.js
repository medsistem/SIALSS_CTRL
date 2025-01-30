/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function agregaRuta(form1) {
    var F_ClaCli = form1.elements['F_ClaCli'].value;
    var F_FecSur = form1.elements['F_FecSur'].value;
    var enRuta = form1.elements['enRuta'].value;
    var F_Juris = form1.elements['F_Juris'].value;
    var F_Anio = form1.elements['F_Anio'].value;
    var F_Mes = form1.elements['F_Mes'].value;
    var idReg = form1.elements['idReg'].value;
    var F_Muni = form1.elements['F_Muni'].value;
    
    if(enRuta==="X"){
        //Se quitará el registro
        var dir = '../Rutas?accion=eliminaRuta&F_ClaCli='+F_ClaCli+'&F_FecSur='+F_FecSur+'';
        var form = $('#formIngresa');
        $.ajax({
            type: form.attr('method'),
            url: dir,
            data: form.serialize(),
            success: function(data) {
                recargaTabla();
            },
            error: function(data) {
                alert('Error al insertar el registro');
            }
        });
        
        function recargaTabla() {
            $('#registroRuta'+idReg).load('adminRutas.jsp?F_Juris=' + F_Juris + '&F_Mes=' + F_Mes + '&F_Anio=' + F_Anio + '&F_Muni='+F_Muni+' #registroRuta'+idReg);
        }
    } else {
        //Se añadirá el registro
        var dir = '../Rutas?accion=guardaRuta&F_ClaCli='+F_ClaCli+'&F_FecSur='+F_FecSur+'';
        var form = $('#formIngresa');
        $.ajax({
            type: form.attr('method'),
            url: dir,
            data: form.serialize(),
            success: function(data) {
                recargaTabla();
            },
            error: function(data) {
                alert('Error al insertar el registro');
            }
        });
        
        function recargaTabla() {
            $('#registroRuta'+idReg).load('adminRutas.jsp?F_Juris=' + F_Juris + '&F_Mes=' + F_Mes + '&F_Anio=' + F_Anio + '&F_Muni='+F_Muni+' #registroRuta'+idReg);
        }
    }
    //alert(F_ClaCli + "  " + F_FecSur);
    return false;
}