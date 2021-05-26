<?php
require_once "conexion.php";
require_once "jwt.php";
if ($_SERVER["REQUEST_METHOD"] == "GET") {
    if (isset($_GET['user']) && isset($_GET['password'])) {
        $c = conexion();
        $s = $c -> prepare ("SELECT * FROM users WHERE user=:u AND password=SHA1(:p)");
        $s->bindValue(":u",$_GET['user']);
        $s->bindValue(":p",$_GET['password']);
        $s->execute();
        $s-> setFetchMode(PDO::FETCH_ASSOC);
        $r = $s -> fetch();
        if ($r) {
            $jwt = JWT :: create(array("user"=>$_GET['user']),"fgtdre5hbjvnyrdc");
            $r = array("login"=>"s","token"=>$jwt);
            header("HTTP/1.1 200 ok");
        }else{
            $r = array("login"=>"n","token"=>"Error de Usuario y/o Contraseña");
            header("HTTP/1.1 200 ok");
        }
        
        echo json_encode($r);
    }else{
        header("HTTP/1.1 400 Bad Request");
    }
}else{
    header("HTTP/1.1 400 Bad Request");
    echo "Metodo no soportado";
}

?>