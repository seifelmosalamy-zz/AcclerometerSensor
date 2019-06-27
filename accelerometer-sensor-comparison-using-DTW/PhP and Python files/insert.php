<?php
    echo 'sh8aaaaaaaala5rmara';
    $db_name = "mostafa123";
    $mysql_username = "root";
    $mysql_password = "";
    $server_name = "localhost";
    $conn = mysqli_connect($server_name, $mysql_username, $mysql_password,$db_name);
    
    $xaxis = $_POST["xaxis"];
    $yaxis = $_POST["yaxis"];
    $zaxis = $_POST["zaxis"];
    
    $mysql_qry = "Insert into test_data (`xaxis`, `yaxis`, `zaxis`) Values ('$xaxis', '$yaxis', '$zaxis')";
    $result = mysqli_query($conn ,$mysql_qry);
    if(mysqli_num_rows($result) > 0) {
        echo 'login success !!!!! Welcome user';
    }
    else {
        echo 'mfesh data';
    }

?>
