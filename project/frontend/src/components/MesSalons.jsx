import { useEffect, useState} from "react";

function MesSalons() {
    const [salons, setSalons] = useState([]);
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        fetch("/moi").then(res => res.json()).then(u => setUserId(u.id))
    }, []);

    useEffect(() => {
        if (!userId) return;
        fetch(`/canaux/proprietaire/${userId}`)
            .then(res => res.json())
            .then(data => setSalons(data))
    }, [userId])

    return (
        <div className="page-content">
            <h2>Mes salons de discussion</h2>

            {salons.length === 0 ? (
                <p>Aucune salon pour le moment.</p>
            ) : (
                salons.map(salon => (
                    <div key={salon.id} className="canal-card">
                        <div>
                            <h3>{salon.titre}</h3>
                            <p>{salon.description}</p>
                            <button onClick={() => window.open(`/chat/${salon.id}`, '_blank')}>
                                Rejoindre le chat
                            </button>
                        </div>
                    </div>
                ))
            )}
        </div>
    )
}

export default MesSalons;