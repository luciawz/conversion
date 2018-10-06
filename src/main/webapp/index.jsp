<%@ page contentType="text/html; ISO-8859-1" language="java" %>
<html>
<head>
    <title>Conversion</title>
</head>
<body>
    Please choose a .csv file and click Convert
    <form action="/csvUpload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" value="select csv"/>
        <input id="submit_form" type="submit" class="btn btn-success save" value="Convert"/>
    </form>


</body>
</html>