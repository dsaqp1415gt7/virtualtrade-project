var API_BASE_URL = "http://147.83.7.155:8080/virtualtrade-api";

var id = $.cookie('id');
var password = $.cookie('userpass')
var email = $.cookie('email')
var anuncioid =id;



$("#button_delete").click(function(e) {
	e.preventDefault();
	delete_anuncio();
	
});

$("#button_update").click(function(e) {
	e.preventDefault();
	document.location.href = '/virtualtrade/modificaranuncio.html';
});

//
//$("#button_update").click(function(e) {
//	e.preventDefault();
//	update_anuncio();
//});

//
//function update_anuncio() {
//	
//	var url = API_BASE_URL + '/anuncios/' + id;
//
//	$.ajax({
//		url : url,
//		type : 'PUT',
//		crossDomain : true,
//		dataType : 'json',
//		headers : {
//			"Accept" : "application/vnd.virtual.api.anuncio+json",
//			"Content-Type" : "application/vnd.virtual.api.anuncio+json"
//		},
//		beforeSend : function(request) {
//			request.withCredentials = true;
//			request.setRequestHeader("Authorization", "Basic "
//					+ btoa($.cookie('email') + ':' + $.cookie('userpass')));
//		},
//
//	}).done(function(data, status, jqxhr) {
//		window.location = "http://147.83.7.155/virtualtrade/index.html"
//
//	}).fail(function(jqXHR, textStatus) {
//		console.log(textStatus);
//	});
//
//}
//



$("#logout").click(function(e) {
    e.preventDefault();
    console.log("dsag");
   logout();
});

function logout() {
	$("#logout").hide();
    $("#perfil").hide();
    $("#anadirnuevo").hide();
    $("#buscar").hide();
    $("#mensajes").hide();
		 $.removeCookie('email');
		 $.removeCookie('userpass');
         $.cookie('loggedin', "nologueado");
        window.location = "http://147.83.7.155/virtualtrade/index.html";
}


$(document).ready(function() {
	
	if ($.cookie('loggedin')=='nologueado'){
	  	  $("#logout").hide();
	      $("#perfil").hide();
	      $("#anadirnuevo").hide();
	      $("#buscar").hide();
	      $("#mensajes").hide();
	}

	if ($.cookie('loggedin')=='logueado'){
	    $("#singup").hide();
	    $("#signin").hide();
	}
	
	
	$(button_delete).hide();
	$(button_update).hide();
	getanuncio();
	
});


$("#send-message").click(function(e) {
	e.preventDefault();
	getEnviarMensaje();
	
});

function getEnviarMensaje() {
	$.cookie('destinatario',destinatario);
	$.cookie('anuncioid',anuncioid);	
	document.location.href='/virtualtrade/redactar.html';
}

function delete_anuncio() {
	
	var url = API_BASE_URL + '/anuncios/' + id;

	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true,
		
		dataType : 'json',
		beforeSend : function(request) {
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "
					+ btoa($.cookie('email') + ':' + $.cookie('userpass')));
		},

	}).done(function(data, status, jqxhr) {
		window.location = "http://147.83.7.155/virtualtrade/index.html"

	}).fail(function(jqXHR, textStatus) {
		console.log(textStatus);
	});
}


