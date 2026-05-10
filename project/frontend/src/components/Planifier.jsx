import {useState} from "react";

function Planifier() {
    const [titre, setTitre] = useState("");
    const [description, setDescription] = useState("");
    const [dateHoraire, setDateHoraire] = useState("");
    const [dureeValidite, setDureeValidite] = useState("");
    const [message, setMessage] = useState("");

    function handleSubmit(e) {
        e.preventDefault();

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
                proprietaire: { id: 1 , isAdmin: false, isActif: false}
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
        });
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
                    />
                </div>

                <div className="form-group">
                    <label>Description</label>
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                    />
                </div>

                <div className="form-group">
                    <label>Date et heure</label>
                    <input
                        type="datetime-local"
                        value={dateHoraire}
                        onChange={(e) => setDateHoraire(e.target.value)}
                    />
                </div>

                <div className="form-group">
                    <label>Durée (minutes)</label>
                    <input
                        type="number"
                        value={dureeValidite}
                        onChange={(e) => setDureeValidite(e.target.value)}
                    />
                </div>

                <button type="submit" className="btn-primary">Créer</button>
            </form>
        </div>
    );
}

export default Planifier;