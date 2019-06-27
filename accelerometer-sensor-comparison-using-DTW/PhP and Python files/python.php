<?php
$command = escapeshellcmd('backend.py');
$output = shell_exec($command);
echo $output;
