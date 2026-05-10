import { useEffect, useState} from "react";

function MesSalons() {
    const [salons, setSalons] = useState([]);
    useEffect(() => {
        fetch("/canaux/proprietaire/1")
            .then(res => res.json())
            .then(data => setSalons(data))
    }, [])
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
                        </div>
                    </div>
                ))
            )}
        </div>
    )
};

export default MesSalons;