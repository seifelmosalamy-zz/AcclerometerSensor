<?php
echo 'sh8aaaaaaaala5rmara';
$db_name = "mostafa123";
$mysql_username = "root";
$mysql_password = "";
$server_name = "localhost";
$conn = mysqli_connect($server_name, $mysql_username, $mysql_password,$db_name);

$mysql_qry = "SELECT `shape_number` FROM `result_data` ORDER BY id DESC LIMIT 1";
$result = mysqli_query($conn ,$mysql_qry);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        echo "Shape Number: " . $row["shape_number"] . "<br>";
        echo json_encode($row);
    }
} else {
    echo "0 results";
}
