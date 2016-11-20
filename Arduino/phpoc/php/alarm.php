<?php
include_once "cmd.php";

$pid = pid_open("/mmap/net1");                // 1번 NET 열기(무선랜)
$spi_pid = pid_open("/mmap/spi0");

$log_pid = array(0, 0, 0, 0);
$log_pid[0] = pid_open("/mmap/log0");
$log_pid[1] = pid_open("/mmap/log1");
$log_pid[2] = pid_open("/mmap/log2");
$log_pid[3] = pid_open("/mmap/log3");

pid_ioctl($spi_pid, "set mode 3");
pid_ioctl($spi_pid, "set role slave");

$isEmergency = "";

$isEmergency = _GET("isEmergency");


 if ($isEmergency == "true") {
 	echo "Emergency on";
 	pid_ioctl($spi_pid, "req start");
 	slave_write(ERR_OK, pid_ioctl($pid, "get mode"));
}
else {
	echo "Emergency off";
}

?>