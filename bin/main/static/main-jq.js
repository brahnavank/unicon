$(document).ready(function () {
	
    $("#custom-file-label").css("display","unset");
    $(".custom-file-input").on("change", function () {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
	
	// Upload single file
    $('#singleUploadForm').submit(function (event) {
        $("#converting").removeClass("d-none");
        $("#home").addClass("d-none");

        var formElement = this;
        var formData = new FormData(formElement);

        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/toHtml",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                var message = 
                    "<h4 class=\"alert-heading\">Conversion done!</h4>" +
                    "<p>" +
                    "<span class=\"e-fe-icon e-fe-html\"></span>" + response.fileName +
                    "</p>" +
                    "<hr>" +
                    "<p class=\"mb-0\">" +
                    "<a class=\"btn btn-primary\" href='" + response.fileDownloadUri + "' target='_blank'>" + "Download" + "</a>" +
                    "&nbsp;&nbsp;<a class=\"btn btn-success\" href='" + "/" + "' target='_blank'>" + "Convert again" + "</a>"+
                	"</p>";
                $("#stampFileUploadSuccess").html(message);
                $("#stampFileUploadSuccess").removeClass("d-none");
                $("#converting").addClass("d-none");
            },
            error: function (error) {
				// log error
                console.log(error);
                $("#converting").addClass("d-none");
                $("#singleFileUploadError").removeClass("d-none");
            }
        });

        event.preventDefault();
    });


	// upload single file
    $( ".single-upload" ).on( "click", "#delete", function() {
        console.log( $( this ).text() );
        $.ajax({
            type: "GET",
            url: "/deleteAll",
            processData: false,
            contentType: false,
            success: function (response) {
                console.log(JSON.stringify(response))
                var message = "<ul class=\"list-group list-group-flush\">";
                message +="<li class=\"list-group-item\"><button id=\"delete\" class=\"btn btn-danger col-2 glyphicon glyphicon-remove primary submit-btn \">Delete all</button></li>";
             	message += "<li class=\"list-group-item\">"+response+"</li>";
                message +="</ul>";
                $("#stampFileUploadSuccess").html(message);
                $("#stampFileUploadSuccess").removeClass("d-none");
                $("#converting").addClass("d-none");
            },
            error: function (error) {
				// log error
                console.log(error);
                $("#converting").addClass("d-none");
                $("#singleFileUploadError").removeClass("d-none");
            }
        });
    });

	// show files
    $('#files').click(function () {
        $("#home").addClass("d-none");

        $.ajax({
            type: "GET",
            url: "/files",
            processData: false,
            contentType: false,
            success: function (response) {
				// log response
                console.log(JSON.stringify(response))
                var message = "<ul class=\"list-group list-group-flush\">";
                message +="<li class=\"list-group-item\"><button id=\"delete\" class=\"btn btn-danger col-2 glyphicon glyphicon-remove primary submit-btn \">Delete all</button></li>";

                if (response.length === 0) {
                    message += "<li class=\"list-group-item\">Folder is empty</li>";

                }else{
                    $.each( response, function( index, value ){
                        message += "<li class=\"list-group-item\"><a rel=\"import\"  href=\"/downloadHtmlFile/"+value.fileName+"\">"+value.fileName+"</a></li>";
                    });
                }
                message +="</ul>";
                $("#stampFileUploadSuccess").html(message);
                $("#stampFileUploadSuccess").removeClass("d-none");
                $("#converting").addClass("d-none");
            },
            error: function (error) {
				// log error
                console.log(error);
                $("#converting").addClass("d-none");
                $("#singleFileUploadError").removeClass("d-none");
            }
        });
     });
});
