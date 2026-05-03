// Fonction pour générer un mot de passe aléatoire (inspirée du TD)
function genererMotDePasse() {
    const chars = "abcdefghijklmnopqrstuvwxyz!@#$%^&*()-+<>ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    let pass = "";
    // On force une longueur de 12 caractères pour être sûr d'avoir un bon mot de passe
    for (let x = 0; x < 12; x++) {
        let i = Math.floor(Math.random() * chars.length);
        pass += chars.charAt(i);
    }
    // Injecte le résultat dans l'input
    document.getElementById("motDePasse").value = pass;
}

// Interception de la soumission du formulaire pour valider les données
document.getElementById("form-ajout-user").addEventListener("submit", function (event) {
    let isValid = true;

    // 1. Validation du nom (uniquement des lettres, espaces, tirets)
    const nomInput = document.getElementById("nom");
    const nomRegex = /^[a-zA-ZÀ-ÿ\s\-]+$/;
    if (!nomRegex.test(nomInput.value)) {
        nomInput.classList.add("is-invalid");
        isValid = false;
    } else {
        nomInput.classList.remove("is-invalid");
    }

    // 2. Validation du prénom
    const prenomInput = document.getElementById("prenom");
    if (!nomRegex.test(prenomInput.value)) {
        prenomInput.classList.add("is-invalid");
        isValid = false;
    } else {
        prenomInput.classList.remove("is-invalid");
    }

    // 3. Validation du mot de passe 
    // Exigence : majuscules, minuscules, chiffres, caractères spéciaux, longueur 8 min
    const mdpInput = document.getElementById("motDePasse");
    const mdpError = document.getElementById("mdp-error");
    const mdpRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&\-+<>])[A-Za-z\d@$!%*?&\-+<>]{8,}$/;

    if (!mdpRegex.test(mdpInput.value)) {
        mdpInput.classList.add("is-invalid");
        mdpError.style.display = "block";
        isValid = false;
    } else {
        mdpInput.classList.remove("is-invalid");
        mdpError.style.display = "none";
    }

    // Si une des validations a échoué, on bloque l'envoi du formulaire
    if (!isValid) {
        event.preventDefault();
    }
});