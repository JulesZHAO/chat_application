import {useState, useEffect} from "react";

function Planifier() {
    const [titre, setTitre] = useState("");
    const [description, setDescription] = useState("");
    const [dateHoraire, setDateHoraire] = useState("");
    const [dureeValidite, setDureeValidite] = useState("");
    const [message, setMessage] = useState("");
    const [userId, setUserId] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        fetch("/moi")
            .then(res => res.ok ? res.json() : null)
            .then(u => setUserId(u?.id ?? null))
            .catch(() => setUserId(null));
    }, []);

    function handleSubmit(e) {
        e.preventDefault();
        if (!userId) {
            setMessage("Utilisateur non connecté.");
            return;
        }
        if (!titre.trim() || !description.trim() || !dateHoraire || !dureeValidite) {
            setMessage("Tous les champs sont obligatoires.");
            return;
        }
        if (parseInt(dureeValidite) <= 0) {
            setMessage("La durée doit être supérieure à 0.");
            return;
        }

        setIsSubmitting(true);
        fetch('/canaux', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                titre: titre,
                description: description,
                dateHoraire: dateHoraire,
                dureeValidite: parseInt(dureeValidite),
                proprietaireId: userId
            })
        }).then(res => {
            if (res.ok) {
                setMessage('Salon créé avec succès !');
                setTitre('');
                setDescription('');
                setDateHoraire('');
                setDureeValidite('');
            } else {
                setMessage('Erreur lors de la creation.');
            }
        }).catch(() => setMessage('Erreur de connexion au serveur.'))
          .finally(() => setIsSubmitting(false));
    }

    return (
        <div className="page-content">
            <h2>Planifier une discussion</h2>

            {message && <p className={message.includes('succès') ? 'success-message' : 'error-message'}>{message}</p>}

            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Titre</label>
                    <input
                        type="text"
                        value={titre}
                        onChange={(e) => setTitre(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Description</label>
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Date et heure</label>
                    <input
                        type="datetime-local"
                        value={dateHoraire}
                        onChange={(e) => setDateHoraire(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Durée (minutes)</label>
                    <input
                        type="number"
                        value={dureeValidite}
                        onChange={(e) => setDureeValidite(e.target.value)}
                        min="1"
                        required
                    />
                </div>

                <button type="submit" className="btn-primary" disabled={isSubmitting}>
                    {isSubmitting ? "Création..." : "Créer"}
                </button>
            </form>
        </div>
    );
}

export default Planifier;