function getanuncio() {

	console.log(id);
	console.log("geawgd");

	var url = API_BASE_URL + "/anuncios/"+id;

	$.ajax(
			{
				url : url,
//				cache: true,
				type : 'GET',
				crossDomain : true,
				dataType : 'json',
				beforeSend : function(request) {
					request.withCredentials = true;
					request.setRequestHeader("Authorization", "Basic "
							+ btoa($.cookie('email') + ':' + $.cookie('userpass')));
					
				},
				

			}).done(function(data, status, jqxhr) {
	//	var response = JSON.parse(jqxhr.responseText);
		var anuncio = JSON.parse(jqxhr.responseText);
	//	var anuncio = response.anuncio;
	//	var links = response.links;

		// $.cookie('idanuncio', );

//		next = links[0].uri;
//		prev = links[1].uri;

//		console.log("El proximo next es:" + next);

			destinatario=anuncio.email;
			console.log=("El creador del anuncio es:"+destinatario)
			foto_anuncio1.src = anuncio.imagenes[0].urlimagen;
			$("#title1").text(anuncio.subject);
			$("titulooo").text(anuncio.subject);
			$("#content1").text(anuncio.content);
			$("#precio1").text(anuncio.precio + " \u20ac");
			
			
			if($.cookie('email') == (destinatario||"adminmail")){
				$(button_delete).show();
				$(button_update).show();
			}
		
			if(anuncio.imagenes[1].urlimagen != undefined){
				foto_anuncio2.src = anuncio.imagenes[1].urlimagen;
			}
			
			else if (anuncio.imagenes[1].urlimagen != undefined){
				foto_anuncio3.src = anuncio.imagenes[2].urlimagen;
			}
			
//			$.removeCookie('id');
//		
//		else if (id == 2) {
//
//			foto_anuncio1.src = anuncios[1].imagenes[0].urlimagen;
//			$("#title1").text(anuncios[1].subject);
//			$("#content1").text(anuncios[1].content);
//			$("#precio1").text(anuncios[1].precio + " \u20ac");
//		}
//		else if (id == 3) {
//
//			foto_anuncio1.src = anuncios[2].imagenes[0].urlimagen;
//			$("#title1").text(anuncios[2].subject);
//			$("#content1").text(anuncios[2].content);
//			$("#precio1").text(anuncios[2].precio + " \u20ac");
//		}
//
//		else if (id == 4) {
//
//			foto_anuncio1.src = anuncios[3].imagenes[0].urlimagen;
//			$("#title1").text(anuncios[3].subject);
//			$("#content1").text(anuncios[3].content);
//			$("#precio1").text(anuncios[3].precio + " \u20ac");
//		}
//		
//		
//		else if (id == 4) {
//
//			foto_anuncio1.src = anuncios[3].imagenes[0].urlimagen;
//			$("#title1").text(anuncios[3].subject);
//			$("#content1").text(anuncios[3].content);
//			$("#precio1").text(anuncios[3].precio + " \u20ac");
//		}
//		
//		else if (id == 5) {
//
//			foto_anuncio1.src = anuncios[4].imagenes[0].urlimagen;
//			$("#title1").text(anuncios[4].subject);
//			$("#content1").text(anuncios[4].content);
//			$("#precio1").text(anuncios[4].precio + " \u20ac");
//		}
//		else if (id == 6) {
//
//			foto_anuncio1.src = anuncios[5].imagenes[0].urlimagen;
//			$("#title1").text(anuncios[5].subject);
//			$("#content1").text(anuncios[5].content);
//			$("#precio1").text(anuncios[5].precio + " \u20ac");
//		}	else if (id == 7) {
//
//			foto_anuncio1.src = anuncios[6].imagenes[0].urlimagen;
//			$("#title1").text(anuncios[6].subject);
//			$("#content1").text(anuncios[6].content);
//			$("#precio1").text(anuncios[6].precio + " \u20ac");
//		}
//		else if (id == 8) {
//
//			foto_anuncio1.src = anuncios[7].imagenes[0].urlimagen;
//			$("#title1").text(anuncios[7].subject);
//			$("#content1").text(anuncios[7].content);
//			$("#precio1").text(anuncios[7].precio + " \u20ac");
//		}
//		else if (id == 9) {
//
//			foto_anuncio1.src = anuncios[8].imagenes[0].urlimagen;
//			$("#title1").text(anuncios[8].subject);
//			$("#content1").text(anuncios[8].content);
//			$("#precio1").text(anuncios[8].precio + " \u20ac");
//		}
//		

	}).fail(function() {
		$("#anuncios_result").text("No hay anuncios");
	});


}
