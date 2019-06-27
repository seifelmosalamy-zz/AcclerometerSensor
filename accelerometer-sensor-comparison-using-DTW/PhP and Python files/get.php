<?php
    $db_name = "mostafa123";
    $mysql_username = "root";
    $mysql_password = "";
    $server_name = "localhost";
    $conn = mysqli_connect($server_name, $mysql_username, $mysql_password,$db_name);
    
    
    $sql = "SELECT shape_number FROM result_data ORDER BY id DESC LIMIT 1";
    
    $result = $conn->query($sql);
    
    if ($result->num_rows > 0) {
        // output data of each row
        while($row = $result->fetch_assoc()) {
            $string = "";
            $string2 = "";
            $string = $row["shape_number"];
            $string2 = substr($string,12,2);
            echo json_encode($string2);
        }
    }
    


?>
