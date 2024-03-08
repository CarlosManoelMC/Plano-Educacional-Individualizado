function visualizarSenha() {
    var senhaInput = document.getElementById('senha');
    var checkbox = document.getElementById('visualizar-senha');

    if (checkbox.checked) {
        senhaInput.type = "text";
    } else {
        senhaInput.type = "password";
    }
}
