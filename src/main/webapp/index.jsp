<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>OTS :: Δοκιμή Ηλ.Υπογραφών</title>
        <!-- Le styles -->
        <link href="js/bootstrap/css/bootstrap.css" rel="stylesheet">
        <link href="js/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
        <style type="text/css">
            body {
                padding-top: 40px;
                padding-bottom: 40px;
                background-color: #f5f5f5;
            }

            .form-signin {
                max-width: 600px;
                padding: 19px 29px 29px;
                margin: 0 auto 20px;
                background-color: #fff;
                border: 1px solid #e5e5e5;
                -webkit-border-radius: 5px;
                -moz-border-radius: 5px;
                border-radius: 5px;
                -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
            }
            .form-signin .form-signin-heading,
            .form-signin .checkbox {
                margin-bottom: 10px;
            }
            .form-signin input[type="text"],
            .form-signin input[type="password"] {
                font-size: 16px;
                height: auto;
                margin-bottom: 15px;
                padding: 7px 9px;
            }

            .dropdown-menu { paddin-top: 5px; }

            .dropdown-menu label {
                padding: 0 10px;
            }

            .dropdown-menu input {
                margin-right: 5px;
                float: left;
            }


        </style>

        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

    </head>
    <body>
        <!--    <c:each item="${sessionScope.myNameList}"  >
                
            </c:each>
                <h1>Hello World!</h1>-->


        <div class="container">

            <form id="formPlaceholder" class="form-signin" action="uploadPdfFile" method="POST" enctype="multipart/form-data" target="_blank" >

                <h3 class="form-signin-heading">Τοποθέτηση Placeholders Ηλ. Υπογραφών σε έγγραφο</h3>
                <input type="hidden" name="pdftypeform" id="pdftypeform"/>

                <ul class="nav nav-pills">
                    <li><a href="#">Είδος εγγράφου:</a></li>
                    <li class="dropdown" id="menu1">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#menu1">
                            <span id="selectedpdftype">Επιλογή τύπου</span>
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a tabindex="-1" href="#" id="pdftype1">Τύπος Α</a></li>
                            <li><a tabindex="-1" href="#" id="pdftype2">Τύπος Β</a></li>
                            <li><a tabindex="-1" href="#" id="pdftype3">Τύπος Γ</a></li>
                            <!--<li class="divider"></li>
                            <li><a href="#">Separated link</a></li>-->
                        </ul>
                    </li>

                    <li>
                        <div class="alert alert-info" id="doctypealert">
                            <button type="button" class="close" >×</button>
                            Σύμφωνα με την παραμετροποίηση, ένα έγγραφο Τύπου <span id="document_type">A</span> 
                            επιδέχεται <span id="placeholder_no">3</span> υποδοχές για υπογραφές στην 1η σελίδα
                        </div>
                    </li>
                    <li><a href="#">Αρχείο:</a></li>
                    <li>
                        <i class="icon-file"></i><input type="file" name="pdfFileToPlaceholder"/>
                        <button class="btn btn-primary" type="submit" id="buttonUploadToPlaceholder">Upload</button>
                    </li>
                </ul>
            </form>



        </div> <!-- second form container -->

        <div class="container">

            <form id="formSign" class="form-signin" action="uploadPdfFile" method="POST" enctype="multipart/form-data" target="_blank" >

                <h3 class="form-signin-heading">Προσθήκη Α.Δ.Α. και Ηλ.Υπογραφής</h3>
                Αρχείο:
                <i class="icon-file"></i><input type="file" name="pdfFileToSign"/>
                <button class="btn btn-primary" type="submit" id="buttonUploadToSign">Upload</button>

            </form>
        </div> <!-- /container -->


        <!-- J' adore javascript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="js/bootstrap/js/jquery.min.js"></script>
        <script src="js/bootstrap/js/bootstrap.min.js"></script>
        <script src="js/bootstrap/js/bootstrap.js"></script>
        <script type="text/javascript">
            $(window).load(function(){
                $(".dropdown-toggle").dropdown();
                $('.alert').hide();
                $("#pdftype1").click();
                
            });
            
            
            // this function is called when an alert is clicked to close
            $('.alert .close').live("click", function(e) {
                $(this).parent().hide();
            });

            // this function is called when a drop down item is
            // clicked
            $(".dropdown-menu").click(function(event) {
                var menuid = event.target.id;
  
                var placeholderCount = 0;
                var pdfType = "Α";
      
                switch(menuid){
                    case "pdftype1":
                        placeholderCount = 1;
                        pdfType = "Α";
                        break;
                    case "pdftype2":
                        placeholderCount = 2;
                        pdfType = "B";
                        break;
                    case "pdftype3":
                        placeholderCount = 3;
                        pdfType = "Γ";
                        break;
                }
                $("#document_type").html(pdfType);
                $("#placeholder_no").html(placeholderCount);
                $("#selectedpdftype").html("Τύπος "+ pdfType);
                $("#pdftypeform").val(placeholderCount);
                
                // show information alert
                $(".alert").show();
            });
            
            //            $("#buttonUploadToSign").click(function() {
            //                alert("pdftypeform value = " + $("#pdftypeform").val());
            //            });
            //                     
            //            $("#buttonUploadToPlaceholder").click(function() {
            //                alert("pdftypeform value = " + $("#pdftypeform").val());
            //            });
             
            // check whether input file is a pdf
            $('INPUT[type="file"]').change(function () {
                var ext = this.value.match(/\.(.+)$/)[1];
                switch (ext) {
                    case 'pdf':
                        break;
                    default:
                        alert('Ο τύπος του αρχείου που επιλέξατε δεν είναι επιτρεπτός!\nΕπιλέξτε ένα αρχείο PDF');
                        this.value = '';
                }
            });
            
       
        </script>
    </body>  
</html>  
