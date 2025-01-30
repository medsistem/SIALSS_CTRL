$(document).ready(function() {

$('input[type=password]').keyup(function() {
    
	// set password variable
	var pswd = $(this).val();
        var ban1=0,ban2=0,ban3=0;
	//validate the length
if ( pswd.length < 8 ) {
	$('#length').removeClass('valid').addClass('invalid');
        ban1=0;
} else {
	$('#length').removeClass('invalid').addClass('valid');        
        ban1=1;
}
//validate letter
if ( pswd.match(/[A-z]/) ) {
	$('#letter').removeClass('invalid').addClass('valid');
} else {
	$('#letter').removeClass('valid').addClass('invalid');
}

//validate capital letter
if ( pswd.match(/[A-Z]/) ) {
	$('#capital').removeClass('invalid').addClass('valid');
        ban2=1;
} else {
	$('#capital').removeClass('valid').addClass('invalid');        
        ban2=0;
}

//validate number
if ( pswd.match(/\d/) ) {
	$('#number').removeClass('invalid').addClass('valid');
        ban3=1;
} else {
	$('#number').removeClass('valid').addClass('invalid');
        ban3=0;
}

var ban = ban1 + ban2 + ban3;
//alert(ban);

if(ban == 3){
    //alert("Si cumple contra");
    var pass = $('#pswd').val();
    var pass2 = $('#pswd2').val();
    
    if(pass == pass2){
        $('#igual').removeClass('invalid').addClass('valid');
        $('#action').attr("disabled", false);
    }else{
        $('#igual').removeClass('valid').addClass('invalid');
        $('#action').attr("disabled", true);
    }
}else{
    $('#igual').removeClass('valid').addClass('invalid');
        $('#action').attr("disabled", true);
}

}).focus(function() {
	$('#pswd_info').show();
}).blur(function() {
	$('#pswd_info').hide();
});


});